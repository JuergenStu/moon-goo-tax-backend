package valkyrie.moon.goo.tax;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import net.troja.eve.esi.ApiException;
import valkyrie.moon.goo.tax.auth.Auth;
import valkyrie.moon.goo.tax.config.ConfigProperties;

@SpringBootApplication
@EnableScheduling
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
