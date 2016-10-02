package br.usp.ime.mig.hubble.xnat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;

import br.usp.ime.mig.hubble.auth.ExternalCredential;
import br.usp.ime.mig.hubble.downloader.FileDownloader;
import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;

import com.google.common.collect.ImmutableMap;

@Slf4j
public class XNATFileDownloader implements FileDownloader {

	private static final String DOWNLOADED_DIR = "downloaded";
	private static final String REPACKAGED_DIR = "repackaged";

	private final String stagingDir;
	private final String user;
	private final String password;

	public XNATFileDownloader(String stagingDir, ExternalCredential credentials) {
		this.stagingDir = stagingDir;
		user = credentials.getUsername();
		password = credentials.getPassword();
	}

	@Override
	public List<Path> download(URL url, Uploadable uploadable) throws IOException {
		Path file = Paths.get(this.stagingDir, DOWNLOADED_DIR, uploadable.getFileName() + ".zip");

		if (Files.notExists(file)) {
			Path parent = file.getParent();
			if (Files.notExists(parent)) {
				Files.createDirectories(parent);
			}

			URLConnection conn = url.openConnection();
			String authString = user + ":" + password;
			conn.setRequestProperty("Authorization", "Basic " + new String(Base64.encodeBase64(authString.getBytes())));
			try (InputStream in = conn.getInputStream()) {
				Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		return repackage(uploadable);
	}

	private List<Path> repackage(Uploadable uploadable) throws IOException {
		Path original = Paths.get(this.stagingDir, DOWNLOADED_DIR, uploadable.getFileName() + ".zip");
		Path tempDir = createTempDirectory();
		Path repackagedDicom = Paths.get(tempDir.toString(), uploadable.getFileName() + ".dcm.zip");

		List<Path> repackagedFiles = new ArrayList<>();
		repackagedFiles.addAll(
				copyNiftiFiles(original, tempDir, uploadable));

		if (copyDicomFiles(original, repackagedDicom, uploadable)) {
			repackagedFiles.add(repackagedDicom);
		}
		return repackagedFiles;
	}

	private Path createTempDirectory() throws IOException {
		Path parent = Paths.get(this.stagingDir, REPACKAGED_DIR);
		if (Files.notExists(parent)) {
			Files.createDirectories(parent);
		}
		Path tempDir = Files.createTempDirectory(parent, null);
		return tempDir;
	}

	private static FileSystem createZipFileSystem(Path path, boolean create) throws IOException {
		final URI uri = URI.create("jar:file:" + path.toUri().getPath());
		return FileSystems.newFileSystem(uri, create ? ImmutableMap.of("create", "true") : Collections.emptyMap());
	}

	private static boolean copyDicomFiles(Path source, Path destination, Uploadable uploadable) throws IOException {
		log.info("Copying DICOM files from {} to {}", source, destination);

		if (!hasAnyDicomFiles(source)) {
			return false;
		}

		try (
				FileSystem sourceFS = createZipFileSystem(source, false);
				FileSystem destinationFS = createZipFileSystem(destination, true)) {
			Path destRoot = destinationFS.getPath("/");

			Files.walk(sourceFS.getPath("/"))
					.filter(src -> isDicomFile(src))
					.forEach(src -> {
						String scanLabel = src.getName(2).toString();
						String filename = src.getFileName().toString();
						Path dest = destinationFS.getPath(
								destRoot.toString(),
								uploadable.getSubjectLabel(),
								uploadable.getExperimentLabel(),
								scanLabel,
								filename);
						Path parent = dest.getParent();
						log.debug("Copying file {} to {}", src, dest);
						try {
							if (Files.notExists(parent)) {
								Files.createDirectories(parent);
							}
							Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
						} catch (Exception e) {
							new RuntimeException(e);
						}
					});
		}
		return true;
	}

	private static boolean hasAnyDicomFiles(Path source) throws IOException {
		try (FileSystem sourceFS = createZipFileSystem(source, false)) {
			return Files.walk(sourceFS.getPath("/")).anyMatch(src -> isDicomFile(src));
		}
	}

	private static boolean isDicomFile(Path path) {
		return !Files.isDirectory(path)
				&& !path.getFileName().toString().endsWith(".gif")
				&& !path.getFileName().toString().endsWith(".nii.gz");
	}

	private static List<Path> copyNiftiFiles(Path source, Path destinationRoot, Uploadable uploadable) throws IOException {
		final List<Path> copiedFiles = new ArrayList<>();

		log.info("Copying NIfTI files from {} to {}", source, destinationRoot);
		try (FileSystem sourceFS = createZipFileSystem(source, false)) {
			Files.walk(sourceFS.getPath("/"))
					.filter(src -> isNiftiFile(src))
					.forEach(src -> {
						Path dest = Paths.get(destinationRoot.toString(), src.getFileName().toString());
						log.debug("Copying file {} to {}", src, dest);
						try {
							Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
						} catch (Exception e) {
							new RuntimeException(e);
						}
						copiedFiles.add(dest);
					});
		}
		return copiedFiles;
	}

	private static boolean isNiftiFile(Path path) {
		return !Files.isDirectory(path) && path.getFileName().toString().endsWith(".nii.gz");
	}
}
