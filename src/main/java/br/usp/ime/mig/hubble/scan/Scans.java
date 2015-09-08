package br.usp.ime.mig.hubble.scan;

import java.util.List;

public interface Scans {
	List<Scan> findByProjectAndSubjectAndExperiment(String projectId, String subjectId, String experimentId);

	void download(String projectId, String subjectId, String experimentId, String scanId);
}
