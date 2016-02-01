package br.usp.ime.mig.hubble.galaxy.dataset;

/**
 * Types of contents within an {@link Uploadable}.
 */
public enum UploadableType {
	/**
	 * The Uploadable contains all scans within project.
	 */
	PROJECT,

	/**
	 * The Uploadable contains all scans for a specific pacient in a project.
	 */
	PACIENT,

	/**
	 * The Uploadable contains all scans of a particular experiment for a
	 * specific pacient.
	 */
	EXPERIMENT,

	/**
	 * The smallest granularity of an Uploadable: a single pacient's scan.
	 */
	SCAN,
}
