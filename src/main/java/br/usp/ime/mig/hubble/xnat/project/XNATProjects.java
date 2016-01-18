package br.usp.ime.mig.hubble.xnat.project;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.usp.ime.mig.hubble.project.Project;
import br.usp.ime.mig.hubble.project.Projects;
import br.usp.ime.mig.hubble.xnat.ListResponseWrapper;
import br.usp.ime.mig.hubble.xnat.XNAT;

import com.google.common.collect.Lists;

/**
 * Example endpoint:
 * http://mig.ime.usp.br/xnat/data/archive/projects/test-data/subjects
 * /SAPL_Study_3T_01/experiments/mig_E00001/scans/3/files
 */
@Service
public class XNATProjects implements Projects {

	private final String serviceUrl;

	private RestTemplate restTemplate;

	@Autowired
	public XNATProjects(@Value("${xnat.url}") String xnatBaseUrl, @XNAT RestTemplate restTemplate) {
		this.serviceUrl = xnatBaseUrl + "/data/archive/projects?format=json";
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Project> findAll() {

		ProjectApiResponseWrapper response = restTemplate.getForObject(serviceUrl, ProjectApiResponseWrapper.class);

		List<Project> projects = Collections.emptyList();

		if (response != null) {
			projects = Lists.transform(response.getResultSet().getResults(), (ProjectApiResult r) -> {
				Project project = new Project();

				project.setId(r.getId());
				project.setName(r.getName());
				project.setDescription(r.getDescription());
				project.setRef(r.getUri());
				return project;
			});
		}

		return projects;
	}

	public static class ProjectApiResponseWrapper extends ListResponseWrapper<ProjectApiResult> {
	};

}
