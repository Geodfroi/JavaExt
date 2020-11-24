package JavaExt.IO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

class SettingsTest {

    public static final String InvalidPropertyName = "NotFoundProperty";
    public static final String StrPropertyName = "StrProperty";
    public static final String IntPropertyName = "IntProperty";
    public static final String MapPropertyName = "MapProperty";

    public static final String TestStringValue = "testStr";
    public static final int IntValue = 43;

    private static final String TestMapPropertyName = "MapProperty";
    private static final String TestMapKey = "MapKey";
    private static final String TestMapValue = "MapValue";
    private static final String TestMapKey2 = "MapKey2";
    private static final String TestMapValue2 = "MapValue2";


    @org.junit.jupiter.api.Test
    void setStr() {
        Settings.getInstance().set(StrPropertyName, TestStringValue);
    }

    @org.junit.jupiter.api.Test
    void setInt() {
        Settings.getInstance().set(IntPropertyName, IntValue);
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

    @org.junit.jupiter.api.Test
    void getStr_existing() {
        Optional<String> result = Settings.getInstance().getStr(StrPropertyName);
        if (result.isPresent()){
            String str = Settings.getInstance().getStr(StrPropertyName).get();
            assert (str.equals(TestStringValue) );
        }
        else
        {
            throw new IllegalArgumentException("getStr_existing : not found");
        }
    }

    @org.junit.jupiter.api.Test
    void getStr_invalid() {
        System.out.println();
        assert(Settings.getInstance().getStr(InvalidPropertyName).isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getInt_existing() {
        int result = Settings.getInstance().getInt(IntPropertyName).get();
        assert (result == IntValue);
    }

    @org.junit.jupiter.api.Test
    void getMapValue(){
       String value = Settings.getInstance().getMapValue(TestMapPropertyName, TestMapKey);
       assert (value.equals(TestMapValue));
    }

    @org.junit.jupiter.api.Test
    void getMapValue_invalidKey(){
        String value = Settings.getInstance().getMapValue(MapPropertyName, "notValidKey");
        assert (value == null);
    }

    @org.junit.jupiter.api.Test
    void getMapValue_nullKey(){
        String value = Settings.getInstance().getMapValue(MapPropertyName, null);
        assert (value == null);
    }

    @org.junit.jupiter.api.AfterAll
    static void afterAll() throws IOException {
        Path path = Path.of(Settings.SETTINGS_FILE_NAME);
        String str = Files.readString(path);
        System.out.println("[settings.json] :\r\n" + str);
    }
}