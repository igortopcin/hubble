package br.usp.ime.mig.hubble.galaxy.dataset;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.GalaxyInstanceFactory;
import com.github.jmchilton.blend4j.galaxy.HistoriesClient;
import com.github.jmchilton.blend4j.galaxy.beans.HistoryDetails;

/**
 * Uploads data into Galaxy
 */
public class Uploader {

	public HistoriesClient historiesClient;

	public void send(Uploadable uploadable) {

	}

//	public static void main(String[] args) {
//		GalaxyInstance galaxyInstance = GalaxyInstanceFactory.get("http://localhost:8080",
//				"2388adaf8ba990defda9bc493a8d58f8", true);
//		HistoriesClient historiesClient = galaxyInstance.getHistoriesClient();
//		// List<History> histories = historiesClient.getHistories();
//		// for (History history : histories) {
//		// System.out.println(history.getName() + " (" + history.getId() + "): "
//		// + history.getUrl());
//		// }
//		HistoryDetails historyDetails = historiesClient.showHistory("ebfb8f50c6abde6d");
//	}
}
