package br.usp.ime.mig.hubble.xnat.experiment;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

import br.usp.ime.mig.hubble.cache.CacheConfiguration;
import br.usp.ime.mig.hubble.experiment.Experiment;
import br.usp.ime.mig.hubble.experiment.Experiments;
import br.usp.ime.mig.hubble.galaxy.FileDownloader;
import br.usp.ime.mig.hubble.galaxy.UploadableFinder;
import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;
import br.usp.ime.mig.hubble.xnat.ListResponseWrapper;
import br.usp.ime.mig.hubble.xnat.XNAT;
import br.usp.ime.mig.hubble.xnat.XNATDownloadHelper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

@Service
@Slf4j
public class XNATExperiments implements Experiments, UploadableFinder, FileDownloader {

	private static final XNATExperimentConverter CONVERTER = new XNATExperimentConverter();

	private final String findAllExperimentsUrl;

	private final String findExperimentUrl;

	private final RestTemplate restTemplate;

	private final String downloadExperimentUrl;

	private final UriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();

	@Autowired
	public XNATExperiments(@Value("${xnat.url}") String xnatBaseUrl,
			@XNAT RestTemplate restTemplate) {

		String extraFields = "&columns=subject_ID,ID,insert_date,label,date";
		
		this.findAllExperimentsUrl = xnatBaseUrl
				+ "/data/archive/projects/{projectId}/subjects/{subjectId}/experiments?format=json" + extraFields;
		this.findExperimentUrl = xnatBaseUrl + "/data/experiments?format=json&ID={experimentId}" + extraFields;
		this.downloadExperimentUrl = xnatBaseUrl + "{experimentRef}/scans/ALL/files?format=zip&file_format=DICOM";
		this.restTemplate = restTemplate;
		;
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
	public Optional<Experiment> findByRef(String experimentRef) {
		Preconditions.checkNotNull(experimentRef);

		String[] splittedRef = experimentRef.split("/");
		String id = splittedRef[splittedRef.length - 1];
		ExperimentApiResponseWrapper response = restTemplate.getForObject(findExperimentUrl,
				ExperimentApiResponseWrapper.class, id);

		Optional<Experiment> experiment = Optional.empty();

		if (response != null && !response.getResultSet().getResults().isEmpty()) {
			experiment = Optional.of(CONVERTER.apply(response.getResultSet().getResults().get(0)));
		}

		return experiment;
	}

	@Override
	public Optional<String> download(Uploadable experiment, XNATDownloadHelper helper) {
		URI experimentUrl = uriTemplateHandler.expand(this.downloadExperimentUrl, experiment.getRef());
		String filename = experiment.getFileName();

		try {
			return Optional.of(helper.download(experimentUrl.toURL(), filename));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public UploadableType getUploadableType() {
		return UploadableType.EXPERIMENT;
	}

	public static class ExperimentApiResponseWrapper extends ListResponseWrapper<ExperimentApiResult> {
	}
}
