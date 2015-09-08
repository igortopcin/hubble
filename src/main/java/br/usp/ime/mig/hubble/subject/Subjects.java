package br.usp.ime.mig.hubble.subject;

import java.util.List;

public interface Subjects {
	List<Subject> findByProject(String projectId);
}
