package br.usp.ime.mig.hubble.xnat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.usp.ime.mig.hubble.xnat.auth.XNATCredentials;

public class XNATDownloadHelper {

	private static final String DOWNLOADED_DIR = "downloaded";

	private final String stagingDir;
	private final String user;
	private final String password;

	public XNATDownloadHelper(String stagingDir, XNATCredentials credentials) {
		this.stagingDir = stagingDir;
		user = credentials.getUser();
		password = credentials.getPassword();
	}

	public String download(URL url, String fileRelativePath) throws IOException {
		Path file = Paths.get(this.stagingDir, DOWNLOADED_DIR, fileRelativePath);

		URLConnection conn = url.openConnection();
		String authString = user + ":" + password;
		conn.setRequestProperty("Authorization", "Basic " + new String(Base64.encodeBase64(authString.getBytes())));
		try (InputStream in = conn.getInputStream()) {
			Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
		}
		return file.toString();
	}

	@Component
	public static class Factory {
		private final String stagingDir;
		private final XNATCredentials credentials;

		@Autowired
		public Factory(@Value("${staging.dir}") String stagingDir, XNATCredentials credentials) {
			this.stagingDir = stagingDir;
			this.credentials = credentials;
		}

		public XNATDownloadHelper create() {
			return new XNATDownloadHelper(stagingDir, credentials);
		}
	}
}
