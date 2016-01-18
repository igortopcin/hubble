package br.usp.ime.mig.hubble.cache;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {

	public static final String EXPERIMENTS_CACHE = "experiments";

	@Bean
	public GuavaCacheManager createCacheManager(@Value("${guava.cache.spec:}") String spec) {
		GuavaCacheManager cacheManager = new GuavaCacheManager();
		cacheManager.setCacheNames(Arrays.asList(EXPERIMENTS_CACHE));
		cacheManager.setCacheSpecification(spec);
		return cacheManager;
	}

}
