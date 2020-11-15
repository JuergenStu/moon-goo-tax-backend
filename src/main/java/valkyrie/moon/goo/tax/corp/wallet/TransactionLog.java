package valkyrie.moon.goo.tax.corp.wallet;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class TransactionLog {

	@Id
	private Integer id;
	private String characterName;
	private String corp;
	private Double amount;
	private LocalDate transactionDate;

	public TransactionLog() {
	}

	public TransactionLog(String characterName, String corp, Double amount, LocalDate transactionDate) {
		this.characterName = characterName;
		this.corp = corp;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}

	public TransactionLog(Integer id, String characterName, String corp, Double amount, LocalDate transactionDate) {
		this.id = id;
		this.characterName = characterName;
		this.corp = corp;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
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
