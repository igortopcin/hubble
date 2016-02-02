package br.usp.ime.mig.hubble.downloader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;

public interface FileDownloader {
	Path download(URL url, Uploadable uploadable) throws IOException;
}