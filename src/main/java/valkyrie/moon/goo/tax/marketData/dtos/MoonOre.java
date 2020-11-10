package valkyrie.moon.goo.tax.marketData.dtos;

import java.util.Map;

public class MoonOre {

	public String id;

	public String name;
	public String visualName;
	public double multiplier;

	public long minedAmount;

	public Map<Integer, RefinedMoonOre> refinedMoonOres;

	public MoonOre() {
	}

	public MoonOre(String id, String name, String visualName, double multiplier, long minedAmount,
			Map<Integer, RefinedMoonOre> refinedMoonOres) {
		this.id = id;
		this.name = name;
		this.visualName = visualName;
		this.multiplier = multiplier;
		this.minedAmount = minedAmount;
		this.refinedMoonOres = refinedMoonOres;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	public long getMinedAmount() {
		return minedAmount;
	}

	public void setMinedAmount(long minedAmount) {
		this.minedAmount = minedAmount;
	}

	public Map<Integer, RefinedMoonOre> getRefinedMoonOres() {
		return refinedMoonOres;
	}

	public void setRefinedMoonOres(Map<Integer, RefinedMoonOre> refinedMoonOres) {
		this.refinedMoonOres = refinedMoonOres;
	}

	public String getVisualName() {
		return visualName;
	}

	public void setVisualName(String visualName) {
		this.visualName = visualName;
	}

	@Override
	public String toString() {
		return "MoonOre{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", visualName='"
				+ visualName + '\'' + ", multiplier=" + multiplier + ", minedAmount=" + minedAmount
				+ ", refinedMoonOres=" + refinedMoonOres + '}';
	}
}
