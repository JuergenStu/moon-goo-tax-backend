package valkyrie.moon.goo.tax.api;

import org.springframework.data.annotation.Id;

public class MinedOre {
	@Id
	private Integer id;
	private String name;
	private long totalAmount;
	private int delta;

	public MinedOre() {
	}

	public MinedOre(String name, long totalAmount, int delta) {
		this.name = name;
		this.totalAmount = totalAmount;
		this.delta = delta;
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

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	@Override
	public String toString() {
		return "MinedOre{" +
				"id=" + id +
				", name='" + name + '\'' +
				", totalAmount=" + totalAmount +
				", delta=" + delta +
				'}';
	}
}
