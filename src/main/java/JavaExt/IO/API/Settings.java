package JavaExt.IO.API;

import JavaExt.IO.SimpleJSON;

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

    public static String getMapValue(String propertyName, String key) {
        return fileAccess.getMapValue(propertyName, key);
    }
}