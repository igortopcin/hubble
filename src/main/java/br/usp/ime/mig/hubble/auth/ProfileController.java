package br.usp.ime.mig.hubble.auth;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private UserRepository userRepository;

	@ModelAttribute("user")
	public User getUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@ModelAttribute("xnat")
	public ExternalCredential getXNATCredential() {
		User user = getUser();
		if (user.getExternalCredentials().isEmpty()) {
			return new ExternalCredential();
		} else {
			return user.getExternalCredentials().get(0);
		}
	}

	@RequestMapping
	public String signUpForm() {
		return "profile/index";
	}

	@RequestMapping(method = RequestMethod.POST)
	@Transactional
	public String signUp(@ModelAttribute("user") @Valid User user,
			@ModelAttribute("xnat") @Valid ExternalCredential xnat, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			return "profile/index";
		}
		user.setId(getUser().getId());
		xnat.setId(getXNATCredential().getId());
		user.getExternalCredentials().add(xnat);

		userRepository.save(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
				user.getAuthorities());
		SecurityContextHolder.clearContext();
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "redirect:profile?success";
	}
}
