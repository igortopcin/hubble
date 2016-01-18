package br.usp.ime.mig.hubble.project;

import lombok.Data;

@Data
public class Project {

	private String id;

	private String name;

	private String description;

	// External reference, such as an unique ID or URI
	private String ref;
}
