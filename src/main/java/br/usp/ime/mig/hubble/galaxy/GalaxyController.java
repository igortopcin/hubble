package br.usp.ime.mig.hubble.galaxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/back-to-galaxy")
public class GalaxyController {

	private final GalaxyContext galaxyContext;

	@Autowired
	public GalaxyController(GalaxyContext galaxyContext) {
		this.galaxyContext = galaxyContext;
	}

	@RequestMapping
	public String sendUserBackToGalaxy() {
		return "redirect:" + galaxyContext.getReturnUrl();
	}

}
