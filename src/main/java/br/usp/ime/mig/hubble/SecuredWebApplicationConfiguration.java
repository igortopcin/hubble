package br.usp.ime.mig.hubble;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import br.usp.ime.mig.hubble.auth.ApplicationSecurity;
import br.usp.ime.mig.hubble.auth.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecuredWebApplicationConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("redirect:projects");
		registry.addViewController("/login").setViewName("home/login");
	}

	@Bean
	public ApplicationSecurity applicationSecurity() {
		return new ApplicationSecurity(userDetailsService(), passwordEncoder());
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new JpaUserDetailsService();
	}

	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
		messageBundle.setBasenames("classpath:messages/ValidationMessages", "messages/ValidationMessages");
		return messageBundle;
	}

	@Override
	@Bean(name = "validator")
	public Validator getValidator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
