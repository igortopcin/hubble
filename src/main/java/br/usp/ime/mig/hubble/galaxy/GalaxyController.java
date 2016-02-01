package br.usp.ime.mig.hubble.galaxy;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;
import br.usp.ime.mig.hubble.xnat.scan.UploadableFinder;

import com.google.common.collect.Maps;

@Controller
@RequestMapping("/galaxy")
public class GalaxyController {

	private final GalaxyContext galaxyContext;

	private final Map<UploadableType, UploadableFinder> finders;

	@Autowired
	public GalaxyController(GalaxyContext galaxyContext, List<UploadableFinder> finders) {
		this.galaxyContext = galaxyContext;
		this.finders = Maps.uniqueIndex(finders, UploadableFinder::getUploadableType);
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
}
