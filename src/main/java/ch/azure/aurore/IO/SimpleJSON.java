package ch.azure.aurore.IO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SimpleJSON {

    ObjectMapper mapper = new ObjectMapper();
    Path path;

    public SimpleJSON(String settingsFileName) {
        path = Path.of(settingsFileName);
    }

    public void clear() {
        try {
            Files.writeString(path, "{}");
        } catch (IOException e) {
            e.printStackTrace();
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

    public void setMapValue(String mapName, String key, int value) {
        try {
            ObjectNode rootNode = getRootNode();
            if (!rootNode.hasNonNull(mapName))
                rootNode.set(mapName, mapper.createObjectNode());

            JsonNode mapNode = rootNode.get(mapName);
            ((ObjectNode) mapNode).put(key, value);

            write(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMapValue(String mapName, String key, String value) {

        try {
            ObjectNode rootNode = getRootNode();
            if (!rootNode.hasNonNull(mapName))
                rootNode.set(mapName, mapper.createObjectNode());

            ObjectNode mapNode = (ObjectNode)rootNode.get(mapName);
            mapNode.put(key, value);

            write(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonNode getMapNode_read(String mapName, String key) {
        try {
            ObjectNode rootNode = getRootNode();

            if (rootNode.hasNonNull(mapName)) {
                JsonNode mapNode = rootNode.get(mapName);
                if (mapNode.hasNonNull(key))
                    return mapNode.path(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<Integer> getMapInteger(String mapName, String key) {
        JsonNode node = getMapNode_read(mapName, key);
        if (node != null)
            return Optional.of(node.asInt());

        return Optional.empty();
    }

    public Optional<String> getMapStr(String mapName, String key) {
        JsonNode node = getMapNode_read(mapName, key);
        if (node != null)
            return Optional.of(node.asText());

        return Optional.empty();
    }

    public Map<String, String> getMapValues(String mapName) {

        HashMap<String,String> map = new HashMap<>();
        try {
            JsonNode rootNode = getRootNode();
            if (rootNode.hasNonNull(mapName)){
                JsonNode mapNode = rootNode.get(mapName);

                Iterator<String> iterator = mapNode.fieldNames();
                while (iterator.hasNext()){
                    String field = iterator.next();
                    String value = mapNode.get(field).asText();
                    if (value.equals("null"))
                        value = null;

                    map.put(field, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
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
