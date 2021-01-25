package valkyrie.moon.goo.tax.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/configuration")
@RestController
public class ConfigurationController {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationController.class);

	@Autowired
	private PersistedConfigPropertiesRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

//	@RequestMapping("/")
	public String config() {
		PersistedConfigProperties configProperties = repository.findById(1).get();

		String html = "<form action=\"/configuration/save/\" method=\"get\" target=\"_blank\">\n"
				+ "    <label for=\"refinementMultiplier\">refinementMultiplier:</label>\n" + "    <input type=\"text\" id=\"refinementMultiplier\" "
				+ "name=\"refinementMultiplier\" value=\"%s\"><br><br>\n" + "    <label for=\"tax\">Tax:</label>\n"
				+ "    <input type=\"text\" id=\"tax\" name=\"tax\" value=\"%s\"><br><br>\n" + "    <label for=\"division\">Division:</label>\n"
				+ "    <input type=\"text\" id=\"division\" name=\"division\" " + "value=\"%s\"><br><br>\n"
				+ "    <input type=\"submit\" value=\"Submit\">\n" + "  </form>" + "<br><br><br><br><br><br><br>" + "<h1> !!!DANGER ZONE!!! </h1>"
				+ "<br> Drop all data: <a href=\"/configuration/dropAll/\">klick me </a>";

		return String.format(html, configProperties.getRefinementMultiplier(), configProperties.getTax(), configProperties.getDivision());
	}

	//	@RequestMapping("/save")
	public String changeSettings(@RequestParam("refinementMultiplier") float refinementMultiplier,
			@RequestParam("tax") float tax, @RequestParam("division") int division) {

		PersistedConfigProperties configProperties = repository.findById(1).get();

		configProperties.setRefinementMultiplier(refinementMultiplier);
		configProperties.setTax(tax);
		configProperties.setDivision(division);

		String msg = "Saving new config settings: " + configProperties.toString();
		LOG.info(msg);
		repository.save(configProperties);
		return msg;
	}

	//	@RequestMapping("/dropAll")
	public void dropAll() {
		LOG.warn("Dropping all data from db...");
		mongoTemplate.getDb().drop();
	}
}
