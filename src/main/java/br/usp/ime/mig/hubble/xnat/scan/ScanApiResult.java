package br.usp.ime.mig.hubble.xnat.scan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScanApiResult {
	@JsonProperty("ID")
	private String id;

	@JsonProperty("quality")
	private String quality;

	@JsonProperty("series_description")
	private String seriesDescription;

	@JsonProperty("type")
	private String type;

	@JsonProperty("xnat:mrscandata/file[0]/file_size")
	private long fileSize;

	@JsonProperty("xnat:mrscandata/file[0]/file_count")
	private int fileCount;

	@JsonProperty("xnat:mrscandata/file[0]/label")
	private String label;

	@JsonProperty("URI")
	private String uri;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getSeriesDescription() {
		return seriesDescription;
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
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
