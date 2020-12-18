package valkyrie.moon.goo.tax.corp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import valkyrie.moon.goo.tax.marketData.dtos.RefinedMoonOre;

class CorpMiningFetcherTest {

	private List<RefinedMoonOre> refinedMoonOres;

	@BeforeEach
	void setUp() {
		refinedMoonOres = new ArrayList<>();
		refinedMoonOres.add(new RefinedMoonOre("1", "ore1", 1f, new Date()));
		refinedMoonOres.add(new RefinedMoonOre("2", "ore2", 2f, new Date()));

	}

	@Test
	void calculateDebtForCharacter() {

		CorpMiningFetcher corpMiningFetcher = new CorpMiningFetcher();

		Map<String, Float> refinedMoonOreMap = new HashMap<>();

		refinedMoonOres.forEach(ore -> {
			refinedMoonOreMap.put(ore.name, ore.price);
		});

		//		corpMiningFetcher.calculateDebtForCharacter(0.85f, 0.3f, refinedMoonOreMap, );

	}
}