package valkyrie.moon.goo.tax.corp.wallet;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import net.troja.eve.esi.model.CorporationWalletJournalResponse;
import net.troja.eve.esi.model.CorporationWalletTransactionsResponse;
import net.troja.eve.esi.model.CorporationWalletsResponse;
import net.troja.eve.esi.model.SearchResponse;
import valkyrie.moon.goo.tax.auth.EsiApi;
import valkyrie.moon.goo.tax.corp.wallet.dto.PlayerDept;

@Component
public class CorpWalletFetcher {

	Logger log = LoggerFactory.getLogger(CorpWalletFetcher.class);

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
			Integer corporationId = corpIds.get(0);
			List<CorporationWalletsResponse> corporationsCorporationIdWallets = walletApi.getCorporationsCorporationIdWallets(corporationId, EsiApi.DATASOURCE, null, null);
			List<CorporationWalletJournalResponse> corporationsCorporationIdWalletsDivisionJournal = walletApi.getCorporationsCorporationIdWalletsDivisionJournal(corporationId, 7, EsiApi.DATASOURCE, null, 1, null);

			corporationsCorporationIdWalletsDivisionJournal.forEach(entry -> {
				String reason = entry.getReason();
				try {
					SearchResponse character = searchApi.getSearch(ImmutableList.of("character"), reason, EsiApi.LANGUAGE, EsiApi.DATASOURCE, "", EsiApi.LANGUAGE, true);
					List<Integer> characterIds = character.getCharacter();
					// valid character
					Double balance = entry.getBalance();

					// calculate stuff!

				} catch (ApiException e) {
					log.warn("Did not find player with name {}", reason, e);
				}

			});

		} catch (ApiException e) {
			e.printStackTrace();
		}

	}



}
