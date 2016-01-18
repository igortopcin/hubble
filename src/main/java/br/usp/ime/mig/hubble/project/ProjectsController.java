package br.usp.ime.mig.hubble.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
public class ProjectsController {

	private final Projects projects;

	@Autowired
	public ProjectsController(Projects projects) {
		this.projects = projects;
	}

	@RequestMapping
	public String index(Model model) {
		model.addAttribute("projects", projects.findAll());
				
		return "projects/index";
	}

}
