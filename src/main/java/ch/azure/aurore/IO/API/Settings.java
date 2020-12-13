package ch.azure.aurore.IO.API;

import ch.azure.aurore.IO.jsonFiles.ReadOnlyJSONFile;

/**
 * https://mkyong.com/java/jackson-tree-model-example/
 */
public class Settings {

   // private static final String SETTINGS_FILE_NAME = "ch/azure/aurore/IO/settings.json";
    private static final String SETTINGS_FILE_NAME = "settings.json";

    private static ReadOnlyJSONFile settings = Settings.create();

    private static ReadOnlyJSONFile create() {
        String txt = FileResources.getResourceText(SETTINGS_FILE_NAME);
        return new ReadOnlyJSONFile(txt);
    }

    public static ReadOnlyJSONFile getInstance(){
        return settings;
    }
}