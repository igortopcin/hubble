package br.usp.ime.mig.hubble.scan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scans")
public class ScansController {

	private final Scans scans;

	@Autowired
	public ScansController(Scans scans) {
		this.scans = scans;
	}

	@RequestMapping
	public String index(String projectId, String subjectId, String experimentId, Model model) {
		model.addAttribute("scans", scans.findByProjectAndSubjectAndExperiment(projectId, subjectId, experimentId));

		return "experiments/scans";
	}

}
