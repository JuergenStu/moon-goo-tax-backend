package valkyrie.moon.goo.tax.marketData.dtos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

public final class MoonOreReprocessConstants {

	public static final Map<String, List<Pair<String, Integer>>> reprocessConstants;

	static {
		Map<String, List<Pair<String, Integer>>> values = new HashMap<>();

		// those ores will not be taxed

		//		// ubiquitous ores
		//		values.put("Bitumens", ImmutableList.of(Pair.of("Hydrocarbons", 65)));
		//		values.put("Coesite", ImmutableList.of(Pair.of("Silicates", 65)));
		//		values.put("Sylvite", ImmutableList.of(Pair.of("Evaporite Deposits", 65)));
		//		values.put("Zeolites", ImmutableList.of(Pair.of("Atmospheric Gases", 65)));
		//
		//		// common ores
		//		values.put("Cobaltite", ImmutableList.of(Pair.of("Cobalt", 40)));
		//		values.put("Euxenite", ImmutableList.of(Pair.of("Scandium", 40)));
		//		values.put("Scheelite", ImmutableList.of(Pair.of("Tungsten", 40)));
		//		values.put("Titanite", ImmutableList.of(Pair.of("Titanium", 40)));

		// exceptional ores
		values.put("Loparite", ImmutableList.of(Pair.of("Hydrocarbons", 20),
				Pair.of("Scandium", 20),
				Pair.of("Platinum", 10),
				Pair.of("Promethium", 22)));
		values.put("Monazite", ImmutableList.of(Pair.of("Evaporite Deposits", 20),
				Pair.of("Tungsten", 20),
				Pair.of("Chromium", 10),
				Pair.of("Neodymium", 22)));
		values.put("Xenotime", ImmutableList.of(Pair.of("Atmospheric Gases", 20),
				Pair.of("Cobalt", 20),
				Pair.of("Vanadium", 10),
				Pair.of("Dysprosium", 22)));
		values.put("Ytterbite", ImmutableList.of(Pair.of("Silicates", 20),
				Pair.of("Titanium", 20),
				Pair.of("Cadmium", 10),
				Pair.of("Thulium", 22)));

		// rare ores
		values.put("Carnotite", ImmutableList.of(Pair.of("Atmospheric Gases", 15),
				Pair.of("Cobalt", 10),
				Pair.of("Technetium", 50)));
		values.put("Cinnabar", ImmutableList.of(Pair.of("Evaporite Deposits", 15),
				Pair.of("Tungsten", 10),
				Pair.of("Mercury", 50)));
		values.put("Pollucite", ImmutableList.of(Pair.of("Hydrocarbons", 15),
				Pair.of("Scandium", 10),
				Pair.of("Caesium", 50)));
		values.put("Zircon", ImmutableList.of(Pair.of("Silicates", 15),
				Pair.of("Titanium", 10),
				Pair.of("Hafnium", 50)));

		// Uncommon ores
		values.put("Chromite", ImmutableList.of(Pair.of("Hydrocarbons", 10),
				Pair.of("Chromium", 40)));
		values.put("Otavite", ImmutableList.of(Pair.of("Atmospheric Gases", 10),
				Pair.of("Cadmium", 40)));
		values.put("Sperrylite", ImmutableList.of(Pair.of("Evaporite Deposits", 10),
				Pair.of("Platinum", 40)));
		values.put("Vanadinite", ImmutableList.of(Pair.of("Silicates", 10),
				Pair.of("Vanadium", 40)));
		reprocessConstants = values;
	}


}
