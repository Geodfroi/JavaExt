package ch.azure.aurore.IO;

import ch.azure.aurore.Strings.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class SimpleJSONTest {

    private static final String EMPTY_TEXT = "{}";

    SimpleJSON LocalSave = new SimpleJSON();

    //region get methods
    @org.junit.jupiter.api.Test
    void getBoolean_existing() {
        String propertyName = "hasAccess";
        String txt = "{\r\n  \""  + propertyName + "\" : true\r\n}";
        Optional<Boolean> result = LocalSave.getBoolean(propertyName, txt);
        assert (result.isPresent());
        assert (result.get().equals(true));
    }

    @org.junit.jupiter.api.Test
    void getDouble() {
        String propertyName = "height";
        double value = 25.5;
        String txt = "{\r\n  \""  + propertyName + "\" : " + value +"\r\n}";
        Optional<Double> result = LocalSave.getDouble(propertyName, txt);
        assert (result.isPresent());
        assert (result.get() == value);
    }

    @org.junit.jupiter.api.Test
    void getDoubles() {
        String propertyName = "count";
        List<Double> list = new ArrayList<>(Arrays.asList(1.0,1.1,1.2,1.3));
        String txt = "{\r\n  \"" + propertyName + "\" : [ " + Strings.toString(list, ", ") + " ]\r\n}";
        Optional<List<Double>> resultList = LocalSave.getDoubles(propertyName, txt);
        assert resultList.isPresent() && list.equals(resultList.get());
    }

    @org.junit.jupiter.api.Test
    void getInt_existing() {
        String propertyName = "count";
        int value = 25;
        String txt = "{\r\n  \""  + propertyName + "\" : " + value +"\r\n}";
        Optional<Integer> result = LocalSave.getInt(propertyName, txt);
        assert (result.isPresent());
        assert (result.get() == value);
    }

    @org.junit.jupiter.api.Test
    void getInts() {
        String propertyName = "count";
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4));
        String txt = "{\r\n  \"" + propertyName + "\" : [ " + Strings.toString(list, ", ") + " ]\r\n}";
        System.out.println(txt);
        Optional<List<Integer>> resultList = LocalSave.getInts(propertyName, txt);
        assert resultList.isPresent() && list.equals(resultList.get());
    }

    @org.junit.jupiter.api.Test
    void getMapValue() {

        String mapProperty = "testMap";
        String mapKey1 = "Jones";
        String mapKey2 = "eric";
        String value1 = "present";
        String value2 = "absent";

        String txt = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + mapKey1 + "\" : \"" + value1 + "\",\r\n" +
                "    \"" + mapKey2 + "\" : \"" + value2 +"\"\r\n" +
                "  }\r\n" +
                "}";

        Optional<String> value = LocalSave.getMapStr(mapProperty, mapKey2, txt);
        assert (value.isPresent() && value.get().equals(value2));
    }

    @org.junit.jupiter.api.Test
    void getMapValue_invalidKey() {

        String mapProperty = "testMap";
        String mapKey1 = "Jones";
        String mapKey2 = "eric";
        String value1 = "present";
        String value2 = "absent";

        String txt = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + mapKey1 + "\" : \"" + value1 + "\",\r\n" +
                "    \"" + mapKey2 + "\" : \"" + value2 +"\"\r\n" +
                "  }\r\n" +
                "}";

        Optional<String> value = LocalSave.getMapStr(mapProperty, "wrongKey", txt);
        assert (value.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getMapValue_nullKey() {

        String mapProperty = "testMap";
        String mapKey1 = "Jones";
        String mapKey2 = "eric";
        String value1 = "present";
        String value2 = "absent";

        String txt = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + mapKey1 + "\" : \"" + value1 + "\",\r\n" +
                "    \"" + mapKey2 + "\" : \"" + value2 +"\"\r\n" +
                "  }\r\n" +
                "}";

        Optional<String> value = LocalSave.getMapStr(mapProperty, null, txt);
        assert (value.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getMapValue_noMap() {
        Optional<String> value = LocalSave.getMapStr("noMap", "key", EMPTY_TEXT);
        assert (value.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getStr_existing() {
        String propertyName = "Name";
        String value = "George";
        String txt = "{\r\n  \""  + propertyName + "\" : \"" + value + "\"\r\n}";
        Optional<String> result = LocalSave.getString(propertyName, txt);
        assert (result.isPresent());
        assert (result.get().equals(value));
    }

    @org.junit.jupiter.api.Test
    void getStrings() {
        String propertyName = "days";
        List<String> list = new ArrayList<>(Arrays.asList("monday","wednesday","friday"));
        String txt = "{\r\n  \"" + propertyName + "\" : [ \"" + Strings.toString(list, "\", \"") + "\" ]\r\n}";
        System.out.println(txt);
        Optional<List<String>> resultList= LocalSave.getStrings(propertyName, txt);
        assert (resultList.isPresent() && list.equals(resultList.get()));
    }

    //endregion

    //region set methods

    @org.junit.jupiter.api.Test
    void setBoolean() {
        String propertyName = "boolean";
        String expected = "{\r\n  \"" + propertyName+ "\" : true\r\n}";
        Optional<String> result = LocalSave.setBoolean(propertyName, true, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setBoolean_nullText() {
        String propertyName = "boolean";
        String expected = "{\r\n  \"" + propertyName+ "\" : " + true + "\r\n}";
        Optional<String> result = LocalSave.setBoolean(propertyName, true, null);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setDouble() {
        String propertyName = "height";
        double value = 7.0;
        String expected = "{\r\n  \"" + propertyName+ "\" : " + value + "\r\n}";
        Optional<String> result = LocalSave.setDouble(propertyName, value, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setDoubles() {
        String propertyName = "numbers";
        List<Double> list = new ArrayList<>(Arrays.asList(4.0,25.2,8.9));
        String expected = "{\r\n  \"" + propertyName + "\" : [ " + Strings.toString(list, ", ") + " ]\r\n}";
        Optional<String> result = LocalSave.setDoubles(propertyName, list, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setInt() {
        String propertyName = "height";
        int value = 7;
        String expected = "{\r\n  \"" + propertyName+ "\" : " + value + "\r\n}";
        Optional<String> result = LocalSave.setInt(propertyName, value, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setInts() {
        String propertyName = "numbers";
        List<Integer> list = new ArrayList<>(Arrays.asList(4,25,8));
        String expected = "{\r\n  \"" + propertyName + "\" : [ " + Strings.toString(list, ", ") + " ]\r\n}";
        Optional<String> result = LocalSave.setInts(propertyName, list, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setInts_null() {
        String propertyName = "numbers";
        String expected = "{ }";
        Optional<String> result = LocalSave.setInts(propertyName, null, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setStringMapValue() {
        String mapProperty = "testMap";
        String mapKey1 = "Jones";
        String mapKey2 = "eric";
        String value1 = "present";
        String value2 = "absent";

        String expected = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + mapKey1 + "\" : \"" + value1 + "\",\r\n" +
                "    \"" + mapKey2 + "\" : \"" + value2 +"\"\r\n" +
                "  }\r\n" +
                "}";

        Optional<String> result = LocalSave.setMapValue(mapProperty, mapKey1, value1, EMPTY_TEXT);
        assert (result.isPresent());
        result = LocalSave.setMapValue(mapProperty, mapKey2, value2, result.get());
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setMapValue_null() {

        String mapProperty = "testMap";
        String key = "count";
        String expected = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + key + "\" : null\r\n" +
                "  }\r\n" +
                "}";

        Optional<String> result = LocalSave.setMapValue(mapProperty, key, null, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setStr() {
        String propertyName = "candidate";
        String value = "hector";
        String expected = "{\r\n  \"" + propertyName+ "\" : \"" + value + "\"\r\n}";
        Optional<String> result = LocalSave.setString(propertyName, value, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setStrings() {
        String propertyName = "days";
        List<String> list = new ArrayList<>(Arrays.asList("monday","wednesday","friday"));
        String expected = "{\r\n  \"" + propertyName + "\" : [ \"" + Strings.toString(list, "\", \"") + "\" ]\r\n}";
        Optional<String> result = LocalSave.setStrings(propertyName, list, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    //endregion
}