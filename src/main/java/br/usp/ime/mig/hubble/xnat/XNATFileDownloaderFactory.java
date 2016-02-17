package br.usp.ime.mig.hubble.xnat;

import static br.usp.ime.mig.hubble.auth.ExternalCredential.ExternalCredentialSource.XNAT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.usp.ime.mig.hubble.auth.Users;
import br.usp.ime.mig.hubble.downloader.FileDownloader;
import br.usp.ime.mig.hubble.downloader.FileDownloaderFactory;

@Component
public class XNATFileDownloaderFactory implements FileDownloaderFactory {
	private final String stagingDir;

	@Autowired
	public XNATFileDownloaderFactory(
			@Value("${staging.dir}") String stagingDir) {
		this.stagingDir = stagingDir;
	}

	@Override
	public FileDownloader create() {
		return new XNATFileDownloader(stagingDir, Users.getExternalCredential(XNAT).get());
	}

}
