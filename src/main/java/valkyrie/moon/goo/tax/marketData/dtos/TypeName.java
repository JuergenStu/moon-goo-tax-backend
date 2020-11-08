package valkyrie.moon.goo.tax.marketData.dtos;

import org.checkerframework.checker.interning.qual.InternedDistinct;
import org.springframework.data.annotation.Id;

public class TypeName {
	@Id
	public String id;
	public String category;
	public String name;

	public TypeName() {
	}

	public TypeName(String id, String category, String name) {
		this.id = id;
		this.category = category;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RefinedMoonOreName{" + "id='" + id + '\'' + ", category='" + category + '\'' + ", name='" + name + '\'' + '}';
	}
}
