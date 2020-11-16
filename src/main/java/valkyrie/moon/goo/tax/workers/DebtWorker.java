package valkyrie.moon.goo.tax.workers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.corp.CorpMiningFetcher;
import valkyrie.moon.goo.tax.corp.UpdateTimeTracker;
import valkyrie.moon.goo.tax.corp.UpdateTimeTrackerRepository;

@Component
public class DebtWorker {

	private Logger LOG = LoggerFactory.getLogger(DebtWorker.class);

	@Autowired
	private CorpMiningFetcher miningFetcher;
	@Autowired
	private UpdateTimeTrackerRepository updateTimeTrackerRepository;

	// cron job: everyday at 14:00
	@Scheduled(fixedRate = 3_600_000, initialDelay = 3_600_000)
	public void fetchMoonLedgerData() {
		LOG.info("Fetching mining statistics...");
		miningFetcher.fetchMiningStatistics();
	}

	// cron job: everyday at 06:00
	@Scheduled(cron = "0 0 6 * * *")
	public void resetUpdate() {
		LOG.info("Resetting todays update to false");
		Optional<UpdateTimeTracker> updateTimeTracker = updateTimeTrackerRepository.findById(1);
		if (updateTimeTracker.isPresent()) {
			updateTimeTracker.get().setUpdatedToday(false);
		}
	}
}
