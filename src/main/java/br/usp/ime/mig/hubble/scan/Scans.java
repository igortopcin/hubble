package br.usp.ime.mig.hubble.scan;

import java.util.List;

public interface Scans {
	List<Scan> findByExperiment(String experimentRef);

	void download(Scan scan);

	Scan findByRef(String scanRef);

}
