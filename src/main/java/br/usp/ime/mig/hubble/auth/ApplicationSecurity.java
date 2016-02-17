package br.usp.ime.mig.hubble.auth;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
	public static final String[] PERMIT_ALL_MATCHERS = {
			"/login",
			"/login/openid",
			"/signup",
			"/error",
			"/images/**",
			"/css/**",
			"/js/**",
			"**/favicon.ico",
			"/favicon.ico" };

	private final UserDetailsService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	public ApplicationSecurity(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers(PERMIT_ALL_MATCHERS).permitAll()
				.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
			.and()
				.logout()
				.permitAll()
			.and()
				.exceptionHandling()
				.accessDeniedPage("/login")
			.and()
				.userDetailsService(userDetailsService)
				.openidLogin()
					.loginPage("/login")
					.permitAll()
				.loginProcessingUrl("/login/openid")
				.permitAll()
					.attributeExchange("https://www.google.com/.*")
						.attribute("email")
						.type("http://axschema.org/contact/email")
						.required(true);
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationBuilder) throws Exception {
		authenticationBuilder
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder);
	}
}