package valkyrie.moon.goo.tax;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.api.CharacterViewProcessor;
import valkyrie.moon.goo.tax.character.Character;
import valkyrie.moon.goo.tax.character.CharacterRepository;
import valkyrie.moon.goo.tax.corp.wallet.TransactionLog;
import valkyrie.moon.goo.tax.corp.wallet.TransactionLogRepository;

@Component
public class DebtManager {

	private Logger LOG = LoggerFactory.getLogger(DebtManager.class);

	@Autowired
	private CharacterRepository characterRepository;
	@Autowired
	private TransactionLogRepository transactionLogRepository;
	@Autowired
	private CharacterViewProcessor characterViewProcessor;

	public void increaseDebtIfNecessary() {
		List<Character> characters = characterRepository.findAll();
		for (Character character : characters) {
			if (character.getDept().getToPay() > 0) {
				TransactionLog transactionLog = transactionLogRepository.findFirstByCharacterNameOrderByTransactionDateDesc(character.getName());
				if (transactionLog == null) {
					continue;
				}
				increaseDebtIfNecessary(character, transactionLog);
				characterRepository.save(character);
			}
		}
		characterViewProcessor.prepareCharacterView();
	}

	public Character increaseDebtIfNecessary(Character character, TransactionLog transactionLog) {
		Date transactionDate = transactionLog.getTransactionDate();
		Date todayMinusTwoWeeks = DateUtils.convertToDateViaInstant(DateUtils.convertToLocalDateViaInstant(new Date()).minusWeeks(2));
		if (transactionDate.before(todayMinusTwoWeeks)) {
			// increase by 10%
			LOG.info("Increasing debt by 10% for {}.", character.getName());
			character.getDept().setToPay((character.getDept().getToPay() / 100) * 110);
		}
		return character;
	}
}
