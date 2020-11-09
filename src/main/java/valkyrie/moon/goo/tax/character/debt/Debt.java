package valkyrie.moon.goo.tax.character.debt;

import java.util.Date;

public class Debt {

	public Integer characterId;
	public long hasPayed = 0L;
	public long toPay = 0L;

	public Date lastUpdate;

	public Debt(Integer characterId, long hasPayed, long toPay, Date lastUpdate) {
		this.characterId = characterId;
		this.hasPayed = hasPayed;
		this.toPay = toPay;
		this.lastUpdate = lastUpdate;
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

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "Debt{" + "characterId=" + characterId + ", hasPayed=" + hasPayed + ", toPay=" + toPay + ", lastUpdate=" + lastUpdate + '}';
	}
}
