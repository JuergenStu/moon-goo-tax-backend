package valkyrie.moon.goo.tax.statistics;

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
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterRepository;

@Component
public class StatisticsCalculator {

	private Logger LOG = LoggerFactory.getLogger(StatisticsCalculator.class);

	@Autowired
	private StatisticsRepository statisticsRepository;

	@Autowired
	private CharacterRepository characterRepository;

	public void calculateStatistics() {
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
