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

	// cron job: everyday at 14:00
	@Scheduled(cron = "0 0 14 * * *")
	public void fetchMoonLedgerData() {
		LOG.info("Fetching mining statistics...");
		miningFetcher.fetchMiningStatistics();
	}
}
