package sg.edu.nus.iss.ssf_dec2023.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.ssf_dec2023.model.Event;
import sg.edu.nus.iss.ssf_dec2023.repository.ListRepo;


@Service
public class databaseService {
    
    @Autowired
    private ListRepo listRepo;

    @Autowired
	private ResourceLoader resourceLoader;

    public List<Event> readFile(String fileName)
    {
        Resource resource = resourceLoader.getResource("classpath:data/" + fileName);
        try(InputStream is = resource.getInputStream())
		{
			// Step 2: Parse the file content using JSON-P
			
			// prepatory step, initiliases JsonReader w InputStream as its input. Doesn't process immediately, jsut a setup step that tells the library, "I'll process this stream as JSON soon."
			JsonReader jsonReader = Json.createReader(is); 
			
			// Specifying how to interpret the JSON content (in this case, as a JSON array)
			JsonArray todosArray = jsonReader.readArray();


			// Check if the array is empty
            if (todosArray.isEmpty()) {
                // System.out.println("No todos found in the file.");
                return;
            }

			

			// Step 3: Store the data in Redis using MapRepo
			for (JsonObject todo : todosArray.getValuesAs(JsonObject.class)) // getValuesAs converts each element in the JsonArray into a JsonObject
			{
				String id = todo.getString("id");
				


				// Check if the key already exists in Redis
                if (listRepo.get(Constant.todoKey, id) != null) 
				{
					// System.out.println("Todo with ID " + id + " already exists. Skipping.");
                    logger.info("Todo with ID {} already exists. Skipping.", id);
					
                    continue;
                }


				// Add to Redis if it doesn't exist
				mapRepo.put(Constant.todoKey, id, todo.toString());
				// System.out.println("Added todo with ID " + id + " to Redis.");
				logger.debug("Added todo with ID {} to Redis.", id);


			}

			// Log success
			// System.out.println("Todos have been loaded into Redis.");
			logger.info("Todos have been successfully loaded into Redis.");
		} 
		catch(Exception e)
		{
			// Log the error and prevent the application from crashing
			// System.err.println("Error occured while processing todos.txt: " + e.getMessage());
			// e.printStackTrace();

			logger.error("Error occurred while processing todos.txt: {}", e.getMessage(), e);
		}
    }
}
