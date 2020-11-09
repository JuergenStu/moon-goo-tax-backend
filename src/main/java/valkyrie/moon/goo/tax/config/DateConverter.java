package valkyrie.moon.goo.tax.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class DateConverter implements Converter<String, Date> {

	private static final Logger LOG = LoggerFactory.getLogger(DateConverter.class);

	@Override
	public Date convert(String source) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			LOG.warn("Could not convert to date. Source: {}", source, e);
		}
		return null;
	}
}