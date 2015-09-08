package br.usp.ime.mig.hubble.xnat.subject;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.usp.ime.mig.hubble.subject.Subject;
import br.usp.ime.mig.hubble.subject.Subjects;
import br.usp.ime.mig.hubble.xnat.ApiResponseWrapper;
import br.usp.ime.mig.hubble.xnat.XNAT;

import com.google.common.collect.Lists;

@Service
public class XNATSubjects implements Subjects {

	private final String serviceUrl;

	private RestTemplate restTemplate;

	@Autowired
	public XNATSubjects(@Value("${xnat.url}") String xnatBaseUrl, @XNAT RestTemplate restTemplate) {
		this.serviceUrl = xnatBaseUrl + "/data/archive/projects/{projectId}/subjects?format=json";
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Subject> findByProject(String projectId) {
		SubjectApiResponseWrapper response = restTemplate.getForObject(serviceUrl,
				SubjectApiResponseWrapper.class, projectId);

		List<Subject> subjects = Collections.emptyList();

		if (response != null) {
			subjects = Lists.transform(response.getResultSet().getResults(), (SubjectApiResult r) -> {
				Subject subject = new Subject();

				subject.setId(r.getId());
				subject.setProjectId(r.getProjectId());
				subject.setInsertDate(r.getInsertDate());
				subject.setLabel(r.getLabel());
				return subject;
			});
		}

		return subjects;
	}

	public static class SubjectApiResponseWrapper extends ApiResponseWrapper<SubjectApiResult> {
	}
}
