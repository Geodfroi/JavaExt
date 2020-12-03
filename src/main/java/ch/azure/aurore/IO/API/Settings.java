package ch.azure.aurore.IO.API;

import ch.azure.aurore.IO.SimpleJSON;

import java.util.Map;
import java.util.Optional;

/**
 * https://mkyong.com/java/jackson-tree-model-example/
 */
public class Settings {

    public static final String SETTINGS_FILE_NAME = "settings.json";

    static SimpleJSON fileAccess = new SimpleJSON(SETTINGS_FILE_NAME);

    public static Optional<String> getStr(String propertyName) {
        return fileAccess.getStr(propertyName);
    }

    public static Optional<Integer> getInt(String propertyName) {
        return fileAccess.getInt(propertyName);
    }

    public static Optional<Integer> getMapInteger(String mapName, String key) {
        return fileAccess.getMapInteger(mapName, key);
    }

    public static Optional<String> getMapStr(String mapName, String key) {
        return fileAccess.getMapStr(mapName, key);
    }

    public static Map<String, String> getMapValues(String mapName){
        return fileAccess.getMapValues(mapName);
    }
}