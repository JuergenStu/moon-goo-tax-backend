package valkyrie.moon.goo.tax.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ore")
public class ConfigProperties {

	private float refinementMultiplier;
	private float tax;

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
}
