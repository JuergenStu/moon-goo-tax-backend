package valkyrie.moon.goo.tax.config;

import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ore")
public class ConfigProperties {

	private float refinementMultiplier;
	private float tax;
	private int division;
	private Date startDate;

	public float getRefinementMultiplier() {
		return refinementMultiplier;
	}

	public void setRefinementMultiplier(float refinementMultiplier) {
		this.refinementMultiplier = refinementMultiplier;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public int getDivision() {
		return division;
	}

	public void setDivision(int division) {
		this.division = division;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public String toString() {
		return "ConfigProperties{" + "refinementMultiplier=" + refinementMultiplier + ", tax=" + tax + ", division=" + division + ", startDate=" + startDate + '}';
	}
}
