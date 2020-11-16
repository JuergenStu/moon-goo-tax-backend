package valkyrie.moon.goo.tax;

import java.time.LocalDate;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.config.ConfigProperties;
import valkyrie.moon.goo.tax.config.PersistedConfigProperties;
import valkyrie.moon.goo.tax.config.PersistedConfigPropertiesRepository;
import valkyrie.moon.goo.tax.corp.UpdateTimeTracker;
import valkyrie.moon.goo.tax.corp.UpdateTimeTrackerRepository;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(StartupApplicationListener.class);

	@Autowired
	private UpdateTimeTrackerRepository updateTimeTrackerRepository;

	@Autowired
	private PersistedConfigPropertiesRepository repository;

	@Autowired
	private ConfigProperties config;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (!repository.findAll().isEmpty()) {
			return;
		}

		LocalDate startDate = config.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		updateTimeTrackerRepository.save(new UpdateTimeTracker(1, startDate, startDate, false));
		PersistedConfigProperties persistedConfig = new PersistedConfigProperties();
		persistedConfig.setDivision(config.getDivision());
		persistedConfig.setRefinementMultiplier(config.getRefinementMultiplier());
		persistedConfig.setTax(config.getTax());
		persistedConfig.setStartDate(config.getStartDate());

		repository.save(persistedConfig);
	}
}
