package br.usp.ime.mig.hubble.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

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

	@NotEmpty(message = "{user.username.empty}")
	@Email(message = "{user.username.invalidemail}")
	private String username;

	@NotEmpty(message = "{user.password.empty}")
	@Size(min = 4, message = "{user.password.tooshort}")
	private String password;

	private String galaxyApiKey;

	private boolean enabled = true;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ExternalCredential> externalCredentials = new ArrayList<>();

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(DEFAULT_AUTHORITY);
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
