package br.usp.ime.mig.hubble.xnat.project.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponseWrapper {

	@JsonProperty("ResultSet")
	private ApiResultSet resultSet;

	public ApiResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ApiResultSet resultSet) {
		this.resultSet = resultSet;
	}

}
