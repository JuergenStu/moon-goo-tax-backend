package valkyrie.moon.goo.tax.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.marketData.moonOre.MoonOreFetcher;

@Component
public class PriceFetchWorker {
	private static final Logger LOG = LoggerFactory.getLogger(PriceFetchWorker.class);

	@Autowired
	private MoonOreFetcher moonOreFetcher;

	@Scheduled(fixedRate = 14400000) //= 4h
	public void reportCurrentTime() {
		LOG.info("Fetching market data...");
		moonOreFetcher.buildMoonOreDatabase();
	}
}
