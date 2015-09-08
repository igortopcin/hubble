package br.usp.ime.mig.hubble.xnat.subject;

import java.time.LocalDateTime;

import br.usp.ime.mig.hubble.xnat.XNATLocalDateTimeDeserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SubjectApiResult {

	@JsonProperty("ID")
	private String id;

	@JsonProperty("project")
	private String projectId;

	@JsonProperty("insert_user")
	private String insertUser;

	@JsonProperty("insert_date")
	@JsonDeserialize(using = XNATLocalDateTimeDeserializer.class)
	private LocalDateTime insertDate;

	@JsonProperty("label")
	private String label;

	@JsonProperty("URI")
	private String uri;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getInsertUser() {
		return insertUser;
	}

	public void setInsertUser(String insertUser) {
		this.insertUser = insertUser;
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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
