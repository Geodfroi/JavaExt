package ch.azure.aurore.IO.jsonFiles;

import ch.azure.aurore.IO.SimpleJSON;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReadOnlyJSONFile {

    protected final Path path;
    protected String txt;

    SimpleJSON JSONFile = new SimpleJSON();

    public ReadOnlyJSONFile(String fileName) {
        path = Path.of(fileName);
    }

    public Optional<Double> getDouble(String propertyName) {
        return JSONFile.getDouble(propertyName, getText());
    }

    public Optional<List<Double>> getDoubles(String propertyName){
        return JSONFile.getDoubles(propertyName, getText());
    }

    public Optional<Integer> getInteger(String propertyName) {
        return JSONFile.getInt(propertyName, getText());
    }

    public Optional<List<Integer>> getIntegers(String propertyName){
        return JSONFile.getInts(propertyName, getText());
    }

    public Optional<String> getString(String propertyName) {
        return JSONFile.getString(propertyName, getText());
    }

    public Optional<List<String>> getStrings(String propertyName) {
        return JSONFile.getStrings(propertyName, getText());
    }

    public Optional<Boolean> getBoolean(String propertyName){
        return JSONFile.getBoolean(propertyName, getText());
    }

    public Optional<Integer> getMapInteger(String mapName, String key){
        return JSONFile.getMapInteger(mapName, key, getText());
    }

    public Optional<Integer> getMapInteger(String mapName, int key){
        return JSONFile.getMapInteger(mapName, key, getText());
    }

    public Optional<String> getMapString(String mapName, int key) {
        return JSONFile.getMapString(mapName, key, getText());
    }

    public Optional<String> getMapString(String mapName, String key) {
        return JSONFile.getMapString(mapName, key, getText());
    }

    public Map<String, String> getMapValues(String propertyName){
        return JSONFile.getMapValues(propertyName, getText());
    }

    String getText(){
        if (txt == null) {
            txt = readFileText();
        }
        return txt;
    }

    private String readFileText()
    {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}