package br.usp.ime.mig.hubble.xnat.project;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectApiResult {

	@JsonProperty("ID")
	private String id;

	@JsonProperty("secondary_ID")
	private String secondaryId;

	@JsonProperty("URI")
	private String uri;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("pi_firstname")
	private String principalInvestigatorFirstname;

	@JsonProperty("pi_lastname")
	private String principalInvestigatorLastname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecondaryId() {
		return secondaryId;
	}

	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrincipalInvestigatorFirstname() {
		return principalInvestigatorFirstname;
	}

	public void setPrincipalInvestigatorFirstname(String principalInvestigatorFirstname) {
		this.principalInvestigatorFirstname = principalInvestigatorFirstname;
	}

	public String getPrincipalInvestigatorLastname() {
		return principalInvestigatorLastname;
	}

	public void setPrincipalInvestigatorLastname(String principalInvestigatorLastname) {
		this.principalInvestigatorLastname = principalInvestigatorLastname;
	}

}
