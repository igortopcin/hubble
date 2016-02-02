package br.usp.ime.mig.hubble.scan;

import java.util.List;
import java.util.Optional;

public interface Scans {
	List<Scan> findByExperiment(String experimentRef);

	Optional<Scan> findByRef(String scanRef);
}
