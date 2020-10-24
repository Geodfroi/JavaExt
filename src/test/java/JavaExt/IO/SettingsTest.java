package JavaExt.IO;

import JavaExt.IO.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class SettingsTest {

    public static final String InvalidPropertyName = "NotFoundProperty";
    public static final String TestPropertyName = "TestProperty";
    public static final String TestStringValue = "testStr";

    private static final String TestMapPropertyName = "MapProperty";
    private static final String TestMapKey = "MapKey";
    private static final String TestMapValue = "MapValue";
    private static final String TestMapKey2 = "MapKey2";
    private static final String TestMapValue2 = "MapValue2";


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
    void getMapValue(){
       String value = Settings.getInstance().getMapValue(TestMapPropertyName, TestMapKey);
       assert (value.equals(TestMapValue));
    }

    @org.junit.jupiter.api.Test
    void getMapValue_invalidKey(){
        String value = Settings.getInstance().getMapValue(TestPropertyName, "notValidKey");
        assert (value == null);
    }

    @org.junit.jupiter.api.Test
    void getMapValue_nullKey(){
        String value = Settings.getInstance().getMapValue(TestPropertyName, null);
        assert (value == null);
    }

    @org.junit.jupiter.api.Test
    void SetMapValue(){
        Settings.getInstance().setMapValue(TestMapPropertyName, TestMapKey,  TestMapValue);
    }
    @org.junit.jupiter.api.Test
    void SetMapValue2(){
        Settings.getInstance().setMapValue(TestMapPropertyName, TestMapKey2,  TestMapValue2);
    }
    @org.junit.jupiter.api.Test
    void SetMapValue_null(){
        Settings.getInstance().setMapValue(TestMapPropertyName, TestMapKey2,  null);
    }

    @org.junit.jupiter.api.AfterAll
    static void afterAll() throws IOException {
        Path path = Path.of(Settings.SETTINGS_FILE_NAME);
        String str = Files.readString(path);
        System.out.println("[settings.json] :\r\n" + str);
    }
}