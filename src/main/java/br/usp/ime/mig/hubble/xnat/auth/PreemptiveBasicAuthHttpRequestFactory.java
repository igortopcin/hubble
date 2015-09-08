package br.usp.ime.mig.hubble.xnat.auth;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class PreemptiveBasicAuthHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

	public PreemptiveBasicAuthHttpRequestFactory(HttpClient httpClient) {
		super(httpClient);
	}

	@Override
	protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());

		AuthCache authCache = new BasicAuthCache();

		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);

		HttpClientContext context = HttpClientContext.create();
		context.setAuthCache(authCache);

		return context;
	}

}
