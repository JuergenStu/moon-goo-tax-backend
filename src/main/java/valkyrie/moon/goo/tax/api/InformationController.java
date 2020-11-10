package valkyrie.moon.goo.tax.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import valkyrie.moon.goo.tax.character.Character;

@RestController
@RequestMapping("/info")
public class InformationController {

	@Autowired
	private ApiHelper apiHelper;

	@RequestMapping("/characters")
	public List<Character> getCharacters() {
		return apiHelper.getFormattedCharacters();
	}

	@RequestMapping("/characters/{id}")
	public Character getCharacterById(@PathVariable int id) {
		return apiHelper.findById(id);
	}

}
