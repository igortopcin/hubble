package br.usp.ime.mig.hubble.experiment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/experiments")
public class ExperimentsController {

	private final Experiments experiments;

	@Autowired
	public ExperimentsController(Experiments experiments) {
		this.experiments = experiments;
	}

	@RequestMapping
	public String index(@RequestParam("projectId") String projectId, @RequestParam("subjectId") String subjectId, Model model) {
		model.addAttribute("experiments", experiments.findByProjectAndSubject(projectId, subjectId));
				
		return "experiments/index";
	}

}
