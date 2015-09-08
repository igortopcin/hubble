package br.usp.ime.mig.hubble.xnat.experiment;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.usp.ime.mig.hubble.experiment.Experiment;
import br.usp.ime.mig.hubble.experiment.Experiments;
import br.usp.ime.mig.hubble.xnat.ApiResponseWrapper;
import br.usp.ime.mig.hubble.xnat.XNAT;

import com.google.common.collect.Lists;

@Service
public class XNATExperiments implements Experiments {

	private final String serviceUrl;

	private RestTemplate restTemplate;

	@Autowired
	public XNATExperiments(@Value("${xnat.url}") String xnatBaseUrl, @XNAT RestTemplate restTemplate) {
		this.serviceUrl = xnatBaseUrl + "/data/archive/projects/{projectId}/subjects/{subjectId}/experiments?format=json";
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Experiment> findByProjectAndSubject(String projectId, String subjectId) {
		ExperimentApiResponseWrapper response = restTemplate.getForObject(serviceUrl,
				ExperimentApiResponseWrapper.class, projectId, subjectId);

		List<Experiment> experiments = Collections.emptyList();

		if (response != null) {
			experiments = Lists.transform(response.getResultSet().getResults(), (ExperimentApiResult r) -> {
				Experiment experiment = new Experiment();

				experiment.setId(r.getId());
				experiment.setLabel(r.getLabel());
				experiment.setDate(r.getDate());
				experiment.setInsertDate(r.getInsertDate());
				experiment.setProjectId(projectId);
				experiment.setSubjectId(subjectId);
				return experiment;
			});
		}

		return experiments;
	}

	public static class ExperimentApiResponseWrapper extends ApiResponseWrapper<ExperimentApiResult> {
	}
}
