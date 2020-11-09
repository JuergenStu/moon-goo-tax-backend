package valkyrie.moon.goo.tax.character;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CharacterRepository extends MongoRepository<Character, Integer> {

	Character findByIsLead(boolean isLead);
	Character findByName(String name);

}
