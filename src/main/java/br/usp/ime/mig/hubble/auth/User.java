package br.usp.ime.mig.hubble.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import br.usp.ime.mig.hubble.auth.ExternalCredential.ExternalCredentialSource;

@Entity(name = "hubbleuser")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_AUTHORITY = "USER";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Email(message = "{user.username.invalidemail}")
	private String username;

	@Size(min = 4, message = "{user.password.tooshort}")
	private String password;

	private String galaxyApiKey;

	private boolean enabled = true;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@MapKey(name = "source")
	private final Map<ExternalCredentialSource, ExternalCredential> externalCredentials = new HashMap<>();

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(DEFAULT_AUTHORITY);
	}

	public Optional<ExternalCredential> getExternalCredential(ExternalCredentialSource source) {
		return Optional.ofNullable(externalCredentials.get(source));
	}

	/**
	 * Puts the external credentials into the map of credentials.
	 * 
	 * @param source
	 *            the source associated with the credentials.
	 * @param externalCredential
	 *            the credential to be assigned to the given source
	 * @return the previous value associated with the source, or null if there
	 *         was no credentials for source.
	 */
	public Optional<ExternalCredential> putExternalCredential(ExternalCredentialSource source,
			ExternalCredential externalCredential) {
		externalCredential.setUser(this);
		externalCredential.setSource(source);
		return Optional.ofNullable(externalCredentials.put(source, externalCredential));
	}

	@Override
	public boolean isAccountNonExpired() {
		return enabled;
	}

	@Override
	public boolean isAccountNonLocked() {
		return enabled;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return enabled;
	}
}
