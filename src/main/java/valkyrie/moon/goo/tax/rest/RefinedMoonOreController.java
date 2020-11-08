package valkyrie.moon.goo.tax.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import valkyrie.moon.goo.tax.config.ConfigProperties;
import valkyrie.moon.goo.tax.marketData.dtos.RefinedMoonOre;
import valkyrie.moon.goo.tax.marketData.refinedMoonOre.RefinedMoonOreRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/refined")
public class RefinedMoonOreController {

	@Autowired
	private RefinedMoonOreRepository refinedMoonOreRepository;

	@RequestMapping(value = "/getAll",
	method = RequestMethod.GET)
	public List<RefinedMoonOre> getAll() {
		return refinedMoonOreRepository.findAll();
	}
}
