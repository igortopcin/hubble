package br.usp.ime.mig.hubble.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/subjects")
public class SubjectsController {

	private final Subjects subjects;

	@Autowired
	public SubjectsController(Subjects subjects) {
		this.subjects = subjects;
	}

	@RequestMapping
	public String index(@RequestParam("projectId") String projectId, Model model) {
		model.addAttribute("projectId", projectId);
		model.addAttribute("subjects", subjects.findByProject(projectId));
				
		return "subjects/index";
	}

}
