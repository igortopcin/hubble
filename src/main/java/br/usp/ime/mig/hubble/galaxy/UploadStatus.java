package br.usp.ime.mig.hubble.galaxy;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import br.usp.ime.mig.hubble.galaxy.dataset.Uploadable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Data
public class UploadStatus {
	private boolean uploadInProgress;

	@JsonIgnore
	private List<Uploadable> uploadables = new ArrayList<>();

	public List<String> getRemainingRefs() {
		return Lists.transform(uploadables, Uploadable::getRef);
	}
}
