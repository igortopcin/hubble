package br.usp.ime.mig.hubble;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@Configuration
public class ThymeleafConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(ThymeleafConfiguration.class);

	@Bean
	public Java8TimeDialect java8TimeDialect() {
		logger.info("Using Java8TimeDialect for Thymeleaf");
		return new Java8TimeDialect();
	}

}
