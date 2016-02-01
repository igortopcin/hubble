package br.usp.ime.mig.hubble.scan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.usp.ime.mig.hubble.galaxy.GalaxyContext;

@Controller
@RequestMapping("/scans")
public class ScansController {

	private final Scans scans;

	private GalaxyContext context;

	@Autowired
	public ScansController(Scans scans, GalaxyContext context) {
		this.scans = scans;
		this.context = context;
	}

	@RequestMapping
	public String index(String experimentRef, Model model) {
		model.addAttribute("scans", scans.findByExperiment(experimentRef));

		return "experiments/scans";
	}

	@RequestMapping("/select")
	public @ResponseBody long sendToGalaxy(String scanRef) {
		boolean removed = context.getSelectedUploadables().removeIf(u -> scanRef.equals(u.getRef()));
		if (!removed) {
			Scan scan = scans.findByRef(scanRef).orElse(null);
			context.addUploadables(scan);
		}
		return context.getSelectedUploadablesSize();
	}

}
