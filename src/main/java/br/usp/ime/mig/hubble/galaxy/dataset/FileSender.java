package br.usp.ime.mig.hubble.galaxy.dataset;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.ToolsClient.FileUploadRequest;
import com.github.jmchilton.blend4j.galaxy.beans.OutputDataset;
import com.github.jmchilton.blend4j.galaxy.beans.ToolExecution;
import com.github.jmchilton.blend4j.galaxy.beans.collection.request.CollectionDescription;
import com.github.jmchilton.blend4j.galaxy.beans.collection.request.HistoryDatasetElement;
import com.github.jmchilton.blend4j.galaxy.beans.collection.response.CollectionResponse;
import com.sun.jersey.api.client.ClientResponse;

@Slf4j
public class FileSender {

	private static final String DICOM_FILE_TYPE = "dcm.zip";
	private static final String NIFTI_FILE_TYPE = "nii.gz";

	private final GalaxyInstance galaxyInstance;

	public FileSender(GalaxyInstance galaxyInstance) {
		this.galaxyInstance = galaxyInstance;
	}

	/**
	 * Send a file to Galaxy and hides it.
	 * 
	 * @param file
	 *            The file to be sent.
	 * @param uploadable
	 *            the {@link Uploadable} instance related to the file being
	 *            sent.
	 * @return The id of Galaxy's created dataset. {@link Optional#empty()} is
	 *         returned if no file was sent.
	 */
	public Optional<UploadedFile> send(Path file, Uploadable uploadable, String historyId) {
		Optional<String> datasetId = uploadFile(file, uploadable, historyId);
		if (!datasetId.isPresent()) {
			log.error("Could not upload {}", file);
			return Optional.empty();
		}

		hideDataset(historyId, datasetId.get());
		return Optional.of(new UploadedFile(
				uploadable,
				datasetId.get(),
				historyId));
	}

	public Optional<String> createCollection(List<UploadedFile> files) {
		if (files.isEmpty()) {
			log.error("Could not create a dataset collection: no files selected");
			return Optional.empty();
		}

		CollectionDescription collectionRequest = new CollectionDescription();
		collectionRequest.setName(String.format("Collection of %s files", files.size()));

		files.stream().forEach(f -> {
			HistoryDatasetElement element = new HistoryDatasetElement();
			element.setId(f.getDatasetId());
			element.setName(generateDatasetName(f.getUploadable()));
			collectionRequest.addDatasetElement(element);
		});

		CollectionResponse response = galaxyInstance.getHistoriesClient()
				.createDatasetCollection(files.get(0).getHistoryId(), collectionRequest);
		if (response == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(response.getHistoryId());
	}

	private Optional<String> uploadFile(Path file, Uploadable uploadable, String historyId) {
		FileUploadRequest request = new FileUploadRequest(historyId, file.toFile());
		String fileType = guessFileType(file);
		request.setDatasetName(generateDatasetName(uploadable));
		request.setFileType(fileType);
		ToolExecution exec = galaxyInstance.getToolsClient().upload(request);
		
		if (exec != null) {
			Optional<OutputDataset> result = exec.getOutputs().stream().findFirst();
			if (result.isPresent()) {
				return Optional.of(result.get().getId());
			}
		}
		return Optional.empty();
	}

	private String guessFileType(Path file) {
		if (file.getFileName().toString().endsWith(NIFTI_FILE_TYPE)) {
			return NIFTI_FILE_TYPE;
		}
		return DICOM_FILE_TYPE;
	}

	private String generateDatasetName(Uploadable uploadable) {
		return String.format("%s - %s/%s",
				uploadable.getSubjectLabel(),
				uploadable.getExperimentLabel(),
				uploadable.getScanLabel());
	}

	private void hideDataset(String historyId, String datasetId) {
		galaxyInstance.getWebResource()
				.path("histories/" + historyId + "/contents/" + datasetId)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, "{\"visible\": false}");
	}

	@Data
	@AllArgsConstructor
	public static class UploadedFile {
		private Uploadable uploadable;
		private String datasetId;
		private String historyId;
	}
}
