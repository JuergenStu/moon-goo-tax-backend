package valkyrie.moon.goo.tax.api;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MiningHistoryViewRepository extends MongoRepository<MiningHistoryView, String> {
	List<MiningHistoryView> findAllByCharacterName(String name);
}
