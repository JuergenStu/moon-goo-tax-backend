package valkyrie.moon.goo.tax.marketData.dtos;

import java.util.Set;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

// moonMatUrl = 'https://esi.evetech.net/latest/universe/groups/427/?datasource=tranquility&language=en-us'
public class UniverseGroups {

	@Id public String id;

	@JsonProperty("category_id")
	public int categoryId;
	@JsonProperty("group_id")
	public int groupId;
	public String name;
	public boolean published;
	public Set<Integer> types;

	public UniverseGroups() {
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public Set<Integer> getTypes() {
		return types;
	}

	public void setTypes(Set<Integer> types) {
		this.types = types;
	}

	public UniverseGroups(int categoryId, int groupId, String name, boolean published, Set<Integer> types) {
		this.categoryId = categoryId;
		this.groupId = groupId;
		this.name = name;
		this.published = published;
		this.types = types;
	}

	@Override
	public String toString() {
		return "RefinedMoonOreGroup{" + "id='" + id + '\'' + ", category_id=" + categoryId + ", group_id=" + groupId + ", name='" + name + '\'' + ", published=" + published + ", types=" + types + '}';
	}
}
