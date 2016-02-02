package br.usp.ime.mig.hubble.downloader;

import java.net.MalformedURLException;
import java.net.URL;

import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;

public interface UploadableURLResolver {
	URL apply(Uploadable uploadable) throws MalformedURLException;
	UploadableType getUploadableType();
}
