package br.usp.ime.mig.hubble.xnat.experiment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.usp.ime.mig.hubble.xnat.XNATLocalDateTimeDeserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


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

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(LocalDateTime insertDate) {
		this.insertDate = insertDate;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
