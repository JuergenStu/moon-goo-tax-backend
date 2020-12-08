package valkyrie.moon.goo.tax.corp.wallet;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.WalletApi;
import net.troja.eve.esi.model.CorporationWalletJournalResponse;
import valkyrie.moon.goo.tax.DateUtils;
import valkyrie.moon.goo.tax.api.CharacterViewProcessor;
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
	@Autowired
	private CharacterViewProcessor characterViewProcessor;
	@Autowired
	private TransactionLogRepository transactionLogRepository;

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
			List<CorporationWalletJournalResponse> corporationsCorporationIdWalletsDivisionJournal = walletApi
					.getCorporationsCorporationIdWalletsDivisionJournal(corporationId,
							persistedConfigPropertiesRepository.findAll().get(0).getDivision(), EsiApi.DATASOURCE, null, null, null);

			LOG.info("Found {} wallet  for division {}...", corporationsCorporationIdWalletsDivisionJournal.size(),
					persistedConfigPropertiesRepository.findAll().get(0).getDivision());

			// sort entries by date - also only get player donations

			List<CorporationWalletJournalResponse> sortedJournalResponses = sortEntriesByDate(
					corporationsCorporationIdWalletsDivisionJournal);

			sortedJournalResponses.forEach(entry -> {
				Character character = checkPreConditions(entry);
				if (character == null) {
					return;
				}
				LOG.info("Calculating debt for {}", character.getName());

				setDebt(entry, character);
				characterManagement.saveChar(character);
				// save transaction
				transactionLogRepository
						.save(new TransactionLog(character.getName(), character.getCorpName(), entry.getAmount(),
								Date.from(entry.getDate().toInstant())));
			});
			// also update character view
			characterViewProcessor.prepareCharacterView();
		} catch (ApiException e) {
			LOG.warn("WalletAPI not reachable or working...", e);
		}
	}

	private List<CorporationWalletJournalResponse> sortEntriesByDate(
			List<CorporationWalletJournalResponse> corporationsCorporationIdWalletsDivisionJournal) {
		List<CorporationWalletJournalResponse> sortedJournalResponses = new ArrayList<>();
		for (CorporationWalletJournalResponse corporationWalletJournalResponse : corporationsCorporationIdWalletsDivisionJournal) {
			if (corporationWalletJournalResponse.getRefType() != CorporationWalletJournalResponse.RefTypeEnum.PLAYER_DONATION) {
				continue;
			}
			sortedJournalResponses.add(corporationWalletJournalResponse);
		}

		sortedJournalResponses.sort((CorporationWalletJournalResponse r1, CorporationWalletJournalResponse r2) -> {
			return r1.getDate().compareTo(r2.getDate());
		});
		return sortedJournalResponses;
	}

	private Character checkPreConditions(CorporationWalletJournalResponse entry) {
		Integer firstPartyId = entry.getFirstPartyId();
		String reason = entry.getReason();
		LOG.debug("Found player donation: {}", entry);
		Character character = getCharacterFromDb(firstPartyId, reason);
		if (character == null) {
			LOG.warn("Did not find char in DB: {}", entry);
			return null;
		}
		LOG.debug("Character: {}", character);

		OffsetDateTime donationDate = entry.getDate();
		OffsetDateTime lastUpdate = character.getDept().getLastUpdate().toInstant().atOffset(ZoneOffset.UTC);
		if (lastUpdate.isAfter(donationDate)) {
			// no need to update!
			LOG.info("last Update for character {}: {}, now: {}", character.getName(), lastUpdate, donationDate);
			return null;
		}
		character.getDept().setLastUpdate(DateUtils.convertOffsetDateToDate(donationDate));
		return character;
	}

	private Character getCharacterFromDb(Integer characterId, String reason) {
		// valid character
		Character character = null;
		if (reason != null && !reason.isEmpty()) {
			character = characterManagement.findByName(reason.trim());
		}

		if (character == null) {
			character = characterManagement.findCharacter(characterId);
			if (character == null) {
				LOG.warn("Did not find character {} in DB - maybe wrong characterId", characterId);
				return null;
			}
		}
		return character;
	}

	private void setDebt(CorporationWalletJournalResponse entry, Character character) {
		Double amount = entry.getAmount();
		Debt dept = character.getDept();
		dept.setHasPayed((long) (dept.getHasPayed() + amount));
		dept.setToPay((long) (dept.getToPay() - amount));
		//		dept.setLastUpdate(new Date());
	}
}
