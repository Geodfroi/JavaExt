package ch.azure.aurore.IO.jsonFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class JSONFile extends ReadOnlyJSONFile {

    public JSONFile(String fileName) {
        super(fileName);
    }

    public void clear() {
        try {
            Files.writeString(path, "{}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String propertyName, int value) {
        Optional<String> text = JSONFile.setInt(propertyName, value, getText());
        text.ifPresent(this::setText);
    }

    public void set(String propertyName, double value){
        Optional<String> text = JSONFile.setDouble(propertyName, value, getText());
        text.ifPresent(this::setText);
    }

    public void set(String propertyName, boolean value) {
        Optional<String> text = JSONFile.setBoolean(propertyName, value, getText());
        text.ifPresent(this::setText);
    }

    public void set(String propertyName, String value) {
        Optional<String> text = JSONFile.setString(propertyName, value, getText());
        text.ifPresent(this::setText);
    }

    public void set(String propertyName, List<Integer> list) {
        Optional<String> text = JSONFile.setInts(propertyName, list, getText());
        text.ifPresent(this::setText);
    }

    public void setMapValue(String propertyName, String key, String value) {
        Optional<String> text = JSONFile.setMapValue(propertyName, key , value, getText());
        text.ifPresent(this::setText);
    }

    public void setMapValue(String propertyName, String key, int value) {
        Optional<String> text = JSONFile.setMapValue(propertyName, key , value, getText());
        text.ifPresent(this::setText);
    }

    public void setDoubles(String propertyName, List<Double> list) {
        Optional<String> text = JSONFile.setDoubles(propertyName, list, getText());
        text.ifPresent(this::setText);
    }

    public void setIntegers(String propertyName, List<Integer> list) {
        Optional<String> text = JSONFile.setInts(propertyName, list, getText());
        text.ifPresent(this::setText);
    }

    public void setStrings(String propertyName, List<String> list) {
        Optional<String> text = JSONFile.setStrings(propertyName, list, getText());
        text.ifPresent(this::setText);
    }
}