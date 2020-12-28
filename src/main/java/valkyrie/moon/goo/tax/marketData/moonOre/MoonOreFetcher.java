package valkyrie.moon.goo.tax.marketData.moonOre;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;

import valkyrie.moon.goo.tax.marketData.EsiFetcher;
import valkyrie.moon.goo.tax.marketData.dtos.MoonOre;
import valkyrie.moon.goo.tax.marketData.dtos.RefinedMoonOre;
import valkyrie.moon.goo.tax.marketData.dtos.TypeName;
import valkyrie.moon.goo.tax.marketData.dtos.UniverseGroups;
import valkyrie.moon.goo.tax.marketData.refinedMoonOre.RefinedMoonOreFetcher;

@Component
public class MoonOreFetcher extends EsiFetcher {

	private static final Logger LOG = LoggerFactory.getLogger(MoonOreFetcher.class);

	private static final String GROUP_ID_URLS = "https://esi.evetech.net/latest/universe/groups/%s/?datasource=tranquility&language=en-us";
	private static final Set<String> MOON_ORE_GROUP_IDS = ImmutableSet.of("1920", "1923", "1922", "1884", "1921");
	private static final Set<String> DOUBLE_VALUE = ImmutableSet.of("Twinkling", "Shining", "Glowing", "Glistering", "Shimmering");
	private static final Set<String> A_BIT_MORE_VALUE = ImmutableSet.of("Copious", "Bountiful", "Replete", "Brimful", "Lavish");

	@Autowired
	private RefinedMoonOreFetcher refinedMoonOreFetcher;

	@Autowired
	private MoonOreRepository moonOreRepository;

	public void buildMoonOreDatabase() {
		List<RefinedMoonOre> refinedMoonOres = refinedMoonOreFetcher.buildRefinedMoonOreDatabase();
		Map<String, RefinedMoonOre> refinedMoonOreMap = new HashMap<>();

		for (RefinedMoonOre refinedMoonOre : refinedMoonOres) {
			refinedMoonOreMap.put(refinedMoonOre.name, refinedMoonOre);
		}

		for (String moonOreGroupId : MOON_ORE_GROUP_IDS) {
			try {
				UniverseGroups group = getUniverseGroups(
						new URL(String.format(GROUP_ID_URLS, moonOreGroupId)));

				// get clear names
				ObjectMapper mapper = new ObjectMapper();
				List<TypeName> typeNames = fetchNames(mapper.writeValueAsString(group.getTypes()),
						new URL(NAME_URLS), TypeName.class);

				if (typeNames == null) {
					LOG.warn("Typenames are {} - most likely eve API failed.", typeNames);
					return;
				}
				prepareAndSave(typeNames);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void prepareAndSave(List<TypeName> typeNames) {
		for (TypeName typeName : typeNames) {
			MoonOre ore = new MoonOre();
			ore.setId(typeName.getId());
			setMultiplier(typeName, ore);

			String[] splittedName = typeName.getName().split(" ");
			ore.setVisualName(typeName.getName());

			if (splittedName.length > 1) {
				ore.setName(splittedName[1]);
			} else {
				ore.setName(splittedName[0]);
			}

			// add prices to ore
			moonOreRepository.save(ore);
		}
	}

	private void setMultiplier(TypeName typeName, MoonOre ore) {
		for (String doubleValue : DOUBLE_VALUE) {
			if (typeName.getName().contains(doubleValue)) {
				ore.setMultiplier(2.0);
				return;
			}
		}
		for (String doubleValue : A_BIT_MORE_VALUE) {
			if (typeName.getName().contains(doubleValue)) {
				ore.setMultiplier(1.15);
				return;
			}
		}
		ore.setMultiplier(1.0);
	}

}
