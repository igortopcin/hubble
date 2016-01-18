package br.usp.ime.mig.hubble.galaxy.dataset;

import br.usp.ime.mig.hubble.externalcontent.ContentHandler;

public interface Uploadable {
	String getName();

	String getRef();

	ContentHandler getContentHandler();
}
