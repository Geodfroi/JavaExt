package ch.azure.aurore.json.jsonFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class JSONFile extends ReadOnlyJSONFile {

    private final Path path;

    public JSONFile(Path path) {
        super(readFileText(path));
        this.path = path;
    }

    public void clear() {
        setText("{}");
    }

    public void set(String propertyName, int value) {
        Optional<String> text = JSONFile.setInt(propertyName, value, txt);
        text.ifPresent(this::setText);
    }

    public void set(String propertyName, double value){
        Optional<String> text = JSONFile.setDouble(propertyName, value, txt);
        text.ifPresent(this::setText);
    }

    public void set(String propertyName, boolean value) {
        Optional<String> text = JSONFile.setBoolean(propertyName, value, txt);
        text.ifPresent(this::setText);
    }

    public void set(String propertyName, String value) {
        Optional<String> text = JSONFile.setString(propertyName, value, txt);
        text.ifPresent(this::setText);
    }

    public void set(String propertyName, Integer value) {
        Optional<String> text = JSONFile.setInt(propertyName, value, txt);
        text.ifPresent(this::setText);
    }

    public void setMapValue(String propertyName, String key, String value) {
        Optional<String> text = JSONFile.setMapValue(propertyName, key , value, txt);
        text.ifPresent(this::setText);
    }

    public void setMapValue(String propertyName, String key, int value) {
        Optional<String> text = JSONFile.setMapValue(propertyName, key , value, txt);
        text.ifPresent(this::setText);
    }

    public void setMapValue(String propertyName, int key, int value) {
        Optional<String> text = JSONFile.setMapValue(propertyName, key , value, txt);
        text.ifPresent(this::setText);
    }

    public void setDoubles(String propertyName, double... doubles) {
        Optional<String> text = JSONFile.setDoubles(propertyName, doubles, txt);
        text.ifPresent(this::setText);
    }

    public void setIntegers(String propertyName, int... integers) {
        Optional<String> text = JSONFile.setInts(propertyName, integers, txt);
        text.ifPresent(this::setText);
    }

    public void setStrings(String propertyName, String... strings) {
        Optional<String> text = JSONFile.setStrings(propertyName, strings, txt);
        text.ifPresent(this::setText);
    }

    private void setText(String txt){
        try {
            super.txt = txt;
            Files.writeString(path, txt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFileText(Path path)
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