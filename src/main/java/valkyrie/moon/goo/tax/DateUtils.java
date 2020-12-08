package valkyrie.moon.goo.tax;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateUtils {

	private DateUtils() {
	}

	public static Date convertToDateViaInstant(LocalDate dateToConvert) {
		return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date convertOffsetDateToDate(OffsetDateTime offsetDateTime) {
		return Date.from(offsetDateTime.toInstant());
	}
}
