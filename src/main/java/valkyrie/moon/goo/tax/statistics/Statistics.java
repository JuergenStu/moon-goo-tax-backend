package valkyrie.moon.goo.tax.statistics;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Statistics {

	@Id
	Integer id = 1;

	private Date lastFetch;
	private long totalCharacters;
	private Map<String, Long> highestDebtCharacters;

	public Statistics() {
	}

	public Statistics(Integer id, Date lastFetch, long totalCharacters, Map<String, Long> highestDebtCharacters) {
		this.id = id;
		this.lastFetch = lastFetch;
		this.totalCharacters = totalCharacters;
		this.highestDebtCharacters = highestDebtCharacters;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getLastFetch() {
		return lastFetch;
	}

	public void setLastFetch(Date lastFetch) {
		this.lastFetch = lastFetch;
	}

	public long getTotalCharacters() {
		return totalCharacters;
	}

	public void setTotalCharacters(long totalCharacters) {
		this.totalCharacters = totalCharacters;
	}

	public Map<String, Long> getHighestDebtCharacters() {
		return highestDebtCharacters;
	}

	public void setHighestDebtCharacters(Map<String, Long> highestDebtCharacters) {
		this.highestDebtCharacters = highestDebtCharacters;
	}

	@Override
	public String toString() {
		return "Statistics{" + "lastFetch=" + lastFetch + ", totalCharacters=" + totalCharacters + ", highestDebtCharacters=" + highestDebtCharacters + '}';
	}
}
