package valkyrie.moon.goo.tax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.config.ConfigProperties;
import valkyrie.moon.goo.tax.config.ConfigRepository;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(StartupApplicationListener.class);

	@Autowired
	private ConfigRepository repository;

	@Autowired
	private ConfigProperties config;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (!repository.findAll().isEmpty()) {
			return;
		}

		repository.save(config);
	}
}
