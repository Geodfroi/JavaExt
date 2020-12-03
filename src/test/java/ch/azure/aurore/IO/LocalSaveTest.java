package ch.azure.aurore.IO;

import ch.azure.aurore.IO.API.LocalSave;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

class LocalSaveTest {

    public static final String InvalidPropertyName = "NotFoundProperty";
    public static final String StrPropertyName = "StrProperty";
    public static final String IntPropertyName = "IntProperty";
    public static final String MapPropertyName = "MapProperty";
    public static final String BoolPropertyName = "BoolProperty";

    public static final String TestStringValue = "testStr";
    public static final int IntValue = 43;

    private static final String TestMapPropertyName = "MapProperty";
    private static final String TestMapKey = "MapKey";
    private static final String TestMapValue = "MapValue";
    private static final String TestMapKey2 = "MapKey2";
    private static final String TestMapValue2 = "MapValue2";

    @org.junit.jupiter.api.Test
    void setStr() {
        LocalSave.set(StrPropertyName, TestStringValue);
    }

    @org.junit.jupiter.api.Test
    void setInt() {
        LocalSave.set(IntPropertyName, IntValue);
    }

    @org.junit.jupiter.api.Test
    void setBoolean(){
        LocalSave.set(BoolPropertyName, true);
    }

    @org.junit.jupiter.api.Test
    void SetMapValue(){
        LocalSave.setMapValue(TestMapPropertyName, TestMapKey,  TestMapValue);
    }

    @org.junit.jupiter.api.Test
    void SetMapValue2(){
        LocalSave.setMapValue(TestMapPropertyName, TestMapKey2,  TestMapValue2);
    }

    @org.junit.jupiter.api.Test
    void SetMapValue_null(){
        LocalSave.setMapValue(TestMapPropertyName, TestMapKey2,  null);
    }

    @org.junit.jupiter.api.Test
    void getStr_existing() {
        Optional<String> result = LocalSave.getStr(StrPropertyName);
        if (result.isPresent()){
            String str = result.get();
            assert (str.equals(TestStringValue) );
        }
        else
        {
            throw new IllegalArgumentException("getStr_existing : not found");
        }
    }

    @org.junit.jupiter.api.Test
    void getStr_invalid() {
        assert(LocalSave.getStr(InvalidPropertyName).isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getBoolean_existing() {
        Optional<Boolean> result = LocalSave.getBoolean(BoolPropertyName);
        assert(result.get());
    }

    @org.junit.jupiter.api.Test
    void getInt_existing() {
        Optional<Integer> result = LocalSave.getInt(IntPropertyName);
        assert (result.get() == IntValue);
    }

    @org.junit.jupiter.api.Test
    void getMapValue(){
        var value = LocalSave.getMapString(MapPropertyName, TestMapKey);
        assert (value.get().equals(TestMapValue));
    }

    @org.junit.jupiter.api.Test
    void getMapValue_invalidKey(){
        var result = LocalSave.getMapString(MapPropertyName, "notValidKey");
        assert (result.get() == null);
    }

    @org.junit.jupiter.api.Test
    void getMapValue_nullKey(){
        var result = LocalSave.getMapString(MapPropertyName, null);
        assert (result.get() == null);
    }

    @org.junit.jupiter.api.Test
    void getMapValues_valid(){
        Map<String,String> map = LocalSave.getMapValues(MapPropertyName);
        assert (map.get(TestMapKey).equals(TestMapValue));
        assert (map.get(TestMapKey2)==(null));
    }

    @org.junit.jupiter.api.AfterAll
    static void afterAll() throws IOException {
        Path path = Path.of(LocalSave.SETTINGS_FILE_NAME);
        String str = Files.readString(path);
        System.out.println("[local.json] :\r\n" + str);
    }
}