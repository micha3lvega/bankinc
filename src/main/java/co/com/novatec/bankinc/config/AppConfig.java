package co.com.novatec.bankinc.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.datafaker.Faker;

@Configuration
public class AppConfig {

	@Bean
	Faker faker() {
		return new Faker(Locale.forLanguageTag("es-CO"));
	}

}
