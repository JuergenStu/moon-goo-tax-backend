package valkyrie.moon.goo.tax.workers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterRepository;
import valkyrie.moon.goo.tax.statistics.Statistics;
import valkyrie.moon.goo.tax.statistics.StatisticsRepository;

@Component
public class StatisticsWorker {

	private Logger LOG = LoggerFactory.getLogger(StatisticsWorker.class);

	@Autowired
	private StatisticsRepository statisticsRepository;

	@Autowired
	private CharacterRepository characterRepository;

	@Scheduled(fixedRate = 14400000) //= 4h
	public void fetchMoonOreData() {
		LOG.info("Creating statistics...");

		Statistics statistics = new Statistics();
		statistics.setTotalCharacters(characterRepository.count());

		List<Character> characters = characterRepository.findAll();

		Map<String, Long> topDebtContributors = new HashMap<>();
		for (Character character : characters) {
			topDebtContributors.put(character.name, character.getDept().toPay);
		}

		statistics.setHighestDebtCharacters(MapUtil.sortByValue(topDebtContributors));
		statistics.setLastFetch(new Date());

		statisticsRepository.save(statistics);
	}

	public static class MapUtil {
		public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
			List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
			list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

			Map<K, V> result = new LinkedHashMap<>();
			for (Map.Entry<K, V> entry : list) {
				result.put(entry.getKey(), entry.getValue());
			}

			return result;
		}
	}
}
