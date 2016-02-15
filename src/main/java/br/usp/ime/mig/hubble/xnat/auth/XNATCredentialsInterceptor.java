package br.usp.ime.mig.hubble.xnat.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
@Slf4j
public class XNATCredentialsInterceptor extends HandlerInterceptorAdapter {

	private final XNATCredentials credentials;

	@Autowired
	public XNATCredentialsInterceptor(XNATCredentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String user = request.getParameter("xnat_user");
		String password = request.getParameter("xnat_password");

		if (user == null && credentials.getUser() == null && !request.getRequestURI().endsWith("login")) {
			log.error("Cannot find XNAT authentication parameters for user");
			if (assertThatAcceptHeaderIsTextHttp(request, response)
					|| assertThatResponseContentTypeIsTextHttp(response, handler)) {
				// response.sendRedirect("/login");
			} else {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
			}
			return true;
		} else if (user != null) {
			credentials.setUser(user);
			credentials.setPassword(password);
		}
		return true;
	}

	private boolean assertThatResponseContentTypeIsTextHttp(HttpServletResponse response, Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			RequestMapping requestMapping = method.getMethodAnnotation(RequestMapping.class);
			if (requestMapping == null || requestMapping.produces() == null) {
				return true;
			}
			for (String contentTypeValue : requestMapping.produces()) {
				MediaType mediaType = MediaType.valueOf(contentTypeValue);
				if (MediaType.TEXT_HTML.isCompatibleWith(mediaType)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean assertThatAcceptHeaderIsTextHttp(HttpServletRequest request, HttpServletResponse response) {
		String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
		return acceptHeader == null || acceptHeader.contains(MediaType.TEXT_HTML_VALUE);
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
