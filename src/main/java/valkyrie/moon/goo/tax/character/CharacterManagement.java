package valkyrie.moon.goo.tax.character;

import java.util.Date;
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
		if (characterApi.getApiClient() == null) {
			init();
		}
		Optional<Character> repoChar = characterRepository.findById(id);
		if (repoChar.isPresent()) {
			return repoChar.get();
		}

		// else fetch all information

		try {
			CharacterResponse characterResponse = characterApi
					.getCharactersCharacterId(id, EsiApi.DATASOURCE, null);
			Character character = new Character(id, characterResponse.getName(),
					characterResponse.getCorporationId(), false,
					new Debt(id, 0L, 0L, new Date(943916400000L)), new HashMap<>(),
					new HashMap<>());

			return character;
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Character getLeadChar() {
		Character lead = characterRepository.findByIsLead(true);
		if (lead == null) {
			esiApi.prepareApi();
		}
		return lead;
	}

	public void saveChar(Character character) {
		characterRepository.save(character);
	}

	public Character findByName(String name) {
		return characterRepository.findByName(name);
	}
}
