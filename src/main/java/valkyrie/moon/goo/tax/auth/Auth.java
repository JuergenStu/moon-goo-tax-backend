package valkyrie.moon.goo.tax.auth;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;

import net.troja.eve.esi.ApiClient;
import net.troja.eve.esi.ApiClientBuilder;
import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.auth.OAuth;
import net.troja.eve.esi.auth.SsoScopes;
import valkyrie.moon.goo.tax.auth.dto.ClientCredentials;
import valkyrie.moon.goo.tax.auth.repo.ClientCredentialsRepository;

@Component
public class Auth {

	private static final String SSO_CLIENT_ID = "SSO_CLIENT_ID";

		private String state = "i8q3GA6odmNadKoQlzHWVqhhhftTcxjyvzBJzl1f";
		private OAuth auth;
		private String args = "aef693bcc07d459689329e6a334c8a14";


//	private String state = ""; // secret
//	private OAuth auth;
//	private String args = ""; // client id

	@Autowired
	private ClientCredentialsRepository repository;

	public void authenticate(String state, String code) throws ApiException {
		auth.finishFlow(code, state);
		System.out.println("Refresh Token: " + auth.getRefreshToken());
		repository.save(new ClientCredentials("1", auth.getClientId(), code));
	}

	public String getAuthUrl() {

		final ApiClient client;

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
