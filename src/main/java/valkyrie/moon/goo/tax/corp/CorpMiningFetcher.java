package valkyrie.moon.goo.tax.corp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.IndustryApi;
import net.troja.eve.esi.model.CorporationMiningObserverResponse;
import net.troja.eve.esi.model.CorporationMiningObserversResponse;
import valkyrie.moon.goo.tax.DateUtils;
import valkyrie.moon.goo.tax.api.CharacterViewProcessor;
import valkyrie.moon.goo.tax.auth.EsiApi;
import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterManagement;
import valkyrie.moon.goo.tax.config.PersistedConfigProperties;
import valkyrie.moon.goo.tax.config.PersistedConfigPropertiesRepository;
import valkyrie.moon.goo.tax.marketData.dtos.MoonOre;
import valkyrie.moon.goo.tax.marketData.dtos.MoonOreReprocessConstants;
import valkyrie.moon.goo.tax.marketData.dtos.RefinedMoonOre;
import valkyrie.moon.goo.tax.marketData.moonOre.MoonOreRepository;
import valkyrie.moon.goo.tax.marketData.refinedMoonOre.RefinedMoonOreRepository;
import valkyrie.moon.goo.tax.statistics.StatisticsCalculator;

@Component
public class CorpMiningFetcher {

	private static final Logger LOG = LoggerFactory.getLogger(CorpMiningFetcher.class);

	@Autowired
	private EsiApi api;

	@Autowired
	private CharacterManagement characterManagement;

	@Autowired
	private PersistedConfigPropertiesRepository persistedConfigPropertiesRepository;

	@Autowired
	private MoonOreRepository moonOreRepository;
	@Autowired
	private RefinedMoonOreRepository refinedMoonOreRepository;
	@Autowired
	private UpdateTimeTrackerRepository updateTimeTrackerRepository;
	@Autowired
	private CharacterViewProcessor characterViewProcessor;
	@Autowired
	private StatisticsCalculator statisticsCalculator;

	private final IndustryApi industryApi = new IndustryApi();
	private UpdateTimeTracker updateTimeTracker;

	public void fetchMiningStatistics() {
		LocalDate today = checkDateRequirements();
		if (today == null)
			return;

		Character leadChar = characterManagement.getLeadChar();
		if (leadChar == null) {
			LOG.warn("No char for fetching data found - please auth char first.");
			return;
		}
		industryApi.setApiClient(api.getApi());
		List<RefinedMoonOre> refinedMoonOres = refinedMoonOreRepository.findAll();

		try {
			calculateAll(today, leadChar, refinedMoonOres);
		} catch (ApiException apiException) {
			LOG.warn("Had error while processing mining ledger.", apiException);
		}
		updateTimeTrackerRepository
				.save(new UpdateTimeTracker(1, updateTimeTracker.getFirstUpdate(), DateUtils.convertToLocalDateViaInstant(new Date())));
	}

	private void calculateAll(LocalDate today, Character leadChar, List<RefinedMoonOre> refinedMoonOres) throws ApiException {
		Map<Integer, Character> touchedChars = getMiningLog(leadChar.getCorpId(), refinedMoonOres, today);

		// get debt
		calculateDebt(refinedMoonOres, touchedChars);
		// calculate statistics
		statisticsCalculator.calculateStatistics();
		// and prepare character views
		characterViewProcessor.prepareCharacterView();
	}

	private LocalDate checkDateRequirements() {
		// first initialize dates and config
		Optional<UpdateTimeTracker> all = updateTimeTrackerRepository.findById(1);
		if (!all.isPresent()) {
			return null;
		}
		updateTimeTracker = all.get();
		LocalDate today = DateUtils.convertToLocalDateViaInstant(new Date());

		LocalDate lastUpdate = updateTimeTracker.getLastUpdate();
		if (!lastUpdate.isBefore(today)) {
			// nothing to update yet!
			LOG.info("last update: {} | current date: {} - nothing to do yet.", lastUpdate, today);
			return null;
		}
		return today;
	}

	private Map<Integer, Character> getMiningLog(Integer corpId, List<RefinedMoonOre> refinedMoonOres, LocalDate today) throws ApiException {
		LOG.info("Getting mining log from ESI.");
		List<CorporationMiningObserversResponse> corpMiningObservers = industryApi
				.getCorporationCorporationIdMiningObservers(corpId, EsiApi.DATASOURCE, null, null, null);
		Map<Integer, Character> touchedChars = new HashMap<>();

		LOG.info("Processing {} stations...", corpMiningObservers.size());
		for (CorporationMiningObserversResponse observersResponse : corpMiningObservers) {
			List<CorporationMiningObserverResponse> observerResponse = industryApi
					.getCorporationCorporationIdMiningObserversObserverId(corpId, observersResponse.getObserverId(), EsiApi.DATASOURCE, null, null,
							null);
			LOG.info("Processing {} mining log entries...", observerResponse.size());
			processObserverEntries(refinedMoonOres, touchedChars, observerResponse, today);
		}
		return touchedChars;
	}

	private void processObserverEntries(List<RefinedMoonOre> refinedMoonOres, Map<Integer, Character> touchedChars,
			List<CorporationMiningObserverResponse> observerResponse, LocalDate today) {
		for (CorporationMiningObserverResponse miner : observerResponse) {
			LocalDate lastUpdated = miner.getLastUpdated();
			LocalDate yesterday = today.minusDays(1);

			if (!lastUpdated.isEqual(yesterday)) {
				// we already calculated that or its not relevant
				// we only want ledger data from yesterday
				continue;
			}

			Character character = lookupCharacter(touchedChars, miner.getCharacterId());

			Map<Integer, MoonOre> minedMoonOre = character.getMinedMoonOre();
			Integer minedOreTypeId = miner.getTypeId();

			prepareMoonOre(minedMoonOre, minedOreTypeId, refinedMoonOres);

			long minedAmount = minedMoonOre.get(minedOreTypeId).getMinedAmount();// total mined for this type
			minedMoonOre.get(minedOreTypeId).setMinedAmount(minedAmount + miner.getQuantity());
			touchedChars.put(character.getId(), character);
		}
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
		LOG.info("Calculating mining debt...");
		PersistedConfigProperties config = persistedConfigPropertiesRepository.findById(1).get();
		float refinementMultiplier = config.getRefinementMultiplier();
		float tax = config.getTax();

		// build refinedMoonOre datastructure:
		Map<String, Float> refinedMoonOreMap = new HashMap<>();

		refinedMoonOres.forEach(ore -> {
			refinedMoonOreMap.put(ore.name, ore.price);
		});
		List<Character> processedChars = new ArrayList<>();
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
			processedChars.add(character);
		}
		characterManagement.saveAll(processedChars);
	}

	private float calculatePrice(float refinementMultiplier, float tax, Map<String, Float> refinedMoonOreMap, float currentDept, MoonOre ore) {
		//calculate value of 100 pieces
		List<Pair<String, Integer>> pairs = MoonOreReprocessConstants.reprocessConstants.get(ore.name);
		for (Pair<String, Integer> pair : pairs) {
			float price = refinedMoonOreMap.get(pair.getLeft());
			float finalPrice = (float) (price * pair.getRight() * refinementMultiplier * ore.getMultiplier() * tax);
			currentDept += finalPrice * ((int) ore.getMinedAmount() / 100);
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
