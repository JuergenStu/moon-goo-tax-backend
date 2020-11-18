package valkyrie.moon.goo.tax.api;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MiningHistoryView {
	@Id
	public String id;

	public String characterName;
	public String oreName;
	public Integer quantity;
	public Date minedDate;

	public MiningHistoryView(String characterName, String oreName, Integer quantity, Date minedDate) {
		this.characterName = characterName;
		this.oreName = oreName;
		this.quantity = quantity;
		this.minedDate = minedDate;
	}
}
