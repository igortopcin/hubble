package br.usp.ime.mig.hubble.auth;

import static javax.persistence.EnumType.STRING;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ExternalCredential {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "{external.username.empty}")
	private String username;

	@NotBlank(message = "{external.password.empty}")
	private String password;

	@Enumerated(STRING)
	private ExternalCredentialSource source;

	@ManyToOne
	private User user;

	public ExternalCredential() {
	}

	public ExternalCredential(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public enum ExternalCredentialSource {
		XNAT
	}
}
