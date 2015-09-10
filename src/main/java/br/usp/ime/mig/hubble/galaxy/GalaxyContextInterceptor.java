package br.usp.ime.mig.hubble.galaxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class GalaxyContextInterceptor extends HandlerInterceptorAdapter {

	private final GalaxyContext galaxyContext;

	@Autowired
	public GalaxyContextInterceptor(GalaxyContext galaxyContext) {
		this.galaxyContext = galaxyContext;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String galaxyUrl = request.getParameter("GALAXY_URL");

		if (galaxyUrl != null) {
			galaxyContext.setReturnUrl(galaxyUrl);
		}

		return true;
	}

	@Configuration
	public static class Configurer extends WebMvcConfigurerAdapter {

		@Autowired
		private GalaxyContextInterceptor interceptor;
		
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(interceptor);
		}

	}

}
