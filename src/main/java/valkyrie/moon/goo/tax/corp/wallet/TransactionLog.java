package valkyrie.moon.goo.tax.corp.wallet;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class TransactionLog {

	@Id
	private String id;
	private String characterName;
	private String corp;
	private Double amount;
	private Date transactionDate;

	public TransactionLog() {
	}

	public TransactionLog(String characterName, String corp, Double amount, Date transactionDate) {
		this.characterName = characterName;
		this.corp = corp;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}

	public TransactionLog(String id, String characterName, String corp, Double amount, Date transactionDate) {
		this.id = id;
		this.characterName = characterName;
		this.corp = corp;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public String getCorp() {
		return corp;
	}

	public void setCorp(String corp) {
		this.corp = corp;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "TransactionLog{" +
				"id=" + id +
				", characterName='" + characterName + '\'' +
				", corp='" + corp + '\'' +
				", amount=" + amount +
				", transactionDate=" + transactionDate +
				'}';
	}
}
