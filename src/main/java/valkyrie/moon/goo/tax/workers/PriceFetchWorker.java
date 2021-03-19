package valkyrie.moon.goo.tax.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.corp.wallet.CorpWalletFetcher;
import valkyrie.moon.goo.tax.marketData.moonOre.MoonOreFetcher;

@Component
public class PriceFetchWorker {
	private static final Logger LOG = LoggerFactory.getLogger(PriceFetchWorker.class);

	@Autowired
	private MoonOreFetcher moonOreFetcher;
	@Autowired
	private CorpWalletFetcher corpWalletFetcher;

	@Scheduled(fixedRate = 144_000_00) //= 4h
	public void fetchMoonOreData() {
		LOG.info("Fetching market data...");
		moonOreFetcher.buildMoonOreDatabase();
	}

	@Scheduled(fixedRate = 3_600_000) //= 1h
	public void fetchWalletData() {
		LOG.info("Fetching wallet data...");
		corpWalletFetcher.fetchWalletData();
	}
}
