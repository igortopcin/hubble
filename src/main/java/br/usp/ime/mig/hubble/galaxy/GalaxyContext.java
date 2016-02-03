package br.usp.ime.mig.hubble.galaxy;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.GalaxyInstanceFactory;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GalaxyContext {

	@Getter @Setter
	private String galaxyURL;

	@Getter @Setter
	private String apiKey;

	@Getter
	private UploadStatus uploadStatus = new UploadStatus();

	public void addUploadables(Uploadable... uploadables) {
		getSelectedUploadables().addAll(Arrays.asList(uploadables));
	}

	public void removeUploadable(Uploadable uploadable) {
		getSelectedUploadables().remove(uploadable);
	}

	public long getSelectedUploadablesSize() {
		return getSelectedUploadables().size();
	}

	public List<Uploadable> getSelectedUploadables() {
		return uploadStatus.getUploadables();
	}

	public GalaxyInstance getGalaxyInstance() {
		return GalaxyInstanceFactory.get(galaxyURL, apiKey);
	}

}
