package br.usp.ime.mig.hubble.auth;

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
@RequestMapping("/signup")
public class SignUpController {

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping
	public String signUpForm(@ModelAttribute("newUser") User newUser) {
		return "home/signup";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String signUp(@ModelAttribute("newUser") @Valid User newUser, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			return "home/signup";
		}
		if (userRepository.findByUsername(newUser.getUsername()) != null) {
			validationResult.rejectValue("username", "user.username.taken");
			return "home/signup";
		}
		userRepository.save(newUser);
		Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, newUser.getPassword(),
				newUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "redirect:projects";
	}
}
