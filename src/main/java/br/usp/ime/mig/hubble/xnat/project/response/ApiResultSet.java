package br.usp.ime.mig.hubble.xnat.project.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResultSet {

	private long totalRecords;

	@JsonProperty("Result")
	private List<ApiResult> results;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<ApiResult> getResults() {
		return results;
	}

	public void setResults(List<ApiResult> results) {
		this.results = results;
	}

}
