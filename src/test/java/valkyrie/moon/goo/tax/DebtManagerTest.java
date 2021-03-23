package valkyrie.moon.goo.tax;

class DebtManagerTest {

	//	@Test
	//	void increaseDebtBy10Percent() {
	//		DebtManager debtManager = new DebtManager();
	//
	//		Debt debt = new Debt(1, 0L, 100L, new Date(1609455600000L));
	//
	//		Character character = new Character();
	//		character.setDept(debt);
	//
	//		TransactionLog log = new TransactionLog("test", "test", 1.0, new Date(1609455600000L));
	//		Character result = debtManager.increaseDebtIfNecessary(character, log);
	//
	//		assertThat(result.getDept().getToPay()).isEqualTo(110L);
	//
	//	}
	//
	//	@Test
	//	void doNotincreaseDebt() {
	//		DebtManager debtManager = new DebtManager();
	//
	//		Debt debt = new Debt(1, 0L, 100L, DateUtils.convertToDateViaInstant(DateUtils.convertToLocalDateViaInstant(new Date()).minusWeeks(1)));
	//
	//		Character character = new Character();
	//		character.setDept(debt);
	//
	//		TransactionLog log = new TransactionLog("test", "test", 1.0, new Date());
	//		Character result = debtManager.increaseDebtIfNecessary(character, log);
	//
	//		assertThat(result.getDept().getToPay()).isEqualTo(100L);
	//	}
	//
	//	@Test
	//	void increaseDebtMultipleTimes() {
	//		DebtManager debtManager = new DebtManager();
	//
	//		Debt debt = new Debt(1, 0L, 100_000L, new Date(1609455600000L));
	//
	//		Character character = new Character();
	//		character.setDept(debt);
	//
	//		TransactionLog log = new TransactionLog("test", "test", 1.0, new Date(1609455600000L));
	//		Character result = debtManager.increaseDebtIfNecessary(character, log);
	//
	//		assertThat(result.getDept().getToPay()).isEqualTo(110_000L);
	//
	//		result = debtManager.increaseDebtIfNecessary(result, log);
	//		assertThat(result.getDept().getToPay()).isEqualTo(121000L);
	//
	//		result = debtManager.increaseDebtIfNecessary(result, log);
	//		assertThat(result.getDept().getToPay()).isEqualTo(133100L);
	//	}
}