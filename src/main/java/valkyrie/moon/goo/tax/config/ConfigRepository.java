package valkyrie.moon.goo.tax.config;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigRepository extends MongoRepository<ConfigProperties, Integer> {
}
