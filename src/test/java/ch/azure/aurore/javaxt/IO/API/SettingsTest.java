package ch.azure.aurore.javaxt.IO.API;

import ch.azure.aurore.javaxt.IO.exceptions.MissingSettingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SettingsTest {

    public static final String PROPERTY_NAME = "count";

    @Test
    void getSettings() {
        Assertions.assertThrows(MissingSettingException.class, () ->
                Settings.getInstance().getIntegers("invalid"));
    }

    @Test
    void getSettings_valid() {
        int result =  Settings.getInstance().getInteger(PROPERTY_NAME);
        Assertions.assertEquals(result, 7);
    }
}