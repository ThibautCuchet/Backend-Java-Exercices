package be.vinci.pae.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import be.vinci.pae.domain.Dactylo;

public class DataServiceDactyloCollection {
	private static final String DB_FILE_PATH = "db.json";
	private static final String COLLECTION_NAME = "dactylo";
	private final static ObjectMapper jsonMapper = new ObjectMapper();

	private static List<Dactylo> dactylos ;
	static {
		dactylos = loadDataFromFile();
	}	

	public static Dactylo getDactylo(int id) {
		return dactylos.stream().filter(item -> item.getId() == id).findAny().orElse(null);
	}

	public static List<Dactylo> getDactylos() {
		return dactylos;
	}

	public static List<Dactylo> getDactylos(String level) {
		return dactylos.stream().filter(item -> item.getLevel().equals(level)).collect(Collectors.toList());
	}

	public static Dactylo addDactylo(Dactylo dactylo) {
		dactylo.setId(nextDactyloId());
		// escape dangerous chars to protect against XSS attacks
		dactylo.setContent(StringEscapeUtils.escapeHtml4(dactylo.getContent()));
		dactylo.setLevel(StringEscapeUtils.escapeHtml4(dactylo.getLevel()));
		dactylos.add(dactylo);
		saveDataToFile();
		return dactylo;
	}

	public static int nextDactyloId() {
		if (dactylos.size() == 0)
			return 1;
		return dactylos.get(dactylos.size() - 1).getId() + 1;
	}

	public static Dactylo deleteDactylo(int id) {
		if (dactylos.size() == 0 | id == 0)
			return null;
		Dactylo dactyloToDelete = getDactylo(id);
		if (dactyloToDelete == null)
			return null;
		int index = dactylos.indexOf(dactyloToDelete);
		dactylos.remove(index);
		saveDataToFile();
		return dactyloToDelete;
	}

	public static Dactylo updateDactylo(Dactylo dactylo) {
		if (dactylos.size() == 0 | dactylo == null)
			return null;
		Dactylo dactyloToUpdate = getDactylo(dactylo.getId());
		if (dactyloToUpdate == null)
			return null;
		// escape dangerous chars to protect against XSS attacks
		dactylo.setContent(StringEscapeUtils.escapeHtml4(dactylo.getContent()));
		dactylo.setLevel(StringEscapeUtils.escapeHtml4(dactylo.getLevel()));
		// update the data structure
		int index = dactylos.indexOf(dactyloToUpdate);
		dactylos.set(index, dactylo);
		saveDataToFile();
		return dactylo;
	}

	private static List<Dactylo> loadDataFromFile() {
		try {
			JsonNode node = jsonMapper.readTree(Paths.get(DB_FILE_PATH).toFile());
			JsonNode collection = node.get(COLLECTION_NAME);
			if (collection == null)
				return new ArrayList<Dactylo>();
			return jsonMapper.readerForListOf(Dactylo.class).readValue(node.get(COLLECTION_NAME));

		} catch (FileNotFoundException e) {
			return new ArrayList<Dactylo>();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Dactylo>();
		}
	}

	private static void saveDataToFile() {
		try {

			// get all collections
			Path pathToDb = Paths.get(DB_FILE_PATH);
			if (!Files.exists(pathToDb)) {
				// write a new collection to the db file
				ObjectNode newCollection = jsonMapper.createObjectNode().putPOJO(COLLECTION_NAME, dactylos);
				jsonMapper.writeValue(pathToDb.toFile(), newCollection);
				return;

			}
			// get all collections
			JsonNode allCollections = jsonMapper.readTree(pathToDb.toFile());

			if (allCollections.has(COLLECTION_NAME)) {// remove current collection
				((ObjectNode) allCollections).remove(COLLECTION_NAME);
			}

			// create a new JsonNode and add it to allCollections
			String currentCollectionAsString = jsonMapper.writeValueAsString(dactylos);
			JsonNode updatedCollection = jsonMapper.readTree(currentCollectionAsString);
			((ObjectNode) allCollections).putPOJO(COLLECTION_NAME, updatedCollection);

			// write to the db file
			jsonMapper.writeValue(pathToDb.toFile(), allCollections);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
