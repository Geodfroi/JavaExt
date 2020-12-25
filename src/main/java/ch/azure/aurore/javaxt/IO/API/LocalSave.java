package ch.azure.aurore.javaxt.IO.API;

import ch.azure.aurore.javaxt.json.jsonFiles.JSONFile;

import java.nio.file.Path;

@Deprecated
public class LocalSave {

    public static final Path SAVE_PATH = Path.of("local.json");

    static JSONFile jsonFile = new JSONFile(SAVE_PATH);

    public static JSONFile getInstance() {
        return jsonFile;
    }
}
