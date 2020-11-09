package valkyrie.moon.goo.tax.statistics;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticsRepository extends MongoRepository<Statistics, Integer> {
	List<Statistics> findFirstByOrderByLastFetchDesc();
}
