package valkyrie.moon.goo.tax.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterRepository;
import valkyrie.moon.goo.tax.marketData.dtos.RefinedMoonOre;
import valkyrie.moon.goo.tax.marketData.refinedMoonOre.RefinedMoonOreRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/info")
public class InformationController {

	@Autowired
	private CharacterViewRepository characterViewRepository;

	@Autowired
	private RefinedMoonOreRepository refinedMoonOreRepository;

	@Autowired
	private MiningHistoryViewRepository miningHistoryViewRepository;

	@Autowired
	private CharacterViewProcessor characterViewProcessor;

	@Autowired
	private CharacterRepository characterRepository;

	@Autowired
	private ApiHelper apiHelper;

	@RequestMapping("/characters")
	public List<CharacterView> getCharacters() {
		return characterViewRepository.findAll();
	}

	@RequestMapping("/characters/{id}")
	public Character getCharacterById(@PathVariable int id) {
		return apiHelper.findById(id);
	}

	@RequestMapping("/ore")
	public List<RefinedMoonOre> getOrePrices() {
		return refinedMoonOreRepository.findAll();
	}

	@RequestMapping("/prepareView")
	public void prepareView() {
		characterViewProcessor.prepareCharacterView();
	}

	@RequestMapping("/getMiningHistory")
	public List<MiningHistoryView> getMiningHistory(@RequestParam String name) {
		return miningHistoryViewRepository.findAllByCharacterName(name);
	}

	@RequestMapping("/holdingCorp")
	public String getHoldingCorp() {
		Character leadChar = characterRepository.findByIsLead(true);
		return leadChar.getCorpName();
	}
}
