package valkyrie.moon.goo.tax;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;

import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.debt.Debt;
import valkyrie.moon.goo.tax.corp.wallet.TransactionLog;

class DebtManagerTest {

	@Test
	void increaseDebtBy10Percent() {
		DebtManager debtManager = new DebtManager();

		Debt debt = new Debt(1, 0L, 100L, new Date(1609455600000L));

		Character character = new Character();
		character.setDept(debt);

		TransactionLog log = new TransactionLog("test", "test", 1.0, new Date(1609455600000L));
		Character result = debtManager.increaseDebtIfNecessary(character, log);

		assertThat(result.getDept().getToPay()).isEqualTo(110L);

	}

	@Test
	void doNotincreaseDebt() {
		DebtManager debtManager = new DebtManager();

		Debt debt = new Debt(1, 0L, 100L, DateUtils.convertToDateViaInstant(DateUtils.convertToLocalDateViaInstant(new Date()).minusWeeks(1)));

		Character character = new Character();
		character.setDept(debt);

		TransactionLog log = new TransactionLog("test", "test", 1.0, new Date());
		Character result = debtManager.increaseDebtIfNecessary(character, log);

		assertThat(result.getDept().getToPay()).isEqualTo(100L);
	}
}