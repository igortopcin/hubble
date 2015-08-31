package br.usp.ime.mig.hubble.xnat;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class XNATRestTemplateFactory {

	@Autowired
	private XNATCredentials xnatCredentials;

	@Value("${xnat.timeoutInMillis:1000}")
	private int timeoutInMillis;

	@Bean
	@XNAT
	@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

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
