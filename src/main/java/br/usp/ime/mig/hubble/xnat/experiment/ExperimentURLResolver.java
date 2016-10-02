package br.usp.ime.mig.hubble.xnat.experiment;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

import br.usp.ime.mig.hubble.downloader.UploadableURLResolver;
import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;

@Component
public class ExperimentURLResolver implements UploadableURLResolver {
	private final String downloadExperimentUrl;
	private final UriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();

	@Autowired
	public ExperimentURLResolver(@Value("${xnat.url}") String xnatBaseUrl) {
		this.downloadExperimentUrl = xnatBaseUrl + "{ref}/scans/ALL/files?format=zip&file_format=DICOM&file_format=U";
	}

	@Override
	public URL apply(Uploadable uploadable) throws MalformedURLException {
		return uriTemplateHandler.expand(this.downloadExperimentUrl, uploadable.getRef()).toURL();
	}

	@Override
	public UploadableType getUploadableType() {
		return UploadableType.EXPERIMENT;
	}
}
