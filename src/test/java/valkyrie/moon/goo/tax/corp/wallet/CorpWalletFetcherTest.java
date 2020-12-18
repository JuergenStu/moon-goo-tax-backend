package valkyrie.moon.goo.tax.corp.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;

import net.troja.eve.esi.model.CorporationWalletJournalResponse;
import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.debt.Debt;

class CorpWalletFetcherTest {

	@Test
	void setDebt() {

		CorpWalletFetcher corpWalletFetcher = new CorpWalletFetcher();
		Character character = new Character();
		Debt debt = new Debt(123, 10L, 50L, new Date());
		character.setDept(debt);
		CorporationWalletJournalResponse journalResponse = mock(CorporationWalletJournalResponse.class);
		when(journalResponse.getAmount()).thenReturn(50.0);

		corpWalletFetcher.setDebt(journalResponse, character);
		Debt newDebt = character.getDept();
		Debt expectedDebt = new Debt(123, 60L, 0L, debt.getLastUpdate());
		assertThat(newDebt).isEqualTo(expectedDebt);

	}
}