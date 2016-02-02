package br.usp.ime.mig.hubble.galaxy.dataset;


public interface Uploadable {
	String getScanLabel();

	String getExperimentLabel();

	String getProjectLabel();

	String getSubjectLabel();

	String getRef();

	UploadableType getType();

	String getFileName();
}
