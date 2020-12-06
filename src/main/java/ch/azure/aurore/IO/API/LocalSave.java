package ch.azure.aurore.IO.API;

import ch.azure.aurore.IO.jsonFiles.JSONFile;

public class LocalSave  {

    public static final String SETTINGS_FILE_NAME = "local.json";

    static JSONFile jsonFile = new JSONFile(SETTINGS_FILE_NAME);

    public static JSONFile getInstance(){
        return jsonFile;
    }
}
