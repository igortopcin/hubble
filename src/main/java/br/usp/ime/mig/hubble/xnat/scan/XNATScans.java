package br.usp.ime.mig.hubble.xnat.scan;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.usp.ime.mig.hubble.experiment.Experiment;
import br.usp.ime.mig.hubble.experiment.Experiments;
import br.usp.ime.mig.hubble.galaxy.UploadableFinder;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;
import br.usp.ime.mig.hubble.scan.Scan;
import br.usp.ime.mig.hubble.scan.Scans;
import br.usp.ime.mig.hubble.xnat.ListResponseWrapper;
import br.usp.ime.mig.hubble.xnat.XNAT;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

@Service
public class XNATScans implements Scans, UploadableFinder {

	private final String findAllScansUrl;

	private final RestTemplate restTemplate;

	private final Experiments experiments;

	public static final XNATScanConverter CONVERTER = new XNATScanConverter();

	@Autowired
	public XNATScans(@Value("${xnat.url}") String xnatBaseUrl,
			@XNAT RestTemplate restTemplate,
			Experiments experiments) {
		String scansBaseUrl = "{experimentRef}/scans";

		String[] columns = { "ID",
				"quality",
				"series_description",
				"type",
				"xnat:mrScanData/file[0]/file_size",
				"xnat:mrScanData/file[0]/file_count",
				"xnat:mrScanData/file[0]/label" };

		this.findAllScansUrl = xnatBaseUrl + scansBaseUrl + "?format=json&file_format=DICOM&columns="
				+ Joiner.on(",").join(columns);
		this.restTemplate = restTemplate;
		this.experiments = experiments;
	}

	@Override
	public List<Scan> findByExperiment(String experimentRef) {
		Optional<Experiment> experiment = experiments.findByRef(experimentRef);
		if (!experiment.isPresent()) {
			return Collections.emptyList();
		}

		ScanApiResponseWrapper response = restTemplate.getForObject(findAllScansUrl,
				ScanApiResponseWrapper.class, experimentRef);
		if (response == null || response.getResultSet().getTotalRecords() == 0) {
			return Collections.emptyList();
		}

		return Lists.transform(response.getResultSet().getResults(), r -> {
			Scan scan = CONVERTER.apply(r);
			scan.setExperiment(experiment.get());
			return scan;
		}).stream()
				.filter(s -> isSupportedFileType(s.getFileType()))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Scan> findByRef(String scanRef) {
		Preconditions.checkNotNull(scanRef);

		String experimentRef = scanRef.substring(0, scanRef.lastIndexOf('/')).replace("/scans", "");
		Optional<Experiment> experiment = experiments.findByRef(experimentRef);
		if (!experiment.isPresent()) {
			return Optional.empty();
		}

		ScanApiResponseWrapper response = restTemplate.getForObject(findAllScansUrl,
				ScanApiResponseWrapper.class, experimentRef);
		if (response == null) {
			return Optional.empty();
		}

		return response.getResultSet().getResults().stream()
				.filter(r -> scanRef.equals(r.getUri()) && isSupportedFileType(r.getLabel()))
				.map(r -> {
					Scan s = CONVERTER.apply(r);
					s.setExperiment(experiment.get());
					return s;
				}).findFirst();
	}

	private boolean isSupportedFileType(String type) {
		return "DICOM".equals(type) || "NIFTI".equals(type);
	}

	@Override
	public UploadableType getUploadableType() {
		return UploadableType.SCAN;
	}

	public static class ScanApiResponseWrapper extends ListResponseWrapper<ScanApiResult> {
	}

}
