package br.usp.ime.mig.hubble.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class UserProfileVO {

	private Long id;

	@Email(message = "{user.username.invalidemail}")
	private String username;

	private String currentPassword;

	private boolean changingPassword;

	private String newPassword;

	@NotEmpty(message = "{user.galaxyApiKey.empty}")
	private String galaxyApiKey;

	@NotBlank(message = "{external.username.empty}")
	private String xnatUsername;

	@NotBlank(message = "{external.password.empty}")
	private String xnatPassword;
}
