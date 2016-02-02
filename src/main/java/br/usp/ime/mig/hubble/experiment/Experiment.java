package br.usp.ime.mig.hubble.experiment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;

import com.google.common.base.Joiner;

@Data
public class Experiment implements Uploadable {

	private String id;

	private String label;

	private String projectId;

	private String subjectId;

	private String subjectLabel;

	private LocalDateTime insertDate;

	private LocalDate date;

	// External reference, such as an unique ID or URI
	private String ref;

	@Override
	public UploadableType getType() {
		return UploadableType.EXPERIMENT;
	}

	@Override
	public String getScanLabel() {
		return "ALL";
	}

	@Override
	public String getExperimentLabel() {
		return label;
	}

	@Override
	public String getProjectLabel() {
		return projectId;
	}

	@Override
	public String getFileName() {
		return Joiner.on("-").skipNulls()
				.join(projectId, subjectLabel, label, getScanLabel())
				.toLowerCase()
				.replace(' ', '-');
	}

}
