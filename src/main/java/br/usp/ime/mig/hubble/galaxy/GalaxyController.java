package br.usp.ime.mig.hubble.galaxy;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.usp.ime.mig.hubble.downloader.FileDownloader;
import br.usp.ime.mig.hubble.downloader.FileDownloaderFactory;
import br.usp.ime.mig.hubble.downloader.UploadableURLResolver;
import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;

import com.google.common.collect.Maps;

@Controller
@RequestMapping("/galaxy")
@Slf4j
public class GalaxyController {

	private final GalaxyContext galaxyContext;

	private final Map<UploadableType, UploadableFinder> finders;

	private final Map<UploadableType, UploadableURLResolver> urlResolvers;

	private final FileDownloaderFactory downloaderFactory;

	@Autowired
	public GalaxyController(GalaxyContext galaxyContext,
			List<UploadableFinder> finders,
			List<UploadableURLResolver> urlResolvers,
			FileDownloaderFactory downloaderFactory) {
		this.galaxyContext = galaxyContext;
		this.downloaderFactory = downloaderFactory;
		this.finders = Maps.uniqueIndex(finders, UploadableFinder::getUploadableType);
		this.urlResolvers = Maps.uniqueIndex(urlResolvers, UploadableURLResolver::getUploadableType);
	}

	@RequestMapping("back")
	public String sendUserBackToGalaxy() {
		return "redirect:" + galaxyContext.getReturnUrl();
	}

	@RequestMapping("selected-items")
	public String viewSelectedUploadables() {
		return "galaxy/index";
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
			path = "/send",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody void send() {
		FileDownloader downloader = downloaderFactory.create();

		galaxyContext.getSelectedUploadables()
				.parallelStream()
				.forEach(u -> {
					try {
						URL url = urlResolvers.get(u.getType()).apply(u);
						downloader.download(url, u);
					} catch (IOException e) {
						log.error("Failed to upload {}", u, e);
					}
				});

		galaxyContext.getSelectedUploadables().clear();
	}
}
