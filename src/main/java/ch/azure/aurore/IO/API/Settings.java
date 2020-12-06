package ch.azure.aurore.IO.API;

import ch.azure.aurore.IO.jsonFiles.ReadOnlyJSONFile;

/**
 * https://mkyong.com/java/jackson-tree-model-example/
 */
public class Settings {

    private static final String SETTINGS_FILE_NAME = "settings.json";

    private static ReadOnlyJSONFile settings = new ReadOnlyJSONFile(SETTINGS_FILE_NAME);

    public static ReadOnlyJSONFile getInstance(){
        return settings;
    }
}