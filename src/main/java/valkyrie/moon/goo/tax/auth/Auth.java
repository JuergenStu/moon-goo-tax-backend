package valkyrie.moon.goo.tax.auth;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;

import net.troja.eve.esi.ApiClient;
import net.troja.eve.esi.ApiClientBuilder;
import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.CharacterApi;
import net.troja.eve.esi.auth.JWT;
import net.troja.eve.esi.auth.OAuth;
import net.troja.eve.esi.auth.SsoScopes;
import net.troja.eve.esi.model.CharacterResponse;
import valkyrie.moon.goo.tax.auth.dto.ClientCredentials;
import valkyrie.moon.goo.tax.auth.repo.ClientCredentialsRepository;
import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterRepository;
import valkyrie.moon.goo.tax.character.debt.Debt;
import valkyrie.moon.goo.tax.workers.DebtWorker;

@Component
public class Auth {

	private static final Logger LOG = LoggerFactory.getLogger(Auth.class);

	private static final String SSO_CLIENT_ID = "SSO_CLIENT_ID";

	private OAuth auth;
	private String args = "aef693bcc07d459689329e6a334c8a14";

	@Autowired
	private ClientCredentialsRepository repository;
	@Autowired
	private CharacterRepository characterRepository;
	@Autowired
	private DebtWorker debtWorker;
	@Autowired
	private EsiApi esiApi;

	private ApiClient client;

	public void authenticate(String state, String code) throws ApiException {
		auth.finishFlow(code, state);
		repository.save(new ClientCredentials("1", auth.getClientId(), auth.getRefreshToken()));

		// save char to db
		JWT jwt = auth.getJWT();
		if (jwt == null) {
			//Handle missing Auth
		}
		JWT.Payload payload = jwt.getPayload();
		Integer characterID = payload.getCharacterID();

		CharacterApi api = new CharacterApi(client);

		CharacterResponse character = api
				.getCharactersCharacterId(characterID, EsiApi.DATASOURCE, null);
		characterRepository
				.save(new Character(characterID, character.getName(), character.getCorporationId(),
						true, new Debt(characterID, 0L, 0L, new Date(943916400000L)), null, null));

		// now start processing...
		startProcessing();
	}

	private void startProcessing() {
		esiApi.prepareApi();
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(() -> {
			debtWorker.fetchMoonLedgerData();
		});
	}

	public String getAuthUrl() {

		final String state = "someSecret"; // doesn't matter for now

		if (!args.isEmpty()) {
			client = new ApiClientBuilder().clientID(args).build();
		} else {
			if (System.getenv().get(SSO_CLIENT_ID) != null) {
				client = new ApiClientBuilder().clientID(System.getenv().get(SSO_CLIENT_ID)).build();
			} else {
				System.err.println("ClientId missing");
				System.exit(-1);
				client = new ApiClientBuilder().build();
			}
		}
		auth = (OAuth) client.getAuthentication("evesso");

		//		esi-wallet.read_corporation_wallets.v1
		//		esi-industry.read_corporation_mining.v1

		final Set<String> scopes = ImmutableSet.of(
				SsoScopes.ESI_WALLET_READ_CORPORATION_WALLETS_V1,
				SsoScopes.ESI_INDUSTRY_READ_CORPORATION_MINING_V1);
		String redirectUri;
		if (System.getenv().get("SSO_CALLBACK_URL") != null) {
			redirectUri = System.getenv().get("SSO_CALLBACK_URL");
		} else {
			redirectUri = "http://localhost:8080/callback";
		}
		final String authorizationUri = auth.getAuthorizationUri(redirectUri, scopes, state);
		System.out.println("Authorization URL: " + authorizationUri);
		return authorizationUri;
	}
}
