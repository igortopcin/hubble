package br.usp.ime.mig.hubble.scan;

import lombok.Data;
import br.usp.ime.mig.hubble.experiment.Experiment;
import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;

@Data
public class Scan implements Uploadable {

	private String id;

	private String quality;

	private String seriesDescription;

	private long fileSize;

	private int fileCount;

	private String fileType;

	// External reference, such as an unique ID or URI
	private String ref;

	private Experiment experiment;

	@Override
	public UploadableType getType() {
		return UploadableType.SCAN;
	}

	@Override
	public String getScanLabel() {
		return id;
	}

	@Override
	public String getExperimentLabel() {
		return experiment == null ? null : experiment.getLabel();
	}

	@Override
	public String getProjectLabel() {
		return experiment == null ? null : experiment.getProjectId();
	}

	@Override
	public String getSubjectLabel() {
		return experiment == null ? null : experiment.getSubjectId();
	}

}
