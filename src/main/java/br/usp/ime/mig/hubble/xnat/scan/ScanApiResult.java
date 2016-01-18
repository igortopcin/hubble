package br.usp.ime.mig.hubble.xnat.scan;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ScanApiResult {
	@JsonProperty("ID")
	private String id;

	private String quality;

	@JsonProperty("series_description")
	private String seriesDescription;

	private String type;

	@JsonProperty("xnat:mrscandata/file[0]/file_size")
	private long fileSize;

	@JsonProperty("xnat:mrscandata/file[0]/file_count")
	private int fileCount;

	@JsonProperty("xnat:mrscandata/file[0]/label")
	private String label;

	@JsonProperty("URI")
	private String uri;

}
