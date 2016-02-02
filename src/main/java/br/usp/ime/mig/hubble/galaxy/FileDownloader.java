package br.usp.ime.mig.hubble.galaxy;

import java.util.Optional;

import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;
import br.usp.ime.mig.hubble.xnat.XNATDownloadHelper;

public interface FileDownloader {
	Optional<String> download(Uploadable u, XNATDownloadHelper helper);
	UploadableType getUploadableType();
}
