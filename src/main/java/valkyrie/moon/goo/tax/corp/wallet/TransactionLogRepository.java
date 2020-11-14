package valkyrie.moon.goo.tax.corp.wallet;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionLogRepository extends MongoRepository<TransactionLog, Integer> {
}
