package JavaExt.Tests;

import JavaExt.IO.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class SettingsTest {

    public static final String InvalidPropertyName = "NotFoundProperty";
    public static final String TestPropertyName = "TestProperty";
    public static final String TestStringValue = "testStr";

    @org.junit.jupiter.api.Test
    void get_existing() {
        String result = Settings.getInstance().get(TestPropertyName);
        assert (result.equals("testStr") );
    }

    @org.junit.jupiter.api.Test
    void get_invalid() {
        String result = Settings.getInstance().get(InvalidPropertyName);
        assert (result == null);
    }

    @org.junit.jupiter.api.Test
    void set() {
        Settings.getInstance().set(TestPropertyName, TestStringValue);
    }

    @org.junit.jupiter.api.Test
    void setEntry() {
    }

    @org.junit.jupiter.api.Test
    void getEntry() {
    }

    @org.junit.jupiter.api.AfterAll
    static void afterAll() throws IOException {
        Path path = Path.of(Settings.SETTINGS_FILE_NAME);
        String str = Files.readString(path);
        System.out.println("[settings.json] :\r\n" + str);
    }
}