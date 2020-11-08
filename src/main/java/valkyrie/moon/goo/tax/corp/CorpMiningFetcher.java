package valkyrie.moon.goo.tax.corp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.IndustryApi;
import net.troja.eve.esi.model.CorporationMiningObserverResponse;
import net.troja.eve.esi.model.CorporationMiningObserversResponse;
import valkyrie.moon.goo.tax.auth.EsiApi;
import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterManagement;
import valkyrie.moon.goo.tax.config.ConfigProperties;
import valkyrie.moon.goo.tax.marketData.dtos.MoonOre;
import valkyrie.moon.goo.tax.marketData.dtos.MoonOreReprocessConstants;
import valkyrie.moon.goo.tax.marketData.dtos.RefinedMoonOre;
import valkyrie.moon.goo.tax.marketData.moonOre.MoonOreRepository;
import valkyrie.moon.goo.tax.marketData.refinedMoonOre.RefinedMoonOreRepository;

@Component
public class CorpMiningFetcher {

	private static final Logger LOG = LoggerFactory.getLogger(CorpMiningFetcher.class);

	@Autowired
	private EsiApi api;

	@Autowired
	private CharacterManagement characterManagement;

	@Autowired
	private ConfigProperties config;

	@Autowired
	private MoonOreRepository moonOreRepository;
	@Autowired
	private RefinedMoonOreRepository refinedMoonOreRepository;

	private final IndustryApi industryApi = new IndustryApi();

	public void fetchMiningStatistics() {
		industryApi.setApiClient(api.getApi());
		Character leadChar = characterManagement.getLeadChar();
		List<RefinedMoonOre> refinedMoonOres = refinedMoonOreRepository.findAll();
		//		refinedMoonOres.stream().collect(Collectors.toMap(RefinedMoonOre::getId, refinedMoonOres));

		try {
			Map<Integer, Character> touchedChars = getMiningLog(leadChar.getCorpId(), refinedMoonOres);

			// get debt
			calculateDebt(refinedMoonOres, touchedChars);

		} catch (ApiException apiException) {
			apiException.printStackTrace();
		}

	}

	private Map<Integer, Character> getMiningLog(Integer corpId, List<RefinedMoonOre> refinedMoonOres) throws ApiException {
		List<CorporationMiningObserversResponse> corporationCorporationIdMiningObservers = industryApi.getCorporationCorporationIdMiningObservers(corpId, EsiApi.DATASOURCE, null, null, null);
		Set<Long> observerIds = new HashSet<>();
		Map<Integer, Character> touchedChars = new HashMap<>();
		for (CorporationMiningObserversResponse corporationCorporationIdMiningObserver : corporationCorporationIdMiningObservers) {
			observerIds.add(corporationCorporationIdMiningObserver.getObserverId());
			List<CorporationMiningObserverResponse> corporationCorporationIdMiningObserversObserverId = industryApi.getCorporationCorporationIdMiningObserversObserverId(corpId, corporationCorporationIdMiningObserver.getObserverId(), EsiApi.DATASOURCE, null, null, null);
			for (CorporationMiningObserverResponse corporationMiningObserverResponse : corporationCorporationIdMiningObserversObserverId) {
				Integer id = corporationMiningObserverResponse.getCharacterId();

				Character character = lookupCharacter(touchedChars, id);

				Map<Integer, MoonOre> minedMoonOre = character.getMinedMoonOre();
				Integer minedOreTypeId = corporationMiningObserverResponse.getTypeId();

				prepareMoonOre(minedMoonOre, minedOreTypeId, refinedMoonOres);

				long minedAmount = minedMoonOre.get(minedOreTypeId).getMinedAmount();// total mined for this type
				minedMoonOre.get(minedOreTypeId).setMinedAmount(minedAmount + corporationMiningObserverResponse.getQuantity());
				touchedChars.put(character.getId(), character);
			}
		}
		return touchedChars;
	}

	private Character lookupCharacter(Map<Integer, Character> touchedChars, Integer id) {
		Character character;
		if (!touchedChars.containsKey(id)) {
			character = characterManagement.findCharacter(id);
		} else {
			character = touchedChars.get(id);
		}
		return character;
	}

	private void calculateDebt(List<RefinedMoonOre> refinedMoonOres, Map<Integer, Character> touchedChars) {
		float refinementMultiplier = config.getRefinementMultiplier();
		float tax = config.getTax();

		// build refinedMoonOre datastructure:
		Map<String, Float> refinedMoonOreMap = new HashMap<>();

		refinedMoonOres.forEach(ore -> {
			refinedMoonOreMap.put(ore.name, ore.price);
		});

		for (Map.Entry<Integer, Character> touchedCharacter : touchedChars.entrySet()) {
			float currentDept = 0;
			Integer characterId = touchedCharacter.getKey();
			Character character = touchedCharacter.getValue();
			for (Map.Entry<Integer, MoonOre> minedOre : character.getMinedMoonOre().entrySet()) {
				Integer oreId = minedOre.getKey();
				MoonOre ore = minedOre.getValue();
				currentDept = calculatePrice(refinementMultiplier, tax, refinedMoonOreMap, currentDept, ore);
			}
			character.getDept().setToPay((long) (character.getDept().getToPay() + currentDept));
			character.getDept().setCharacterId(characterId);

			characterManagement.saveChar(character);
		}
	}

	private float calculatePrice(float refinementMultiplier, float tax, Map<String, Float> refinedMoonOreMap, float currentDept, MoonOre ore) {
		//calculate value of 100 pieces
		List<Pair<String, Integer>> pairs = MoonOreReprocessConstants.reprocessConstants.get(ore.name);
		for (Pair<String, Integer> pair : pairs) {
			float price = refinedMoonOreMap.get(pair.getLeft());
			float finalPrice = (float) (price * pair.getRight() * refinementMultiplier * ore.getMultiplier() * tax);
			currentDept += finalPrice * ((int) ore.getMinedAmount() / 100) ;
		}
		return currentDept;
	}

	private void prepareMoonOre(Map<Integer, MoonOre> minedMoonOre, Integer minedOreTypeId, List<RefinedMoonOre> refinedMoonOres) {
		Optional<MoonOre> minedOre = moonOreRepository.findById(String.valueOf(minedOreTypeId));
		if (!minedOre.isPresent()) {
			LOG.error("Did not find moon ore with id {}", minedOreTypeId);
		} else {
			MoonOre ore = minedOre.get();
			List<Pair<String, Integer>> refinedPairs = MoonOreReprocessConstants.reprocessConstants.get(ore.getName());
			if (minedMoonOre.containsKey(minedOreTypeId)) {
				MoonOre moonOre = minedMoonOre.get(minedOreTypeId);
				moonOre.setMinedAmount(moonOre.getMinedAmount() + ore.getMinedAmount());
			} else {
				minedMoonOre.putIfAbsent(minedOreTypeId, ore);
			}
		}
	}

}
