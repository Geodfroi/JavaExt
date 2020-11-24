package JavaExt.IO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * https://mkyong.com/java/jackson-tree-model-example/
 */
public class Settings {

    public static final String SETTINGS_FILE_NAME = "settings.json";

    static Settings instance = new Settings();

    ObjectMapper mapper = new ObjectMapper();
    Path path = Path.of(SETTINGS_FILE_NAME);

    public static Settings getInstance() {
        return instance;
    }

    public Optional<String> getStr(String propertyName) {
        try {
            ObjectNode rootNode = getRootNode();
            if (!Objects.requireNonNull(rootNode).hasNonNull(propertyName)) {
                return Optional.empty();
            }
            return Optional.of(rootNode.path(propertyName).asText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Integer> getInt(String propertyName) {
        try {
            ObjectNode rootNode = getRootNode();
            if (Objects.requireNonNull(rootNode).hasNonNull(propertyName))
                return Optional.of(rootNode.path(propertyName).asInt());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void set(String propertyName, String value) {

        try {
            ObjectNode rootNode = getRootNode();
            rootNode.put(propertyName, value);
            write(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String propertyName, int value) {

        try {
            ObjectNode rootNode = getRootNode();
            rootNode.put(propertyName, value);
            write(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMapValue(String propertyName, String key, String value) {
        try {
            ObjectNode rootNode = getRootNode();
            JsonNode mapNode = rootNode.get(propertyName);
            if (mapNode == null) {
                mapNode = mapper.createObjectNode();
                rootNode.set(propertyName, mapNode);
            }
            ((ObjectNode) mapNode).put(key, value);

            write(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMapValue(String propertyName, String key) {

        try {
            JsonNode rootNode = getRootNode();
            JsonNode mapNode = rootNode.get(propertyName);
            if (mapNode == null) {
                return null;
            }

            if (mapNode.hasNonNull(key))
                return mapNode.path(key).asText();

            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void write(JsonNode rootNode) throws IOException {
        try {
            String str = mapper.
                    writerWithDefaultPrettyPrinter().
                    writeValueAsString(rootNode);

            Path path = Path.of(SETTINGS_FILE_NAME);
            Files.writeString(path, str);
        } catch (JsonProcessingException e) {
            throw  new IOException(e.getMessage());
        }
    }

    private ObjectNode getRootNode() throws IOException {

        if (!Files.exists(path))
            Files.createFile(path);

        String str = Files.readString(path);

        JsonNode jsonNode = mapper.readTree(str);
        if (jsonNode.isMissingNode())
            jsonNode = mapper.readTree("{}");

        ObjectNode node = (ObjectNode) jsonNode;
        return Objects.requireNonNull(node);
    }


}