package br.usp.ime.mig.hubble.galaxy.dataset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.usp.ime.mig.hubble.galaxy.GalaxyContext;

@Component
public class FileSenderFactory {
	private final GalaxyContext context;

	@Autowired
	public FileSenderFactory(GalaxyContext context) {
		this.context = context;
	}

	public FileSender create() {
		return new FileSender(context.getGalaxyInstance());
	}
}
