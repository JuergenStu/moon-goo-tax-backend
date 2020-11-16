package valkyrie.moon.goo.tax.corp;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UpdateTimeTracker {

	@Id
	private int id = 1;

	private LocalDate firstUpdate; // first update given from config
	private LocalDate lastUpdate; // dynamic - will change on every update
	private Boolean updatedToday;

	public UpdateTimeTracker(int id, LocalDate firstUpdate, LocalDate lastUpdate) {
		this.id = id;
		this.firstUpdate = firstUpdate;
		this.lastUpdate = lastUpdate;
	}

	@PersistenceConstructor
	public UpdateTimeTracker(int id, LocalDate firstUpdate, LocalDate lastUpdate, Boolean updatedToday) {
		this.id = id;
		this.firstUpdate = firstUpdate;
		this.lastUpdate = lastUpdate;
		this.updatedToday = updatedToday;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getFirstUpdate() {
		return firstUpdate;
	}

	public void setFirstUpdate(LocalDate firstUpdate) {
		this.firstUpdate = firstUpdate;
	}

	public LocalDate getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDate lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Boolean getUpdatedToday() {
		return updatedToday;
	}

	public void setUpdatedToday(Boolean updatedToday) {
		this.updatedToday = updatedToday;
	}

	@Override
	public String toString() {
		return "UpdateTimeTracker{" +
				"id=" + id +
				", firstUpdate=" + firstUpdate +
				", lastUpdate=" + lastUpdate +
				", updatedToday=" + updatedToday +
				'}';
	}
}
