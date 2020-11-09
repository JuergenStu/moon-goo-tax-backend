package valkyrie.moon.goo.tax.auth;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.troja.eve.esi.ApiClient;
import net.troja.eve.esi.ApiClientBuilder;
import okhttp3.OkHttpClient;
import valkyrie.moon.goo.tax.auth.dto.ClientCredentials;
import valkyrie.moon.goo.tax.auth.repo.ClientCredentialsRepository;

@Component
public class EsiApi {

	private static OkHttpClient OkHttpClient;
	private ApiClient api;
	public static final String DATASOURCE = "tranquility";
	public static final String LANGUAGE = "en-us";

	public static String refreshToken;
	@Autowired
	private ClientCredentialsRepository repository;

	@PostConstruct
	public void prepareApi() {
		List<ClientCredentials> clients = repository.findAll();
		for (ClientCredentials client : clients) {
			api = new ApiClientBuilder().clientID(client.getApplicationId()).refreshToken(client.getRefreshToken()).okHttpClient(getHttpClient()).build();

			refreshToken = client.refreshToken;

			System.out.println("client.refreshtoken: " + refreshToken);
		}
	}

	public ApiClient getApi() {
		return api;
	}

	public static OkHttpClient getHttpClient() {
		if (OkHttpClient == null || OkHttpClient.interceptors().size() > 100 || OkHttpClient.networkInterceptors().size() > 100) {
			OkHttpClient = new OkHttpClient.Builder()
					.readTimeout(20, TimeUnit.SECONDS)
					.writeTimeout(20, TimeUnit.SECONDS)
					.connectTimeout(20, TimeUnit.SECONDS).build();
		}
		return OkHttpClient;
	}
}
