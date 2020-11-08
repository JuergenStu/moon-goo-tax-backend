package valkyrie.moon.goo.tax.character;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CharacterRepository extends MongoRepository<Character, String> {
	Character findByIsLead(boolean isLead);
}
