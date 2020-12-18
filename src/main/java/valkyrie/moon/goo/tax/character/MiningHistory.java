package valkyrie.moon.goo.tax.character;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MiningHistory {

	@Id
	public String id;

	public int characterId;
	public long quantity;
	public long typeId;
	public Date minedDate;
	public long value;

	public MiningHistory(int characterId, long quantity, long typeId, Date minedDate, long value) {
		this.characterId = characterId;
		this.quantity = quantity;
		this.typeId = typeId;
		this.minedDate = minedDate;
		this.value = value;
	}

	public MiningHistory(String id, int characterId, long quantity, long typeId, Date minedDate, long value) {
		this.id = id;
		this.characterId = characterId;
		this.quantity = quantity;
		this.typeId = typeId;
		this.minedDate = minedDate;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCharacterId() {
		return characterId;
	}

	public void setCharacterId(int characterId) {
		this.characterId = characterId;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public Date getMinedDate() {
		return minedDate;
	}

	public void setMinedDate(Date minedDate) {
		this.minedDate = minedDate;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

}
