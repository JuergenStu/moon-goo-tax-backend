package valkyrie.moon.goo.tax.character.debt;

import org.springframework.data.annotation.Id;

public class Debt {

	@Id
	public String id;

	public Integer characterId;
	public long hasPayed = 0;
	public long toPay = 0;

	public Debt() {
	}

	public Debt(String id, Integer characterId, long hasPayed, long toPay) {
		this.id = id;
		this.characterId = characterId;
		this.hasPayed = hasPayed;
		this.toPay = toPay;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCharacterId() {
		return characterId;
	}

	public void setCharacterId(Integer characterId) {
		this.characterId = characterId;
	}

	public long getHasPayed() {
		return hasPayed;
	}

	public void setHasPayed(long hasPayed) {
		this.hasPayed = hasPayed;
	}

	public long getToPay() {
		return toPay;
	}

	public void setToPay(long toPay) {
		this.toPay = toPay;
	}

	@Override
	public String toString() {
		return "Debt{" + "id='" + id + '\'' + ", characterId=" + characterId + ", hasPayed=" + hasPayed + ", toPay=" + toPay + '}';
	}
}
