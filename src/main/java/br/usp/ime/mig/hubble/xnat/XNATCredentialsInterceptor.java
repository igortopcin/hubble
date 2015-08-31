package br.usp.ime.mig.hubble.xnat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class XNATCredentialsInterceptor extends HandlerInterceptorAdapter {

	private final XNATCredentials credentials;

	@Autowired
	public XNATCredentialsInterceptor(XNATCredentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		credentials.setUser(request.getParameter("xnat_user"));
		credentials.setPassword(request.getParameter("xnat_password"));

		return true;
	}

	@Configuration
	public static class Configurer extends WebMvcConfigurerAdapter {

		@Autowired
		private XNATCredentials credentials;

		@Autowired
		private XNATCredentialsInterceptor interceptor;
		
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(interceptor);
		}

	}

}
