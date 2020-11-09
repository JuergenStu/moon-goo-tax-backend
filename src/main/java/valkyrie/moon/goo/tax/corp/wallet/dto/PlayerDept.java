package valkyrie.moon.goo.tax.corp.wallet.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PlayerDept {

	@Id
	public Integer id;
	public String playerName;
	public Integer balance;
	public Integer openDept;

	public PlayerDept() {
	}

	public PlayerDept(Integer id, String playerName, Integer balance, Integer openDept) {
		this.id = id;
		this.playerName = playerName;
		this.balance = balance;
		this.openDept = openDept;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getOpenDept() {
		return openDept;
	}

	public void setOpenDept(Integer openDept) {
		this.openDept = openDept;
	}

	@Override
	public String toString() {
		return "PlayerDept{" + "id=" + id + ", playerName='" + playerName + '\'' + ", balance=" + balance + ", openDept=" + openDept + '}';
	}
}
