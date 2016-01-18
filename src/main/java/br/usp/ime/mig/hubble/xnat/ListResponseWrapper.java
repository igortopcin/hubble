package br.usp.ime.mig.hubble.xnat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListResponseWrapper<T> {

	@JsonProperty("ResultSet")
	private ApiResultSet<T> resultSet;

	public ApiResultSet<T> getResultSet() {
		return resultSet;
	}

	public void setResultSet(ApiResultSet<T> resultSet) {
		this.resultSet = resultSet;
	}

}
