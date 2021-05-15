package valkyrie.moon.goo.tax.statistics;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import valkyrie.moon.goo.tax.corp.wallet.TransactionLog;
import valkyrie.moon.goo.tax.corp.wallet.TransactionLogRepository;
import valkyrie.moon.goo.tax.statistics.report.monthly.MonthlyCalc;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/statistics")
public class StatisticsController {

	@Autowired
	private StatisticsRepository statistics;

	@Autowired
	private TransactionLogRepository transactionLogRepository;

	@Autowired
	private MonthlyCalc monthlyCalc;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<Statistics> getAll() {
		return statistics.findAll();
	}

	@RequestMapping(value = "/transactionLog", method = RequestMethod.GET)
	public String transactionLog() {
		List<TransactionLog> logs = transactionLogRepository.findAll();
		StringBuilder builder = new StringBuilder();
		builder.append("<table>\n"
				+ "    <thead>\n"
				+ "        <tr>\n"
				+ "            <th>Name</th>\n"
				+ "            <th>Corp</th>\n"
				+ "            <th>Amount</th>\n"
				+ "            <th>TransactionDate</th>\n"
				+ "        </tr>\n"
				+ "    </thead>\n"
				+ "    <tbody>");
		logs.forEach(log -> {
			String row = String.format(
					" <tr>\n"
							+ "            <td>%s</td>\n"
							+ "            <td>%s</td>\n"
							+ "            <td>%s</td>\n"
							+ "            <td>%s</td>\n"
							+ "        </tr>", log.getCharacterName(), log.getCorp(), log.getAmount(), log.getTransactionDate());

			builder.append(row);
		});

		builder.append("    </tbody>\n"
				+ "</table>");
		return builder.toString();
	}

	@RequestMapping(value = "/monthly", method = RequestMethod.GET)
	public String listMonthlyReport() {

		Map<String, String> report = monthlyCalc.calculateMonthlyIncome();

		StringBuilder builder = new StringBuilder();
		builder.append("<table>\n"
				+ "    <thead>\n"
				+ "        <tr>\n"
				+ "            <th>Month</th>\n"
				+ "            <th></th>\n"
				+ "            <th>Amount</th>\n"
				+ "        </tr>\n"
				+ "    </thead>\n"
				+ "    <tbody>");
		report.forEach((key, value) -> {
			String row = String.format(
					" <tr>\n"
							+ "            <td>%s</td>\n"
							+ "            <td></td>\n"
							+ "            <td>%s</td>\n"
							+ "        </tr>", key, value);

			builder.append(row);
		});
		return builder.toString();
	}
}
