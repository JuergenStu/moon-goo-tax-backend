package valkyrie.moon.goo.tax.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.corp.CorpMiningFetcher;

@Component
public class DebtWorker {

	private Logger LOG = LoggerFactory.getLogger(DebtWorker.class);

	@Autowired
	private CorpMiningFetcher miningFetcher;

	@Scheduled(fixedRate = 14400000) //= 4h
	public void fetchMoonLedgerData() {
		LOG.info("Fetching mining statistics...");
		miningFetcher.fetchMiningStatistics();
	}
}
