package br.usp.ime.mig.hubble.auth;

import static br.usp.ime.mig.hubble.auth.ExternalCredential.ExternalCredentialSource.XNAT;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Strings;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	private static final int MIN_PASSWORD_LENGTH = 4;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@ModelAttribute("user")
	public UserProfileVO getUser() {
		Optional<User> userOptional = Users.getLoggedUser();
		if (userOptional.isPresent()) {
			return new Users.UserToProfileVOConverter().apply(userOptional.get());
		} else {
			return new UserProfileVO();
		}
	}

	@RequestMapping
	public String signUpForm() {
		return "profile/index";
	}

	@RequestMapping(method = RequestMethod.POST)
	@Transactional
	public String editProfile(@ModelAttribute("user") @Valid UserProfileVO userProfile, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			return "profile/index";
		}

		User user = userRepository.findOne(Users.getLoggedUser().get().getId());
		if (userProfile.isChangingPassword()) {
			if (!passwordEncoder.matches(userProfile.getCurrentPassword(), user.getPassword())) {
				validationResult.rejectValue("currentPassword", "user.password.nomatch");
				return "profile/index";
			}
			if (Strings.isNullOrEmpty(userProfile.getNewPassword())
					|| userProfile.getNewPassword().length() < MIN_PASSWORD_LENGTH) {
				validationResult.rejectValue("newPassword", "user.password.tooshort",
						new Object[] { MIN_PASSWORD_LENGTH }, "Not valid");
				return "profile/index";
			}
		}

		user.setId(getUser().getId());
		user.setGalaxyApiKey(userProfile.getGalaxyApiKey());
		if (userProfile.isChangingPassword()) {
			user.setPassword(passwordEncoder.encode(userProfile.getNewPassword()));
		}
		Optional<ExternalCredential> externalCredentialOptional = user.getExternalCredential(XNAT);
		if (externalCredentialOptional.isPresent()) {
			externalCredentialOptional.get().setUsername(userProfile.getXnatUsername());
			externalCredentialOptional.get().setPassword(userProfile.getXnatPassword());
		} else {
			user.putExternalCredential(XNAT, new ExternalCredential(
					userProfile.getXnatUsername(),
					userProfile.getXnatPassword()));
		}

		userRepository.save(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
				user.getAuthorities());
		SecurityContextHolder.clearContext();
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "redirect:profile?success";
	}
}
