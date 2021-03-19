package valkyrie.moon.goo.tax.corp.wallet;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionLogRepository extends MongoRepository<TransactionLog, String> {
	List<TransactionLog> findByCharacterName(String name);

	TransactionLog findFirstByCharacterNameOrderByTransactionDateDesc(String name);
}
