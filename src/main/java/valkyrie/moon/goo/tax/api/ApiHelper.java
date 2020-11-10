package valkyrie.moon.goo.tax.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterRepository;

@Component
public class ApiHelper {

	@Autowired
	private CharacterRepository characterRepository;

	public List<Character> getFormattedCharacters() {
		return characterRepository.findAll();
	}

	public Character findById(int id) {
		Optional<Character> character = characterRepository.findById(id);
		return character.orElse(null);
	}

}
