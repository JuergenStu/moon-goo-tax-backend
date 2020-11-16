package valkyrie.moon.goo.tax.character;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.CharacterApi;
import net.troja.eve.esi.api.CorporationApi;
import net.troja.eve.esi.model.CharacterResponse;
import net.troja.eve.esi.model.CorporationResponse;
import valkyrie.moon.goo.tax.auth.EsiApi;
import valkyrie.moon.goo.tax.character.debt.Debt;

@Component
public class CharacterManagement {

	private CharacterApi characterApi;
	private CorporationApi corporationApi;

	@Autowired
	private EsiApi esiApi;
	@Autowired
	private CharacterRepository characterRepository;

	public CharacterManagement() {

	}

	@PostConstruct
	public void init() {
		characterApi = new CharacterApi(esiApi.getApi());
		corporationApi = new CorporationApi(esiApi.getApi());
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
			CharacterResponse characterResponse = characterApi.getCharactersCharacterId(id, EsiApi.DATASOURCE, null);
			CorporationResponse corporationsCorporationId = corporationApi
					.getCorporationsCorporationId(characterResponse.getCorporationId(), EsiApi.DATASOURCE, null);

			return new Character(id, characterResponse.getName(), characterResponse.getCorporationId(),
					corporationsCorporationId.getName(), false, new Debt(id, 0L, 0L, new Date(943916400000L)), new HashMap<>(), new HashMap<>());
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

	public void saveAll(List<Character> characters) {
		characterRepository.saveAll(characters);
	}

	public List<Character> getAllCharacters() {
		return characterRepository.findAll();
	}
}
