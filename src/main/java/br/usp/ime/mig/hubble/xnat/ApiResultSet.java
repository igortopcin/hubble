package br.usp.ime.mig.hubble.xnat;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResultSet<T> {

	private long totalRecords;

	@JsonProperty("Result")
	private List<T> results;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

}
