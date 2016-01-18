package br.usp.ime.mig.hubble.subject;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Subject {

	private String id;

	private String projectId;

	private LocalDateTime insertDate;

	private String label;

	// External reference, such as an unique ID or URI
	private String ref;
}
