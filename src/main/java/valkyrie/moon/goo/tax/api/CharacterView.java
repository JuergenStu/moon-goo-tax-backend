package valkyrie.moon.goo.tax.api;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CharacterView {

	@Id
	private Integer id;

	private String name;
	private String corpName;
	private Long debt;
	private Map<String, Integer> minedOre;

	public CharacterView() {
	}

	public CharacterView(Integer id, String name, String corpName, Long debt, Map<String, Integer> minedOre) {
		this.id = id;
		this.name = name;
		this.corpName = corpName;
		this.debt = debt;
		this.minedOre = minedOre;
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

	public Long getDebt() {
		return debt;
	}

	public void setDebt(Long debt) {
		this.debt = debt;
	}

	public Map<String, Integer> getMinedOre() {
		return minedOre;
	}

	public void setMinedOre(Map<String, Integer> minedOre) {
		this.minedOre = minedOre;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	@Override
	public String toString() {
		return "CharacterView{" + "id=" + id + ", name='" + name + '\'' + ", corpName='" + corpName + '\'' + ", debt=" + debt + ", minedOre="
				+ minedOre + '}';
	}
}
