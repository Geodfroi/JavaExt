package ch.azure.aurore.IO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class SimpleJSON {

    ObjectMapper mapper = new ObjectMapper();
    Path path;

    public SimpleJSON(String settingsFileName) {
        path = Path.of(settingsFileName);
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

    public Optional<Boolean> getBoolean(String valueName) {
        try {
            ObjectNode rootNode = getRootNode();
            if (Objects.requireNonNull(rootNode).hasNonNull(valueName))
                return Optional.of(rootNode.path(valueName).asBoolean());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Integer> getInt(String valueName) {
        try {
            ObjectNode rootNode = getRootNode();
            if (Objects.requireNonNull(rootNode).hasNonNull(valueName))
                return Optional.of(rootNode.path(valueName).asInt());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<String> getStr(String valueName) {
        try {
            ObjectNode rootNode = getRootNode();
            if (Objects.requireNonNull(rootNode).hasNonNull(valueName))
                return Optional.of(rootNode.path(valueName).asText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void set(String valueName, String value) {

        try {
            ObjectNode rootNode = getRootNode();
            rootNode.put(valueName, value);
            write(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String valueName, int value) {
        try {
            ObjectNode rootNode = getRootNode();
            rootNode.put(valueName, value);
            write(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String valueName, Boolean value){
        try {
            ObjectNode rootNode = getRootNode();
            rootNode.put(valueName, value);
            write(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMapValue(String valueName, String key, String value) {
        try {
            ObjectNode rootNode = getRootNode();
            JsonNode mapNode = rootNode.get(valueName);
            if (mapNode == null) {
                mapNode = mapper.createObjectNode();
                rootNode.set(valueName, mapNode);
            }
            ((ObjectNode) mapNode).put(key, value);

            write(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMapValue(String valueName, String key) {

        try {
            JsonNode rootNode = getRootNode();
            JsonNode mapNode = rootNode.get(valueName);
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

            Files.writeString(path, str);
        } catch (JsonProcessingException e) {
            throw  new IOException(e.getMessage());
        }
    }


}
