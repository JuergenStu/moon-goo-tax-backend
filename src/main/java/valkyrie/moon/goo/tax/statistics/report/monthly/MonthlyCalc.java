package valkyrie.moon.goo.tax.statistics.report.monthly;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import valkyrie.moon.goo.tax.corp.wallet.TransactionLog;

@Component
public class MonthlyCalc {

	@Autowired
	private MongoTemplate mongoTemplate;

	public Map<String, String> calculateMonthlyIncome() {

		Query query = new Query();
		query.with(Sort.by(Sort.Direction.ASC, "transactionDate"));
		List<TransactionLog> transactionLogs = mongoTemplate.find(query, TransactionLog.class);

		Map<String, Double> doubleReport = new LinkedHashMap<>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM-yyyy");

		calculateMonthlyReport(transactionLogs, doubleReport, simpleDateFormat);
		return convertToFormattedReport(doubleReport);
	}

	private void calculateMonthlyReport(List<TransactionLog> transactionLogs, Map<String, Double> doubleReport, SimpleDateFormat simpleDateFormat) {
		transactionLogs.forEach(log -> {

			String s = simpleDateFormat.format(log.getTransactionDate());
			String monthAndYear = s.toUpperCase();

			double amount = doubleReport.containsKey(monthAndYear) ? doubleReport.get(monthAndYear) : 0;
			doubleReport.put(monthAndYear, amount + log.getAmount());
		});
	}

	private Map<String, String> convertToFormattedReport(Map<String, Double> doubleReport) {
		Map<String, String> formattedReport = new LinkedHashMap<>();
		doubleReport.forEach((key, value) -> {
			formattedReport.put(key, String.format("%,d", Math.round(value)).replace(',', '.'));
		});
		return formattedReport;
	}

}
