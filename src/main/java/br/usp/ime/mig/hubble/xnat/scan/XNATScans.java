package br.usp.ime.mig.hubble.xnat.scan;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.usp.ime.mig.hubble.scan.Scan;
import br.usp.ime.mig.hubble.scan.Scans;
import br.usp.ime.mig.hubble.xnat.ApiResponseWrapper;
import br.usp.ime.mig.hubble.xnat.XNAT;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

@Service
public class XNATScans implements Scans {

	private final String findAllScansUrl;

	private final String downloadScanUrl;

	private final String downloadAllUrl;

	private RestTemplate restTemplate;

	@Autowired
	public XNATScans(@Value("${xnat.url}") String xnatBaseUrl, @XNAT RestTemplate restTemplate) {
		String scansBaseUrl = "/data/archive/projects/{projectId}/subjects/{subjectId}/experiments/{experimentId}/scans";

		String[] columns = { "ID",
				"quality",
				"series_description",
				"type",
				"xnat:mrScanData/file[0]/file_size",
				"xnat:mrScanData/file[0]/file_count",
				"xnat:mrScanData/file[0]/label" };

		this.findAllScansUrl = xnatBaseUrl + scansBaseUrl + "?format=json&columns=" + Joiner.on(",").join(columns);
		this.downloadScanUrl = xnatBaseUrl + scansBaseUrl + "/{scanId}/files?format=zip";
		this.downloadAllUrl = xnatBaseUrl + scansBaseUrl + "/ALL/files?format=zip";
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Scan> findByProjectAndSubjectAndExperiment(String projectId, String subjectId, String experimentId) {
		ScanApiResponseWrapper response = restTemplate.getForObject(findAllScansUrl,
				ScanApiResponseWrapper.class, projectId, subjectId, experimentId);

		List<Scan> scans = Collections.emptyList();

		if (response != null) {
			scans = Lists.transform(response.getResultSet().getResults(), (ScanApiResult r) -> {
				Scan scan = new Scan();

				scan.setId(r.getId());
				scan.setQuality(r.getQuality());
				scan.setFileType(r.getLabel());
				scan.setSeriesDescription(r.getSeriesDescription());
				scan.setFileCount(r.getFileCount());
				scan.setFileSize(r.getFileSize());
				scan.setProjectId(projectId);
				scan.setSubjectId(subjectId);
				scan.setExperimentId(experimentId);

				return scan;
			});
		}

		return scans;
	}

	@Override
	public void download(String projectId, String subjectId, String experimentId, String scanId) {

	}

	public static class ScanApiResponseWrapper extends ApiResponseWrapper<ScanApiResult> {
	}
}
