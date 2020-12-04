package ch.azure.aurore.IO.API;

import ch.azure.aurore.IO.SimpleJSON;

import java.util.Map;
import java.util.Optional;

public class LocalSave {

    public static final String SETTINGS_FILE_NAME = "local.json";

    static SimpleJSON fileAccess = new SimpleJSON(SETTINGS_FILE_NAME);

    public static void clear()
    {
        fileAccess.clear();
    }

    public static Optional<Integer> getInt(String valueName) {
        return fileAccess.getInt(valueName);
    }
    
    public static Optional<String> getStr(String valueName) {
        return fileAccess.getStr(valueName);
    }
    
    public static Optional<Boolean> getBoolean(String valueName){
        return fileAccess.getBoolean(valueName);
    }

    public static Optional<Integer> getMapInteger(String mapName, String key){
        return fileAccess.getMapInteger(mapName, key);
    }

    public static Optional<String> getMapString(String mapName, String key) {
        return fileAccess.getMapStr(mapName, key);
    }

    public static Map<String, String> getMapValues(String valueName){
        return fileAccess.getMapValues(valueName);
    }
    
    public static void set(String valueName, int value) {
        fileAccess.set(valueName, value);
    }

    public static void set(String valueName, boolean value) {
        fileAccess.set(valueName, value);
    }

    public static void set(String valueName, String value) {
        fileAccess.set(valueName, value);
    }

    public static void setMapValue(String valueName, String key, String value) {
        fileAccess.setMapValue(valueName, key , value);
    }

    public static void setMapValue(String valueName, String key, int value) {
        fileAccess.setMapValue(valueName, key , value);
    }
}
