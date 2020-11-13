package valkyrie.moon.goo.tax.character;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import valkyrie.moon.goo.tax.character.debt.Debt;
import valkyrie.moon.goo.tax.marketData.dtos.MoonOre;

@Document(collection = "chars")
public class Character {

	@Id
	public Integer id;

	public String name;
	public Integer corpId;
	public String corpName;

	public boolean isLead;

	public Debt dept;

	public Map<Long, Date> lastUpdateObserverMapping;
	public Map<Integer, MoonOre> minedMoonOre;

	// add mined information

	public Character() {
	}

	public Character(Integer id, String name, Integer corpId, String corpName, boolean isLead, Debt dept, Map<Long, Date> lastUpdateObserverMapping,
			Map<Integer, MoonOre> minedMoonOre) {
		this.id = id;
		this.name = name;
		this.corpId = corpId;
		this.corpName = corpName;
		this.isLead = isLead;
		this.dept = dept;
		this.lastUpdateObserverMapping = lastUpdateObserverMapping;
		this.minedMoonOre = minedMoonOre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCorpId() {
		return corpId;
	}

	public void setCorpId(Integer corpId) {
		this.corpId = corpId;
	}

	public boolean isLead() {
		return isLead;
	}

	public void setLead(boolean lead) {
		isLead = lead;
	}

	public Debt getDept() {
		return dept;
	}

	public void setDept(Debt dept) {
		this.dept = dept;
	}

	public Map<Long, Date> getLastUpdateObserverMapping() {
		return lastUpdateObserverMapping;
	}

	public void setLastUpdateObserverMapping(Map<Long, Date> lastUpdateObserverMapping) {
		this.lastUpdateObserverMapping = lastUpdateObserverMapping;
	}

	public Map<Integer, MoonOre> getMinedMoonOre() {
		return minedMoonOre;
	}

	public void setMinedMoonOre(Map<Integer, MoonOre> minedMoonOre) {
		this.minedMoonOre = minedMoonOre;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	@Override
	public String toString() {
		return "Character{" + "id=" + id + ", name='" + name + '\'' + ", corpId=" + corpId + ", isLead=" + isLead + ", dept=" + dept
				+ ", lastUpdateObserverMapping=" + lastUpdateObserverMapping + ", minedMoonOre=" + minedMoonOre + '}';
	}
}
