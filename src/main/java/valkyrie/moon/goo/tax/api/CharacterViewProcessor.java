package valkyrie.moon.goo.tax.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterRepository;
import valkyrie.moon.goo.tax.marketData.dtos.MoonOre;

@Component
public class CharacterViewProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(CharacterView.class);

	@Autowired
	private CharacterViewRepository characterViewRepository;
	@Autowired
	private CharacterRepository characterRepository;

	public void prepareCharacterView() {

		LOG.info("Preparing character views for frontend...");

		List<Character> allCharacters = characterRepository.findAll();
		List<CharacterView> characterViews = new ArrayList<>();

		for (Character character : allCharacters) {
			setCharacterViewData(characterViews, character);
		}
		characterViewRepository.saveAll(characterViews);
	}

	private void setCharacterViewData(List<CharacterView> characterViews, Character character) {
		CharacterView characterView = new CharacterView();
		characterView.setId(character.getId());
		characterView.setName(character.getName());
		Map<String, Integer> minedOre = new HashMap<>();
		prepareMinedOre(character, minedOre);
		characterView.setMinedOre(minedOre);
		characterView.setDebt(character.getDept().getToPay());
		characterViews.add(characterView);
	}

	private void prepareMinedOre(Character character, Map<String, Integer> minedOre) {
		if (character.getMinedMoonOre() != null) {
			for (MoonOre entry : character.getMinedMoonOre().values()) {
				minedOre.put(entry.getVisualName(), (int) entry.getMinedAmount());
			}
		}
	}

}
