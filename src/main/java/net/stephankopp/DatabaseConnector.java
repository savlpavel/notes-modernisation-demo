package net.stephankopp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class DatabaseConnector {
	
	private List<DocumentData> cachedData;

	/**
	 * Constructor
	 */
	public DatabaseConnector() {
		//createSampleData();
		readDominoData();
	}
	
	/**
	 * Create sample data
	 */
	private void createSampleData() {
		cachedData = new ArrayList<DocumentData>();
		for (int i=1; i<=100; i++) {
			DocumentData data = new DocumentData();
			data.setStatus("OPEN");
			data.setDescription("Task " + i);
			data.setDueDate(LocalDate.now().plusDays(i));
			data.setTopic("Project " + i);
			cachedData.add(data);
		}
	}
	
	/**
	 * Deletes the given documentData object
	 * @param documentData
	 */
	public void delete(DocumentData documentData) {
		//Remove the object from the cachedData list
		cachedData.remove(documentData);
	}
	
	/**
	 * Adds the given documentData object
	 * @param documentData
	 */
	public void add(DocumentData documentData) {
		//add the given DocumentData to the cachedData list
		cachedData.add(documentData);
		
		//create the document also in the Domino database
		save(documentData);
	}
	
	/**
	 * Save changes to the Domino Database. This method can also be used to create documents. 
	 * For new documents, the UniversalID property is null and will be omitted, which means for the api to create a new document.
	 * @param doc
	 */
	public void save(DocumentData doc) {
		String url = "http://10.211.55.6/demo/tasks.nsf/api.xsp/updateData"; //TODO: Change URL to your domino server
		
		//Create a json object which will be sent to the REST api
		ObjectNode json = JsonNodeFactory.instance.objectNode();
		
		//Add all DocumentData details to your JSON object
		if (doc.getUniversalId() != null) json.put("universalID", doc.getUniversalId());
		if (doc.getStatus() != null) json.put("status", doc.getStatus());
		if (doc.getTopic() != null) json.put("topic", doc.getTopic());
		if (doc.getDescription() != null) json.put("description", doc.getDescription());
		if (doc.getDueDate() != null) {
			//Due date has to be converted to the string format expected by the rest api ("02/22/2017")
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			json.put("dueDate", doc.getDueDate().format(formatter));
		}
		
		//Send the json object to the REST api
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(url, json, String.class);
		
		//TODO: Check the response and show an appropriate message to the user
	}
	
	/**
	 * Read all tasks from the Domino Database and write it to cachedData list
	 */
	private void readDominoData() {
		//TODO: Add errorhandling and do other useful validations
		
		//Create an empty list
		cachedData = new ArrayList<DocumentData>();
		
		//Read JSON from domino REST api
		String url = "http://10.211.55.6/demo/tasks.nsf/api.xsp/readData"; //TODO: Change URL to your domino server

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

		try {
			//Get the returned JsonNode object
			JsonNode json = response.getBody();
			
			//Check if the api returned a Json array
			if (json.isArray()) {
				//Loop through JSONArray
				for(int i=0; i< json.size(); i++) {
					//Extract each individual JSON Object representing on of our task documents
					JsonNode json_task = json.get(i);
					
					//Create DocumentData objects
					DocumentData document = new DocumentData();
					if (json_task.has("universalID")) document.setUniversalId(json_task.get("universalID").asText());
					if (json_task.has("status")) document.setStatus(json_task.get("status").asText());
					if (json_task.has("topic")) document.setTopic(json_task.get("topic").asText());
					if (json_task.has("description")) document.setDescription(json_task.get("description").asText());
					
					//we get the due date as pure text, so we have to convert it to a LocalDate object
					if (json_task.has("dueDate")) {
						String dueDateString = json_task.get("dueDate").asText();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
						document.setDueDate(LocalDate.parse(dueDateString, formatter));
					}
					
					//Add DocumentData to the cache
					cachedData.add(document);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<DocumentData> getCachedData() {
		return cachedData;
	}

	public void setCachedData(List<DocumentData> cachedData) {
		this.cachedData = cachedData;
	}
	
}
