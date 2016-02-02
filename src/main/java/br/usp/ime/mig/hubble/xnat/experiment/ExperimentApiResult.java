package br.usp.ime.mig.hubble.xnat.experiment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import br.usp.ime.mig.hubble.xnat.XNATLocalDateTimeDeserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Data
public class ExperimentApiResult {

	@JsonProperty("project")
	private String projectId;

	@JsonProperty("xsiType")
	private String type;

	@JsonProperty("ID")
	private String id;

	@JsonProperty("insert_date")
	@JsonDeserialize(using = XNATLocalDateTimeDeserializer.class)
	private LocalDateTime insertDate;

	@JsonProperty("label")
	private String label;

	@JsonProperty("date")
	private LocalDate date;

	@JsonProperty("URI")
	private String uri;

	@JsonProperty("subject_ID")
	private String subjectId;

	@JsonProperty("subject_label")
	private String subjectLabel;

}
