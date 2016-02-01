package br.usp.ime.mig.hubble.experiment;

import java.util.List;
import java.util.Optional;

public interface Experiments {
	List<Experiment> findByProjectAndSubject(String projectId, String subjectId);

	Optional<Experiment> findByRef(String experimentRef);
}
