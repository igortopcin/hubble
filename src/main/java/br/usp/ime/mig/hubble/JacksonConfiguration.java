package br.usp.ime.mig.hubble;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

@Configuration
public class JacksonConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(JacksonConfiguration.class);

	@Bean
	public JSR310Module jsr310Module() {
		logger.info("Using JSR310Module to serialize/deserialize JDK8 date-time objects");
		return new JSR310Module();
	}

}
