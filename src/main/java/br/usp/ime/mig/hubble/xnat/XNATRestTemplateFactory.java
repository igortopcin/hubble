package br.usp.ime.mig.hubble.xnat;

import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import br.usp.ime.mig.hubble.xnat.auth.PreemptiveBasicAuthHttpRequestFactory;
import br.usp.ime.mig.hubble.xnat.auth.XNATCredentials;

@Configuration
public class XNATRestTemplateFactory {

	private static final Logger logger = LoggerFactory.getLogger(XNATRestTemplateFactory.class);

	@Autowired
	private XNATCredentials xnatCredentials;

	@Value("${xnat.timeoutInMillis:1000}")
	private int timeoutInMillis;

	@Bean
	@XNAT
	@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public RestTemplate restTemplate(List<HttpMessageConverter<?>> messageConverters) {
		logger.info("Using message converters: {}", messageConverters);

		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

		restTemplate.setMessageConverters(messageConverters);
		return restTemplate;
	}

	private HttpClient httpClient() {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				xnatCredentials.getUser(),
				xnatCredentials.getPassword());

		credentialsProvider.setCredentials(AuthScope.ANY, credentials);

		return HttpClients.custom()
				.setDefaultCredentialsProvider(credentialsProvider)
				.build();
	}

	private HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
		PreemptiveBasicAuthHttpRequestFactory clientHttpRequestFactory = new PreemptiveBasicAuthHttpRequestFactory(
				httpClient());

		clientHttpRequestFactory.setReadTimeout(timeoutInMillis);
		clientHttpRequestFactory.setConnectTimeout(timeoutInMillis);
		return clientHttpRequestFactory;
	}

}
