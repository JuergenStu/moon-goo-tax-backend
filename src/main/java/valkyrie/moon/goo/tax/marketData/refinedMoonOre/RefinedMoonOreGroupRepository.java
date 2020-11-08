package valkyrie.moon.goo.tax.marketData.refinedMoonOre;


import org.springframework.data.mongodb.repository.MongoRepository;

import valkyrie.moon.goo.tax.marketData.dtos.UniverseGroups;

public interface RefinedMoonOreGroupRepository extends MongoRepository<UniverseGroups, String> {

	public UniverseGroups findByGroupId(String groupId);

}
