package br.usp.ime.mig.hubble.xnat.scan;

import br.usp.ime.mig.hubble.scan.Scan;

import com.google.common.base.Function;

public class XNATScanConverter implements Function<ScanApiResult, Scan> {

	@Override
	public Scan apply(ScanApiResult r) {
		Scan scan = new Scan();

		scan.setId(r.getId());
		scan.setQuality(r.getQuality());
		scan.setFileType(r.getLabel());
		scan.setSeriesDescription(r.getSeriesDescription());
		scan.setFileCount(r.getFileCount());
		scan.setFileSize(r.getFileSize());
		scan.setRef(r.getUri());

		return scan;
	}

}
