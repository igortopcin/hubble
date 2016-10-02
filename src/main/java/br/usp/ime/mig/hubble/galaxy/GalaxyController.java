package br.usp.ime.mig.hubble.galaxy;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.usp.ime.mig.hubble.downloader.FileDownloader;
import br.usp.ime.mig.hubble.downloader.FileDownloaderFactory;
import br.usp.ime.mig.hubble.downloader.UploadableURLResolver;
import br.usp.ime.mig.hubble.galaxy.dataset.FileSender;
import br.usp.ime.mig.hubble.galaxy.dataset.FileSender.UploadedFile;
import br.usp.ime.mig.hubble.galaxy.dataset.FileSenderFactory;
import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;

import com.github.jmchilton.blend4j.galaxy.HistoriesClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/galaxy")
@Slf4j
public class GalaxyController {

	private final GalaxyContext galaxyContext;

	private final Map<UploadableType, UploadableFinder> finders;

	private final Map<UploadableType, UploadableURLResolver> urlResolvers;

	private final FileDownloaderFactory downloaderFactory;

	private final FileSenderFactory senderFactory;

	private final TaskExecutor taskExecutor;

	@Autowired
	public GalaxyController(GalaxyContext galaxyContext,
			List<UploadableFinder> finders,
			List<UploadableURLResolver> urlResolvers,
			FileDownloaderFactory downloaderFactory,
			FileSenderFactory senderFactory,
			TaskExecutor taskExecutor) {
		this.galaxyContext = galaxyContext;
		this.downloaderFactory = downloaderFactory;
		this.senderFactory = senderFactory;
		this.taskExecutor = taskExecutor;
		this.finders = Maps.uniqueIndex(finders, UploadableFinder::getUploadableType);
		this.urlResolvers = Maps.uniqueIndex(urlResolvers, UploadableURLResolver::getUploadableType);
	}

	@RequestMapping("selected-items")
	public String viewSelectedUploadables(Model model) {
		HistoriesClient historiesClient = galaxyContext.getGalaxyInstance().getHistoriesClient();
		model.addAttribute("histories", historiesClient.getHistories());
		model.addAttribute("currentHistory", historiesClient.showHistory("most_recently_used"));
		return "galaxy/index";
	}

	@RequestMapping(
			path = "/upload-status",
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody UploadStatus uploadStatus() {
		return galaxyContext.getUploadStatus();
	}

	@RequestMapping(
			path = "/select",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody long select(String ref, UploadableType type) {
		boolean removed = galaxyContext.getSelectedUploadables().removeIf(u -> ref.equals(u.getRef()));
		if (!removed) {
			findUploadable(ref, type)
					.ifPresent(u -> galaxyContext.addUploadables(u));
		}
		return galaxyContext.getSelectedUploadablesSize();
	}

	private Optional<? extends Uploadable> findUploadable(String ref, UploadableType type) {
		UploadableFinder finder = finders.get(type);
		if (finder == null) {
			return Optional.empty();
		}
		return finder.findByRef(ref);
	}

	@RequestMapping(
			path = "history/{historyId}/send",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody void send(@PathVariable("historyId") String historyId) {
		UploadStatus uploadStatus = galaxyContext.getUploadStatus();
		FileDownloader downloader = downloaderFactory.create();
		FileSender sender = senderFactory.create();

		taskExecutor.execute(() -> send(historyId, uploadStatus, downloader, sender));
	}

	private void send(String historyId,
			UploadStatus uploadStatus,
			FileDownloader downloader,
			FileSender sender) {
		try {
			uploadStatus.setUploadInProgress(true);

			List<UploadedFile> sentFiles = new ArrayList<>();

			Lists.newArrayList(uploadStatus.getUploadables())
					.parallelStream()
					.forEach(u -> {
						try {
							URL url = urlResolvers.get(u.getType()).apply(u);
							List<Path> downloadedFiles = downloader.download(url, u);

							downloadedFiles.forEach(downloadedFile -> {
									sender.send(downloadedFile, u, historyId)
											.ifPresent(f -> {
												sentFiles.add(f);
												uploadStatus.getUploadables().remove(u);
											});
							});
						} catch (IOException e) {
							log.error("Failed to upload {}", u, e);
						}
					});
			sender.createCollection(sentFiles);
		} finally {
			uploadStatus.setUploadInProgress(false);
		}
	}

}
