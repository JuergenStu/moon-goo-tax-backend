package valkyrie.moon.goo.tax.api;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import valkyrie.moon.goo.tax.corp.wallet.TransactionLog;

@Document
public class CharacterView {

	@Id
	private Integer id;

	private String name;
	private String corpName;
	private Long debt;
	private List<MinedOre> minedOre;
	private List<TransactionLog> transactionLogs;

	public CharacterView() {
	}

	public CharacterView(Integer id, String name, String corpName, Long debt, List<MinedOre> minedOre,
			List<TransactionLog> transactionLogs) {
		this.id = id;
		this.name = name;
		this.corpName = corpName;
		this.debt = debt;
		this.minedOre = minedOre;
		this.transactionLogs = transactionLogs;
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

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public Long getDebt() {
		return debt;
	}

	public void setDebt(Long debt) {
		this.debt = debt;
	}

	public List<MinedOre> getMinedOre() {
		return minedOre;
	}

	public void setMinedOre(List<MinedOre> minedOre) {
		this.minedOre = minedOre;
	}

	public List<TransactionLog> getTransactionLogs() {
		return transactionLogs;
	}

	public void setTransactionLogs(List<TransactionLog> transactionLogs) {
		this.transactionLogs = transactionLogs;
	}

	@Override
	public String toString() {
		return "CharacterView{" +
				"id=" + id +
				", name='" + name + '\'' +
				", corpName='" + corpName + '\'' +
				", debt=" + debt +
				", minedOre=" + minedOre +
				", transactionLogs=" + transactionLogs +
				'}';
	}
}
