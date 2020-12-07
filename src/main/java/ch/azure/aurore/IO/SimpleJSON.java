package ch.azure.aurore.IO;

import ch.azure.aurore.Strings.Strings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

public class SimpleJSON {
    private final ObjectMapper mapper = new ObjectMapper();

    private ObjectNode getRootNode(String txt) {
        JsonNode jsonNode = null;
        try {
            if (!Strings.isNullOrEmpty(txt)) {
                jsonNode = mapper.readTree(txt);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (jsonNode == null || jsonNode.isMissingNode()) {
            try {
                jsonNode = mapper.readTree("{}");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return (ObjectNode) jsonNode;
    }

    public Optional<Boolean> getBoolean(String valueName, String txt) {

        JsonNode rootNode = getRootNode(txt);
        if (rootNode.hasNonNull(valueName))
            return Optional.of(rootNode.path(valueName).asBoolean());

        return Optional.empty();
    }

    public Optional<Double> getDouble(String valueName, String txt) {

        JsonNode rootNode = getRootNode(txt);
        if (rootNode.hasNonNull(valueName))
            return Optional.of(rootNode.path(valueName).asDouble());

        return Optional.empty();
    }

    public Optional<List<Double>> getDoubles(String propertyName, String txt) {
        ObjectNode rootNode = getRootNode(txt);

        if (rootNode.has(propertyName)) {
            List<Double> list = new ArrayList<>();
            JsonNode node = rootNode.path(propertyName);
            if (node.isArray()) {
                for (var n = 0; n < node.size(); n++) {
                    double value = node.get(n).asDouble();
                    list.add(value);
                }
            }
            return Optional.of(list);
        }
        return Optional.empty();
    }

    public Optional<Integer> getInt(String valueName, String txt) {

        JsonNode rootNode = getRootNode(txt);
        if (rootNode.hasNonNull(valueName))
            return Optional.of(rootNode.path(valueName).asInt());

        return Optional.empty();
    }

    public Optional<List<Integer>> getInts(String propertyName, String txt) {
        ObjectNode rootNode = getRootNode(txt);

        if (rootNode.has(propertyName)) {
            List<Integer> list = new ArrayList<>();
            JsonNode node = rootNode.path(propertyName);
            if (node.isArray()) {
                for (int n = 0; n < node.size(); n++) {
                    int value = node.get(n).asInt();
                    list.add(value);
                }
            }
            return Optional.of(list);
        }
        return Optional.empty();
    }


    private JsonNode getMapNode_read(String mapName, String key, String txt) {

        JsonNode rootNode = getRootNode(txt);

        if (rootNode.hasNonNull(mapName)) {
            JsonNode mapNode = rootNode.get(mapName);
            if (mapNode.hasNonNull(key))
                return mapNode.path(key);
        }

        return null;
    }

    public Optional<Integer> getMapInteger(String mapName, String key, String txt) {
        JsonNode node = getMapNode_read(mapName, key, txt);
        if (node != null)
            return Optional.of(node.asInt());

        return Optional.empty();
    }

    public Optional<Integer> getMapInteger(String mapName, int key, String txt) {
        return getMapInteger(mapName, Integer.toString(key),txt);
    }

    public Optional<String> getMapString(String mapName, String key, String txt) {
        JsonNode node = getMapNode_read(mapName, key, txt);
        if (node != null)
            return Optional.of(node.asText());

        return Optional.empty();
    }

    public Optional<String> getMapString(String mapName, int key, String txt) {
        return getMapString(mapName,Integer.toString(key), txt);
    }

    public Map<String, String> getMapValues(String mapName, String txt) {

        HashMap<String, String> map = new HashMap<>();

        JsonNode rootNode = getRootNode(txt);
        if (rootNode.hasNonNull(mapName)) {
            JsonNode mapNode = rootNode.get(mapName);

            Iterator<String> iterator = mapNode.fieldNames();
            while (iterator.hasNext()) {
                String field = iterator.next();
                String value = mapNode.get(field).asText();
                if (value.equals("null"))
                    value = null;

                map.put(field, value);
            }
        }
        return map;
    }


    public Optional<String> getString(String valueName, String txt) {

        JsonNode rootNode = getRootNode(txt);
        if (Objects.requireNonNull(rootNode).hasNonNull(valueName))
            return Optional.of(rootNode.path(valueName).asText());

        return Optional.empty();
    }

    public Optional<List<String>> getStrings(String propertyName, String txt) {
        ObjectNode rootNode = getRootNode(txt);
        if (rootNode.has(propertyName)) {
            List<String> list = new ArrayList<>();
            JsonNode node = rootNode.path(propertyName);
            if (node.isArray()) {
                for (int n = 0; n < node.size(); n++) {
                    String value = node.get(n).asText();
                    list.add(value);
                }
            }
            return Optional.of(list);
        }
        return Optional.empty();
    }

    public Optional<String> setBoolean(String valueName, Boolean value, String txt) {
        ObjectNode rootNode = getRootNode(txt);
        rootNode.put(valueName, value);
        return write(rootNode);
    }

    public Optional<String> setDouble(String valueName, double value, String txt) {
        ObjectNode rootNode = getRootNode(txt);
        rootNode.put(valueName, value);
        return write(rootNode);
    }

    public Optional<String> setDoubles(String propertyName, List<Double> list, String txt) {
        ObjectNode rootNode = getRootNode(txt);
        if (list == null || list.size() == 0) {
            rootNode.remove(propertyName);
        } else {
            ArrayNode node = mapper.createArrayNode();
            for (Double i : list) {
                node.add(i);
            }
            rootNode.set(propertyName, node);
        }
        return write(rootNode);
    }

    public Optional<String> setInt(String valueName, int value, String txt) {
        ObjectNode rootNode = getRootNode(txt);
        rootNode.put(valueName, value);
        return write(rootNode);
    }

    public Optional<String> setInts(String propertyName, List<Integer> list, String txt) {
        ObjectNode rootNode = getRootNode(txt);
        if (list == null || list.size() == 0) {
            rootNode.remove(propertyName);
        } else {
            ArrayNode node = mapper.createArrayNode();
            for (Integer i : list) {
                node.add(i);
            }
            rootNode.set(propertyName, node);
        }
        return write(rootNode);
    }

    public Optional<String> setMapValue(String mapName, String key, int value, String txt) {

        ObjectNode rootNode = getRootNode(txt);
        if (!rootNode.hasNonNull(mapName))
            rootNode.set(mapName, mapper.createObjectNode());

        JsonNode mapNode = rootNode.get(mapName);
        ((ObjectNode) mapNode).put(key, value);

        return write(rootNode);
    }

    public Optional<String> setMapValue(String mapName, int key, String value, String txt) {
        return setMapValue(mapName, Integer.toString(key), value, txt);
    }

    public Optional<String> setMapValue(String mapName, int key, int value, String txt) {
        return setMapValue(mapName, Integer.toString(key), value, txt);
    }

    public Optional<String> setMapValue(String mapName, String key, String value, String txt) {

        ObjectNode rootNode = getRootNode(txt);
        if (!rootNode.hasNonNull(mapName))
            rootNode.set(mapName, mapper.createObjectNode());

        ObjectNode mapNode = (ObjectNode) rootNode.get(mapName);
        mapNode.put(key, value);

        return write(rootNode);
    }

    public Optional<String> setString(String valueName, String value, String txt) {
        ObjectNode rootNode = getRootNode(txt);
        rootNode.put(valueName, value);
        return write(rootNode);
    }

    public Optional<String> setStrings(String propertyName, List<String> list, String str) {

        var rootNode = (ObjectNode) getRootNode(str);
        if (list == null || list.size() == 0) {
            rootNode.remove(propertyName);
        } else {
            ArrayNode node = mapper.createArrayNode();
            for (String s : list) {
                node.add(s);
            }
            rootNode.set(propertyName, node);
        }
        return write(rootNode);
    }

    private Optional<String> write(JsonNode rootNode) {
        try {
            return Optional.of(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}