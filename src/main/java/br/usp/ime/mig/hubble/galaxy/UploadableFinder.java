package br.usp.ime.mig.hubble.galaxy;

import java.util.Optional;

import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;
import br.usp.ime.mig.hubble.galaxy.dataset.UploadableType;

public interface UploadableFinder {
	Optional<? extends Uploadable> findByRef(String ref);
	UploadableType getUploadableType();
}
