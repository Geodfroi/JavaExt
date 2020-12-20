package ch.azure.aurore.json.jsonFiles;

import ch.azure.aurore.json.SimpleJSON;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReadOnlyJSONFile {

    protected String txt;

    SimpleJSON JSONFile = new SimpleJSON();

    public ReadOnlyJSONFile(String txt) {
        this.txt = txt;
    }

    public Optional<Double> getDouble(String propertyName) {
        return JSONFile.getDouble(propertyName, txt);
    }

    public Optional<List<Double>> getDoubles(String propertyName){
        return JSONFile.getDoubles(propertyName, txt);
    }

    public Optional<Integer> getInteger(String propertyName) {
        return JSONFile.getInt(propertyName, txt);
    }

    public Optional<List<Integer>> getIntegers(String propertyName){
        return JSONFile.getInts(propertyName, txt);
    }

    public Optional<String> getString(String propertyName) {
        return JSONFile.getString(propertyName, txt);
    }

    public Optional<List<String>> getStrings(String propertyName) {
        return JSONFile.getStrings(propertyName, txt);
    }

    public Optional<Boolean> getBoolean(String propertyName){
        return JSONFile.getBoolean(propertyName, txt);
    }

    public Optional<Integer> getMapInteger(String mapName, String key){
        return JSONFile.getMapInteger(mapName, key, txt);
    }

    public Optional<Integer> getMapInteger(String mapName, int key){
        return JSONFile.getMapInteger(mapName, key, txt);
    }

    public Optional<String> getMapString(String mapName, int key) {
        return JSONFile.getMapString(mapName, key, txt);
    }

    public Optional<String> getMapString(String mapName, String key) {
        return JSONFile.getMapString(mapName, key, txt);
    }

    public Map<String, String> getMapValues(String propertyName){
        return JSONFile.getMapValues(propertyName, txt);
    }
}