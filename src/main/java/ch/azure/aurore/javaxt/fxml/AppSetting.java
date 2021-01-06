package ch.azure.aurore.javaxt.fxml;

import ch.azure.aurore.javaxt.IO.API.FileResources;
import ch.azure.aurore.javaxt.strings.Strings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppSetting {

    private static final String SETTINGS_FILE_NAME = "settings.json";
    private String appName;
    private double[] windowSize;
    private boolean _isWriteForbidden;

    public static AppSetting createInstance(Class<? extends AppSetting> clazz) {

        if (clazz == null)
            throw new IllegalArgumentException("AppSetting class parameter is null");
        String txt = FileResources.getResourceText(SETTINGS_FILE_NAME);
        if (Strings.isNullOrEmpty(txt))
            throw new IllegalStateException("[" + clazz + "] file is empty");

        ObjectMapper mapper = new ObjectMapper();
        AppSetting item = null;
        try {
            item = mapper.readValue(txt, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (item == null)
            throw new IllegalStateException("Error loading app settings");
        item.forbidWrite();
        return item;
    }

    private void forbidWrite() {
        _isWriteForbidden = true;
    }

    public String getAppName() {
        return appName;
    }

    @SuppressWarnings("unused") // <- JSON serialisation
    public void setAppName(String appName) {
        this.appName = appName;
        checkWritePermission();
    }

    protected void checkWritePermission() {
        if (_isWriteForbidden)
            throw new IllegalCallerException("[Settings] file set methods should not be called");
    }

    public double[] getWindowSize() {
        return windowSize;
    }

    @SuppressWarnings("unused") // <- JSON serialisation
    public void setWindowSize(double[] windowSize) {
        this.windowSize = windowSize;
        checkWritePermission();
    }
}
