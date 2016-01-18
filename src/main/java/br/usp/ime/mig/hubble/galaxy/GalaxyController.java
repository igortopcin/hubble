package br.usp.ime.mig.hubble.galaxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/galaxy")
public class GalaxyController {

	private final GalaxyContext galaxyContext;

	@Autowired
	public GalaxyController(GalaxyContext galaxyContext) {
		this.galaxyContext = galaxyContext;
	}

	@RequestMapping("back")
	public String sendUserBackToGalaxy() {
		return "redirect:" + galaxyContext.getReturnUrl();
	}

	@RequestMapping("selected-items")
	public String viewSelectedUploadables() {
		return "galaxy/index";
	}

}
