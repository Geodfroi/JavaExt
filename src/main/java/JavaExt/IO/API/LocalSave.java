package JavaExt.IO.API;

import JavaExt.IO.SimpleJSON;

import java.util.Optional;

public class LocalSave {

    public static final String SETTINGS_FILE_NAME = "local.json";

    static SimpleJSON fileAccess = new SimpleJSON(SETTINGS_FILE_NAME);
    public static Optional<String> getStr(String propertyName) {
        return fileAccess.getStr(propertyName);
    }

    public static void set(String propertyName, String value) {
        fileAccess.set(propertyName, value);
    }

    public static Optional<Integer> getInt(String propertyName) {
        return fileAccess.getInt(propertyName);
    }


    public static void set(String propertyName, int value) {
        fileAccess.set(propertyName, value);
    }

    public static String getMapValue(String propertyName, String key) {
        return fileAccess.getMapValue(propertyName, key);
    }

    public static void setMapValue(String propertyName, String key, String value) {
        fileAccess.setMapValue(propertyName, key , value);
    }
}
