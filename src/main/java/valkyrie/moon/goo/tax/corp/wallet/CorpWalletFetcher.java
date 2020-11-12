package valkyrie.moon.goo.tax.corp.wallet;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.WalletApi;
import net.troja.eve.esi.model.CorporationWalletJournalResponse;
import valkyrie.moon.goo.tax.auth.EsiApi;
import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterManagement;
import valkyrie.moon.goo.tax.character.debt.Debt;
import valkyrie.moon.goo.tax.config.PersistedConfigPropertiesRepository;

@Component
public class CorpWalletFetcher {

	Logger LOG = LoggerFactory.getLogger(CorpWalletFetcher.class);

	@Autowired
	private EsiApi api;
	@Autowired
	private CharacterManagement characterManagement;
	@Autowired
	private PersistedConfigPropertiesRepository persistedConfigPropertiesRepository;

	private final WalletApi walletApi = new WalletApi();

	public void fetchWalletData() {

		Character leadCharacter = characterManagement.getLeadChar();
		if (leadCharacter == null) {
			LOG.warn("Authorize first...");
			return;
		}
		Integer corporationId = leadCharacter.getCorpId();

		walletApi.setApiClient(api.getApi());
		try {

			List<CorporationWalletJournalResponse> corporationsCorporationIdWalletsDivisionJournal = walletApi.getCorporationsCorporationIdWalletsDivisionJournal(corporationId, persistedConfigPropertiesRepository.findAll().get(0).getDivision(), EsiApi.DATASOURCE, null, null, null);

			corporationsCorporationIdWalletsDivisionJournal.forEach(entry -> {
				String reason = entry.getReason().trim();
				if (reason.isEmpty()) {
					return;
				}

				Character character = getCharacterFromDb(reason);
				if (character == null) {
					return;
				}
				OffsetDateTime date = entry.getDate();
				OffsetDateTime lastUpdate = character.getDept().getLastUpdate().toInstant().atOffset(ZoneOffset.UTC);
				if (lastUpdate.isAfter(date)) {
					// no need to update!
					return;
				}

				setDebt(entry, character);

				characterManagement.saveChar(character);

			});

		} catch (ApiException e) {
			LOG.warn("WalletAPI not reachable or working...", e);
		}
	}

	private Character getCharacterFromDb(String reason) {
		// valid character
		Character character = characterManagement.findByName(reason);
		if (character == null) {
			LOG.warn("Did not find character {} in DB - maybe wrong reason", reason);
			return null;
		}
		return character;
	}

	private void setDebt(CorporationWalletJournalResponse entry, Character character) {
		Double amount = entry.getAmount();
		Debt dept = character.getDept();
		dept.setHasPayed((long) (dept.getHasPayed() + amount));
		dept.setToPay((long) (dept.getToPay() - amount));
		dept.setLastUpdate(new Date());
	}
}
