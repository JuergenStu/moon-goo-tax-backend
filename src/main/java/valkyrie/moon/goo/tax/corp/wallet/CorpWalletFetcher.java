package valkyrie.moon.goo.tax.corp.wallet;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import net.troja.eve.esi.ApiClient;
import net.troja.eve.esi.ApiClientBuilder;
import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.SearchApi;
import net.troja.eve.esi.api.WalletApi;
import net.troja.eve.esi.auth.Authentication;
import net.troja.eve.esi.auth.OAuth;
import net.troja.eve.esi.model.CorporationWalletsResponse;
import net.troja.eve.esi.model.SearchResponse;
import valkyrie.moon.goo.tax.auth.EsiApi;

@Component
public class CorpWalletFetcher {

	@Autowired
	private EsiApi api;

	private final WalletApi walletApi = new WalletApi();
	private final SearchApi searchApi = new SearchApi();

	// corp will be chibi finance - always!
	private List<Integer> corpIds;

//	@PostConstruct
	public void fetchWalletData() {

		searchApi.setApiClient(api.getApi());
		// get corp ID:
		try {
			SearchResponse search = searchApi.getSearch(ImmutableList.of("corporation"), "Blind Berserker", EsiApi.LANGUAGE, EsiApi.DATASOURCE, "", EsiApi.LANGUAGE, null);
			corpIds = search.getCorporation();
		} catch (ApiException e) {
			e.printStackTrace();
		}


		walletApi.setApiClient(api.getApi());
		try {
			List<CorporationWalletsResponse> corporationsCorporationIdWallets = walletApi.getCorporationsCorporationIdWallets(corpIds.get(0), EsiApi.DATASOURCE, null, null);
			System.out.println(corporationsCorporationIdWallets);
		} catch (ApiException e) {
			e.printStackTrace();
		}

	}



}
