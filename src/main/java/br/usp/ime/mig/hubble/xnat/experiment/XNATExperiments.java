package br.usp.ime.mig.hubble.xnat.experiment;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.usp.ime.mig.hubble.cache.CacheConfiguration;
import br.usp.ime.mig.hubble.experiment.Experiment;
import br.usp.ime.mig.hubble.experiment.Experiments;
import br.usp.ime.mig.hubble.xnat.ListResponseWrapper;
import br.usp.ime.mig.hubble.xnat.XNAT;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

@Service
public class XNATExperiments implements Experiments {

	private static final XNATExperimentConverter CONVERTER = new XNATExperimentConverter();

	private final String findAllExperimentsUrl;

	private final String findExperimentUrl;

	private final RestTemplate restTemplate;

	@Autowired
	public XNATExperiments(@Value("${xnat.url}") String xnatBaseUrl, @XNAT RestTemplate restTemplate) {
		String extraFields = "&columns=subject_ID,ID,insert_date,label,date";

		this.findAllExperimentsUrl = xnatBaseUrl
				+ "/data/archive/projects/{projectId}/subjects/{subjectId}/experiments?format=json" + extraFields;
		this.findExperimentUrl = xnatBaseUrl + "/data/experiments?format=json&ID={experimentId}" + extraFields;
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Experiment> findByProjectAndSubject(String projectId, String subjectId) {
		ExperimentApiResponseWrapper response = restTemplate.getForObject(findAllExperimentsUrl,
				ExperimentApiResponseWrapper.class, projectId, subjectId);

		List<Experiment> experiments = Collections.emptyList();

		if (response != null) {
			experiments = Lists.transform(response.getResultSet().getResults(), CONVERTER);
		}

		return experiments;
	}

	@Override
	@Cacheable(CacheConfiguration.EXPERIMENTS_CACHE)
	public Experiment findByRef(String experimentRef) {
		Preconditions.checkNotNull(experimentRef);

		String[] splittedRef = experimentRef.split("/");
		String id = splittedRef[splittedRef.length - 1];
		ExperimentApiResponseWrapper response = restTemplate.getForObject(findExperimentUrl,
				ExperimentApiResponseWrapper.class, id);

		Experiment experiment = null;

		if (response != null && !response.getResultSet().getResults().isEmpty()) {
			experiment = CONVERTER.apply(response.getResultSet().getResults().get(0));
		}

		return experiment;
	}

	public static class ExperimentApiResponseWrapper extends ListResponseWrapper<ExperimentApiResult> {
	}
}
