package valkyrie.moon.goo.tax.character;

import java.util.HashMap;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.CharacterApi;
import net.troja.eve.esi.model.CharacterResponse;
import valkyrie.moon.goo.tax.auth.EsiApi;
import valkyrie.moon.goo.tax.character.debt.Debt;

@Component
public class CharacterManagement {

	private CharacterApi characterApi;

	@Autowired
	private EsiApi esiApi;
	@Autowired
	private CharacterRepository characterRepository;

	public CharacterManagement() {

	}

	@PostConstruct
	public void init() {
		characterApi = new CharacterApi(esiApi.getApi());
	}

	public Character findCharacter(Integer id) {
		Optional<Character> repoChar = characterRepository.findById(String.valueOf(id));
		if (repoChar.isPresent()) {
			return repoChar.get();
		}

		// else fetch all information

		try {
			CharacterResponse characterResponse = characterApi.getCharactersCharacterId(id, EsiApi.DATASOURCE, null);
			Character character = new Character(id, characterResponse.getName(),
					characterResponse.getCorporationId(), false, new Debt(),
					new HashMap<>(), new HashMap<>());

			return character;
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Character getLeadChar() {
		return characterRepository.findByIsLead(true);
	}

	public void saveChar(Character character) {
		characterRepository.save(character);
	}
}
