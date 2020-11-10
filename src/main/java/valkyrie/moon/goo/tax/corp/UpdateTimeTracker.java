package valkyrie.moon.goo.tax.corp;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UpdateTimeTracker {

	private LocalDate firstUpdate; // first update given from config
	private LocalDate lastUpdate; // dynamic - will change on every update

	public UpdateTimeTracker(LocalDate firstUpdate, LocalDate lastUpdate) {
		this.firstUpdate = firstUpdate;
		this.lastUpdate = lastUpdate;
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

	@Override
	public String toString() {
		return "UpdateTimeTracker{" + "firstUpdate=" + firstUpdate + ", lastUpdate=" + lastUpdate + '}';
	}
}
