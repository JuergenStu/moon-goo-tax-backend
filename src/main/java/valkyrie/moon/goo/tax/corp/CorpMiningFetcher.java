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
import net.troja.eve.esi.api.UniverseApi;
import net.troja.eve.esi.model.CorporationMiningObserverResponse;
import net.troja.eve.esi.model.CorporationMiningObserversResponse;
import net.troja.eve.esi.model.StructureResponse;
import valkyrie.moon.goo.tax.DateUtils;
import valkyrie.moon.goo.tax.api.CharacterViewProcessor;
import valkyrie.moon.goo.tax.api.MiningHistoryView;
import valkyrie.moon.goo.tax.api.MiningHistoryViewRepository;
import valkyrie.moon.goo.tax.auth.EsiApi;
import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterManagement;
import valkyrie.moon.goo.tax.character.MiningHistory;
import valkyrie.moon.goo.tax.character.MiningHistoryRepository;
import valkyrie.moon.goo.tax.config.PersistedConfigProperties;
import valkyrie.moon.goo.tax.config.PersistedConfigPropertiesRepository;
import valkyrie.moon.goo.tax.marketData.dtos.MoonOre;
import valkyrie.moon.goo.tax.marketData.dtos.MoonOreReprocessConstants;
import valkyrie.moon.goo.tax.marketData.dtos.RefinedMoonOre;
import valkyrie.moon.goo.tax.marketData.moonOre.MoonOreRepository;
import valkyrie.moon.goo.tax.marketData.refinedMoonOre.RefinedMoonOreRepository;
import valkyrie.moon.goo.tax.observer.ObserverStation;
import valkyrie.moon.goo.tax.observer.ObserverStationRepository;
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
	private MiningHistoryRepository miningHistoryRepository;
	@Autowired
	private MiningHistoryViewRepository miningHistoryViewRepository;
	private final UniverseApi universeApi = new UniverseApi();
	@Autowired
	private CharacterViewProcessor characterViewProcessor;
	@Autowired
	private StatisticsCalculator statisticsCalculator;

	private final IndustryApi industryApi = new IndustryApi();
	@Autowired
	private ObserverStationRepository observerStationRepository;
	private UpdateTimeTracker updateTimeTracker;
	private PersistedConfigProperties config;

	public void fetchMiningStatistics() {

		LOG.info("Fetching mining Statistics...");
		Optional<PersistedConfigProperties> configOptional = persistedConfigPropertiesRepository.findById(1);
		if (!configOptional.isPresent()) {
			LOG.warn("No persisted config found - restart helps here.");
			return;
		}

		config = configOptional.get();
		LocalDate today = checkDateRequirements();

		// used for debugging
		//				LocalDate today = DateUtils.convertToLocalDateViaInstant(new Date());

		if (today == null)
			return;

		Character leadChar = characterManagement.getLeadChar();
		if (leadChar == null) {
			LOG.warn("No char for fetching data found - please auth char first.");
			return;
		}
		industryApi.setApiClient(api.getApi());
		universeApi.setApiClient(api.getApi());
		List<RefinedMoonOre> refinedMoonOres = refinedMoonOreRepository.findAll();

		try {
			calculateAll(today, leadChar, refinedMoonOres);
		} catch (ApiException apiException) {
			LOG.warn("Had error while processing mining ledger.", apiException);
			return; // might brick the data :(
		}
		updateTimeTrackerRepository
				.save(new UpdateTimeTracker(1, updateTimeTracker.getFirstUpdate(), DateUtils.convertToLocalDateViaInstant(new Date()), true));
	}

	public void resetDelta() {
		// reset delta for all characters
		LOG.info("Resetting delta for all characters...");
		List<Character> allCharacters = characterManagement.getAllCharacters();
		allCharacters.forEach(this::resetDelta);
	}

	private void calculateAll(LocalDate today, Character leadChar, List<RefinedMoonOre> refinedMoonOres) throws ApiException {
		Map<Integer, Character> touchedChars = getMiningLog(leadChar.getCorpId(), refinedMoonOres, today);

		// get debt
		//		calculateDebt(refinedMoonOres, touchedChars);

		if (!touchedChars.isEmpty()) {
			characterManagement.saveAll(new ArrayList<>(touchedChars.values()));
		}
		// calculate statistics
		statisticsCalculator.calculateStatistics();
		// and prepare character views
		characterViewProcessor.prepareCharacterView();
		// and, of course, prepare mining history view

	}

	private LocalDate checkDateRequirements() {
		// first initialize dates and config
		Optional<UpdateTimeTracker> all = updateTimeTrackerRepository.findById(1);
		if (!all.isPresent()) {
			return null;
		}
		updateTimeTracker = all.get();
		if (updateTimeTracker.getUpdatedToday()) {
			LOG.info("Already updated today... skipping run...");
			return null;
		}
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

			// check if the observer is already in db - if not add it
			ObserverStation observerStation = fetchObserverName(observersResponse);

			LOG.info("Processing {} mining log entries...", observerResponse.size());
			processObserverEntries(refinedMoonOres, touchedChars, observerResponse, today, observerStation.getName());
		}
		return touchedChars;
	}

	private ObserverStation fetchObserverName(CorporationMiningObserversResponse observersResponse) throws ApiException {
		Optional<ObserverStation> observerId = observerStationRepository.findById(observersResponse.getObserverId());
		if (!observerId.isPresent()) {
			// fetch name
			StructureResponse universeStructuresStructureId = universeApi
					.getUniverseStructuresStructureId(observersResponse.getObserverId(), EsiApi.DATASOURCE, null, null);
			ObserverStation station = new ObserverStation(observersResponse.getObserverId(), universeStructuresStructureId.getName());
			observerStationRepository.save(station);
			return station;
		}
		return observerId.get();
	}

	private void processObserverEntries(List<RefinedMoonOre> refinedMoonOres, Map<Integer, Character> touchedChars,
			List<CorporationMiningObserverResponse> observerResponse, LocalDate today, String observerName) {
		for (CorporationMiningObserverResponse miner : observerResponse) {
			LocalDate lastUpdated = miner.getLastUpdated();
			LocalDate yesterday = today.minusDays(1);

			if (!lastUpdated.isEqual(yesterday) || miner.getQuantity() < 100) {
				// we already calculated that or its not relevant
				// we only want ledger data from yesterday and quantities above 99
				continue;
			}

			Character character = lookupCharacter(touchedChars, miner.getCharacterId());

			Map<Integer, MoonOre> minedMoonOre = character.getMinedMoonOre();

			// sanity check
			if (minedMoonOre == null) {
				minedMoonOre = new HashMap<>();
				character.setMinedMoonOre(minedMoonOre);
			}
			Integer minedOreTypeId = miner.getTypeId();

			MoonOre moonOre = prepareMoonOre(minedMoonOre, minedOreTypeId, refinedMoonOres);
			if (moonOre == null) {
				continue;
			}
			setDetails(touchedChars, miner, character, minedMoonOre, minedOreTypeId);

			Date lastUpdate = DateUtils.convertToDateViaInstant(miner.getLastUpdated());

			long toPay = character.getDept().getToPay();
			long value = calculatePrice(prepareRefinedMoonOreMap(refinedMoonOres), minedMoonOre.get(miner.getTypeId()));

			MiningHistory miningHistory = new MiningHistory(character.getId(), miner.getQuantity(), miner.getTypeId(),
					lastUpdate, value);

			character.getDept().setToPay(toPay + value);

			saveToDb(observerName, miner, character, minedMoonOre, lastUpdate, value, miningHistory);
		}
	}

	private void saveToDb(String observerName, CorporationMiningObserverResponse miner, Character character, Map<Integer, MoonOre> minedMoonOre,
			Date lastUpdate, long value, MiningHistory miningHistory) {
		miningHistoryRepository.save(miningHistory);
		miningHistoryViewRepository.save(new MiningHistoryView(character.getName(), minedMoonOre.get(miner.getTypeId()).getVisualName(),
				Math.toIntExact(miner.getQuantity()), lastUpdate, observerName, value));
	}

	private void setDetails(Map<Integer, Character> touchedChars, CorporationMiningObserverResponse miner, Character character,
			Map<Integer, MoonOre> minedMoonOre, Integer minedOreTypeId) {
		long minedAmount = minedMoonOre.get(minedOreTypeId).getMinedAmount();// total mined for this type
		minedMoonOre.get(minedOreTypeId).setMinedAmount(minedAmount + miner.getQuantity());
		minedMoonOre.get(minedOreTypeId).setDelta(Math.toIntExact(miner.getQuantity()));
		touchedChars.put(character.getId(), character);
	}

	private void resetDelta(Character character) {
		if (character.getMinedMoonOre() != null) {
			character.getMinedMoonOre().forEach((id, ore) -> {
				ore.setDelta(0);
			});
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

	private Map<String, Float> prepareRefinedMoonOreMap(List<RefinedMoonOre> refinedMoonOres) {
		Map<String, Float> refinedMoonOreMap = new HashMap<>();

		refinedMoonOres.forEach(ore -> {
			refinedMoonOreMap.put(ore.name, ore.price);
		});
		return refinedMoonOreMap;
	}

	private long calculatePrice(Map<String, Float> refinedMoonOreMap, MoonOre ore) {
		float refinementMultiplier = config.getRefinementMultiplier();
		float tax = config.getTax();
		long lastPrice = 0L;

		//calculate value of 100 pieces
		List<Pair<String, Integer>> pairs = MoonOreReprocessConstants.reprocessConstants.get(ore.name);
		if (pairs == null) {
			return lastPrice;
		}
		for (Pair<String, Integer> pair : pairs) {
			float price = refinedMoonOreMap.get(pair.getLeft());
			float finalPrice = (float) (price * pair.getRight() * refinementMultiplier * ore.getMultiplier() * tax);
			lastPrice += finalPrice * (int) (ore.getDelta() / 100);
		}
		return lastPrice;
	}

	private MoonOre prepareMoonOre(Map<Integer, MoonOre> minedMoonOre, Integer minedOreTypeId, List<RefinedMoonOre> refinedMoonOres) {
		Optional<MoonOre> minedOre = moonOreRepository.findById(String.valueOf(minedOreTypeId));
		if (!minedOre.isPresent()) {
			LOG.error("Did not find moon ore with id {}", minedOreTypeId);
			return null;
		} else {
			MoonOre ore = minedOre.get();
			List<Pair<String, Integer>> refinedPairs = MoonOreReprocessConstants.reprocessConstants.get(ore.getName());
			if (refinedPairs == null) {
				// its an ore which is not taxed
				return null;
			}
			if (minedMoonOre.containsKey(minedOreTypeId)) {
				MoonOre moonOre = minedMoonOre.get(minedOreTypeId);
				moonOre.setMinedAmount(moonOre.getMinedAmount() + ore.getMinedAmount());
			} else {
				minedMoonOre.putIfAbsent(minedOreTypeId, ore);
			}
			return ore;
		}
	}
}
