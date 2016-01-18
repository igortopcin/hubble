package br.usp.ime.mig.hubble.scan;

import lombok.Data;
import br.usp.ime.mig.hubble.experiment.Experiment;
import br.usp.ime.mig.hubble.externalcontent.ContentHandler;
import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;

import com.google.common.base.Joiner;

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
	public String getName() {
		return Joiner.on(", ").skipNulls().join(
				experiment == null ? null : experiment.getProjectId(),
				experiment == null ? null : experiment.getSubjectId(),
				experiment == null ? null : experiment.getId(),
				id);
	}

	@Override
	public ContentHandler getContentHandler() {
		return null;
	}

}
