package br.usp.ime.mig.hubble.galaxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GalaxyContext {

	private String returnUrl;

	private String userUUID = UUID.randomUUID().toString();

	private final List<Uploadable> selectedUploadables = new ArrayList<>();

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public List<Uploadable> getSelectedUploadables() {
		return selectedUploadables;
	}

	public void addUploadables(Uploadable... uploadables) {
		selectedUploadables.addAll(Arrays.asList(uploadables));
	}

	public long getSelectedUploadablesSize() {
		return selectedUploadables.size();
	}

	public String getUserUUID() {
		return userUUID;
	}

}
