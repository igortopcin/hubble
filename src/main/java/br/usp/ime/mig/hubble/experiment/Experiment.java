package br.usp.ime.mig.hubble.experiment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import br.usp.ime.mig.hubble.externalcontent.ContentHandler;
import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;

@Data
public class Experiment implements Uploadable {

	private String id;

	private String label;

	private String projectId;

	private String subjectId;

	private LocalDateTime insertDate;

	private LocalDate date;

	// External reference, such as an unique ID or URI
	private String ref;

	@Override
	public String getName() {
		StringBuilder name = new StringBuilder()
				.append(projectId).append("/")
				.append(subjectId).append("/")
				.append(label);
		return name.toString();
	}

	@Override
	public ContentHandler getContentHandler() {
		return null;
	}

}
