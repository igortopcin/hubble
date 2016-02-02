package br.usp.ime.mig.hubble.xnat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.usp.ime.mig.hubble.downloader.FileDownloader;
import br.usp.ime.mig.hubble.downloader.FileDownloaderFactory;
import br.usp.ime.mig.hubble.xnat.auth.XNATCredentials;

@Component
public class XNATFileDownloaderFactory implements FileDownloaderFactory {
	private final String stagingDir;
	private final XNATCredentials credentials;

	@Autowired
	public XNATFileDownloaderFactory(
			@Value("${staging.dir}") String stagingDir,
			XNATCredentials credentials) {
		this.stagingDir = stagingDir;
		this.credentials = credentials;
	}

	@Override
	public FileDownloader create() {
		return new XNATFileDownloader(stagingDir, credentials);
	}

}
