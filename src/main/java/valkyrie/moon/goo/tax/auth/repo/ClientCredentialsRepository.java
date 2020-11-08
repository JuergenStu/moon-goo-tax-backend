package valkyrie.moon.goo.tax.auth.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import valkyrie.moon.goo.tax.auth.dto.ClientCredentials;

public interface ClientCredentialsRepository extends MongoRepository<ClientCredentials, String> {

}
