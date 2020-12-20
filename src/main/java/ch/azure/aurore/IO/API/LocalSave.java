package ch.azure.aurore.IO.API;

import ch.azure.aurore.json.jsonFiles.JSONFile;

import java.nio.file.Path;

public class LocalSave  {

    public static final Path SAVE_PATH = Path.of("local.json");

    static JSONFile jsonFile = new JSONFile(SAVE_PATH);

    public static JSONFile getInstance(){
        return jsonFile;
    }
}
