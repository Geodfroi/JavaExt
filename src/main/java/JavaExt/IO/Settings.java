package JavaExt.IO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//https://mkyong.com/java/jackson-tree-model-example/
public class Settings {

    public static final String SETTINGS_FILE_NAME = "settings.json";

    static Settings instance = new Settings();

    ObjectMapper objectMapper = new ObjectMapper();
    Path path = Path.of(SETTINGS_FILE_NAME);

    public static Settings getInstance() {
        return instance;
    }

    public String get(String propertyName) {
        JsonNode rootNode = getRootNode();
        if (rootNode.hasNonNull(propertyName))
            return rootNode.path(propertyName).asText();

        return null;
    }

    public void set(String propertyName, String value) {

        JsonNode rootNode = getRootNode();
        ((ObjectNode) rootNode).put(propertyName, value);

        try {
            String str = objectMapper.
                    writerWithDefaultPrettyPrinter().
                    writeValueAsString(rootNode);

            Path path = Path.of(SETTINGS_FILE_NAME);
            Files.writeString(path, str);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("error writing to settings.json file: " + e.getMessage());
        }
    }

    private JsonNode getRootNode() {

        try {
            if (!Files.exists(path))
                Files.createFile(path);

            String str = Files.readString(path);

            JsonNode jsonNode = objectMapper.readTree(str);
            if (jsonNode.isMissingNode())
                jsonNode = objectMapper.readTree("{}");

            return jsonNode;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}


//
//    public void setEntry(String mapPropertyName, String key, String value){
//        Map<String, String> dddd = new HashMap<>();
//        dddd.put(key, value);
//
////        obj.put(dddd);
//    }
//
//    public String getEntry(String mapPropertyName, String key){
//        return null;
//    }
//}