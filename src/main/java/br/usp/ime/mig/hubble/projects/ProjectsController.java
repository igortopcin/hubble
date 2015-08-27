package br.usp.ime.mig.hubble.projects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProjectsController {

	@RequestMapping("/")
	public String showIndex(Model model) {
		model.addAttribute("message", "Welcome to Hubble");
		return "home";
	}
	
}
