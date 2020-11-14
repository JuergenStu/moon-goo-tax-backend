package valkyrie.moon.goo.tax.marketData.dtos;

import org.springframework.data.annotation.Id;

public class RefinedMoonOre {
	@Id
	public String id;

	public String name;
	public float price;
	public String date;

	public RefinedMoonOre() {
	}

	public RefinedMoonOre(String id, String name, float price, String date) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "RefinedMoonOre{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", price=" + price + ", date='" + date + '\'' + '}';
	}
}
