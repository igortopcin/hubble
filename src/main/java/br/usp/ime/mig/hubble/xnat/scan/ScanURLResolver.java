package br.usp.ime.mig.hubble.xnat.scan;

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
public class ScanURLResolver implements UploadableURLResolver {
	private final String downloadScanUrl;
	private final UriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();

	@Autowired
	public ScanURLResolver(@Value("${xnat.url}") String xnatBaseUrl) {
		this.downloadScanUrl = xnatBaseUrl + "{ref}/files?format=zip&file_format=DICOM";
	}

	@Override
	public URL apply(Uploadable uploadable) throws MalformedURLException {
		return uriTemplateHandler.expand(this.downloadScanUrl, uploadable.getRef()).toURL();
	}

	@Override
	public UploadableType getUploadableType() {
		return UploadableType.SCAN;
	}

}
