package br.usp.ime.mig.hubble.auth;

import static br.usp.ime.mig.hubble.auth.ExternalCredential.ExternalCredentialSource.XNAT;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.usp.ime.mig.hubble.auth.ExternalCredential.ExternalCredentialSource;

import com.google.common.base.Function;

public class Users {

	public static Optional<User> getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
			return Optional.empty();
		} else {
			return Optional.ofNullable((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}
	}

	public static Optional<ExternalCredential> getExternalCredential(ExternalCredentialSource source) {
		Optional<User> userOptional = getLoggedUser();
		if (userOptional.isPresent()) {
			return userOptional.get().getExternalCredential(source);
		} else {
			return Optional.empty();
		}
	}

	public static class UserToProfileVOConverter implements Function<User, UserProfileVO> {
		@Override
		public UserProfileVO apply(User user) {
			String xnatUsername = null, xnatPassword = null;
			Optional<ExternalCredential> xnatCredentialOptional = user.getExternalCredential(XNAT);
			if (xnatCredentialOptional.isPresent()) {
				xnatUsername = xnatCredentialOptional.get().getUsername();
				xnatPassword = xnatCredentialOptional.get().getPassword();
			}
			return UserProfileVO.builder()
					.id(user.getId())
					.username(user.getUsername())
					.changingPassword(false)
					.galaxyApiKey(user.getGalaxyApiKey())
					.xnatUsername(xnatUsername)
					.xnatPassword(xnatPassword)
					.build();
		}
	}
}
