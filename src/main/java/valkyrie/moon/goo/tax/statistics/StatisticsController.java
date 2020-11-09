package valkyrie.moon.goo.tax.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/statistics")
public class StatisticsController {

	@Autowired
	private StatisticsRepository statistics;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<Statistics> getAll() {
		return statistics.findAll();
	}

}
