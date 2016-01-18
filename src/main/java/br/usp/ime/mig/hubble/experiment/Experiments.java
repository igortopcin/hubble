package br.usp.ime.mig.hubble.experiment;

import java.util.List;

public interface Experiments {
	List<Experiment> findByProjectAndSubject(String projectId, String subjectId);

	Experiment findByRef(String experimentRef);
}
