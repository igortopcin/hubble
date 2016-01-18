package br.usp.ime.mig.hubble.xnat.experiment;

import br.usp.ime.mig.hubble.experiment.Experiment;

import com.google.common.base.Function;

public class XNATExperimentConverter implements Function<ExperimentApiResult, Experiment> {

	@Override
	public Experiment apply(ExperimentApiResult r) {
		Experiment experiment = new Experiment();

		experiment.setId(r.getId());
		experiment.setLabel(r.getLabel());
		experiment.setDate(r.getDate());
		experiment.setInsertDate(r.getInsertDate());
		experiment.setProjectId(r.getProjectId());
		experiment.setSubjectId(r.getSubjectId());
		experiment.setRef(r.getUri());
		return experiment;
	}

}
