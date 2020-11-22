package valkyrie.moon.goo.tax.observer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ObserverStation {

	@Id
	private Long id;

	private String name;

	public ObserverStation(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ObserverStation{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
