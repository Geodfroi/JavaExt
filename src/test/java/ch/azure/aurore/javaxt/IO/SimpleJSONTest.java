package ch.azure.aurore.javaxt.IO;

import ch.azure.aurore.javaxt.conversions.Conversions;
import ch.azure.aurore.javaxt.json.SimpleJSON;
import org.junit.jupiter.api.Assertions;

import java.util.*;

class SimpleJSONTest {

    private static final String EMPTY_TEXT = "{}";

    SimpleJSON jsonFile = new SimpleJSON();

    //region get methods
    @org.junit.jupiter.api.Test
    void getBoolean_existing() {
        String propertyName = "hasAccess";
        String txt = "{\r\n  \""  + propertyName + "\" : true\r\n}";
        Optional<Boolean> result = jsonFile.getBoolean(propertyName, txt);
        assert (result.isPresent());
        assert (result.get().equals(true));
    }

    @org.junit.jupiter.api.Test
    void getDouble() {
        String propertyName = "height";
        double value = 25.5;
        String txt = "{\r\n  \""  + propertyName + "\" : " + value +"\r\n}";
        Optional<Double> result = jsonFile.getDouble(propertyName, txt);
        assert (result.isPresent());
        assert (result.get() == value);
    }

    @org.junit.jupiter.api.Test
    void getDoubles() {
        String propertyName = "count";
        List<Double> list = new ArrayList<>(Arrays.asList(1.0,1.1,1.2,1.3));
        String txt = "{\r\n  \"" + propertyName + "\" : [ " + Conversions.toString(list, ", ") + " ]\r\n}";
        Optional<List<Double>> resultList = jsonFile.getDoubles(propertyName, txt);
        assert resultList.isPresent() && list.equals(resultList.get());
    }

    @org.junit.jupiter.api.Test
    void getInt_existing() {
        String propertyName = "count";
        int value = 25;
        String txt = "{\r\n  \""  + propertyName + "\" : " + value +"\r\n}";
        Optional<Integer> result = jsonFile.getInt(propertyName, txt);
        assert (result.isPresent());
        assert (result.get() == value);
    }

    @org.junit.jupiter.api.Test
    void getInts() {
        String propertyName = "count";
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4));
        String txt = "{\r\n  \"" + propertyName + "\" : [ " + Conversions.toString(list, ", ") + " ]\r\n}";
        Optional<List<Integer>> resultList = jsonFile.getInts(propertyName, txt);
        assert resultList.isPresent() && list.equals(resultList.get());
    }

    @org.junit.jupiter.api.Test
    void getIntToStringMap(){
        String mapProperty = "testMap";
        int key1 = 24;
        int key2 = 32;
        String value1 = "Max";
        String value2 = "Webster";

        String txt = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + key1 + "\" : \"" + value1 + "\",\r\n" +
                "    \"" + key2 + "\" : \"" + value2 + "\"\r\n" +
                "  }\r\n" +
                "}";

        Optional<String> value = jsonFile.getMapString(mapProperty, key2, txt);
        assert value.isPresent();
        Assertions.assertEquals(value.get(), value2);
    }

    @org.junit.jupiter.api.Test
    void getIntToIntMao(){
        String mapProperty = "testMap";
        int mapKey1 = 0;
        int mapKey2 = 1;
        int value1 = 38;
        int value2 = 202;

        String txt = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + mapKey1 + "\" : " + value1 + ",\r\n" +
                "    \"" + mapKey2 + "\" : " + value2 +"\r\n" +
                "  }\r\n" +
                "}";
        Optional<Integer> value = jsonFile.getMapInteger(mapProperty, mapKey2, txt);
        assert value.isPresent();
        Assertions.assertEquals(value.get(), value2);
    }

    @org.junit.jupiter.api.Test
    void getStringToStringMap() {

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

        Optional<String> value = jsonFile.getMapString(mapProperty, mapKey2, txt);
        assert value.isPresent();
        Assertions.assertEquals(value.get(), value2);
    }

    @org.junit.jupiter.api.Test
    void getStringToStringMap_invalidKey() {

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

        Optional<String> value = jsonFile.getMapString(mapProperty, "wrongKey", txt);
        assert (value.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getStringToStringMap_nullKey() {

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

        Optional<String> value = jsonFile.getMapString(mapProperty, null, txt);
        assert (value.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getStringToStringMap_noMap() {
        Optional<String> value = jsonFile.getMapString("noMap", "key", EMPTY_TEXT);
        assert (value.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getStr_existing() {
        String propertyName = "Name";
        String value = "George";
        String txt = "{\r\n  \""  + propertyName + "\" : \"" + value + "\"\r\n}";
        Optional<String> result = jsonFile.getString(propertyName, txt);
        assert (result.isPresent());
        assert (result.get().equals(value));
    }

    @org.junit.jupiter.api.Test
    void getStrings() {
        String propertyName = "days";
        List<String> list = new ArrayList<>(Arrays.asList("monday","wednesday","friday"));
        String txt = "{\r\n  \"" + propertyName + "\" : [ \"" + Conversions.toString(list, "\", \"") + "\" ]\r\n}";
        System.out.println(txt);
        Optional<List<String>> resultList= jsonFile.getStrings(propertyName, txt);
        assert (resultList.isPresent() && list.equals(resultList.get()));
    }

    //endregion

    //region set methods

    @org.junit.jupiter.api.Test
    void setBoolean() {
        String propertyName = "boolean";
        String expected = "{\r\n  \"" + propertyName+ "\" : true\r\n}";
        Optional<String> result = jsonFile.setBoolean(propertyName, true, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setBoolean_existingTxt() {
        String propertyName = "boolean";
        String existingTxt = "{\r\n  \"Count\" : 25\r\n}";
        String expected ="{\r\n" +
                "  \"Count\" : 25,\r\n" +
                "  \"boolean\" : true\r\n" +
                "}";

        Optional<String> result = jsonFile.setBoolean(propertyName, true, existingTxt);
        assert result.isPresent();
        Assertions.assertEquals(result.get(), expected);
    }

    @org.junit.jupiter.api.Test
    void setBoolean_nullText() {
        String propertyName = "boolean";
        String expected = "{\r\n  \"" + propertyName+ "\" : " + true + "\r\n}";
        Optional<String> result = jsonFile.setBoolean(propertyName, true, null);
        assert result.isPresent();
        Assertions.assertEquals(result.get(), expected);
    }

    @org.junit.jupiter.api.Test
    void setDouble() {
        String propertyName = "height";
        double value = 7.0;
        String expected = "{\r\n  \"" + propertyName+ "\" : " + value + "\r\n}";
        Optional<String> result = jsonFile.setDouble(propertyName, value, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setDoubles() {
        String propertyName = "numbers";
        double[] list = new double[]{25.2, 8.9, 42.23};
        String expected = "{\r\n  \"" + propertyName + "\" : [ " + Conversions.toString(Collections.singleton(list), ", ") + " ]\r\n}";
        Optional<String> result = jsonFile.setDoubles(propertyName, list, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setInt() {
        String propertyName = "height";
        int value = 7;
        String expected = "{\r\n  \"" + propertyName+ "\" : " + value + "\r\n}";
        Optional<String> result = jsonFile.setInt(propertyName, value, EMPTY_TEXT);
        assert result.isPresent();
        Assertions.assertEquals(result.get(), expected);
    }

    @org.junit.jupiter.api.Test
    void setInts() {
        String propertyName = "numbers";
        int[] list = new int[]{4,25,8};
        String expected = "{\r\n  \"" + propertyName + "\" : [ " + Conversions.toString(Collections.singleton(list), ", ") + " ]\r\n}";
        Optional<String> result = jsonFile.setInts(propertyName, list, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setInts_null() {
        String propertyName = "numbers";
        String expected = "{ }";
        Optional<String> result = jsonFile.setInts(propertyName, null, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setIntegerToIntegerMap(){
        String mapProperty = "testMap";
        int key = 1;
        int value = 203;

        String expected = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + key + "\" : " + value + "\r\n" +
                "  }\r\n" +
                "}";

        Optional<String> result = jsonFile.setMapValue(mapProperty, key, value, EMPTY_TEXT);
        assert  result.isPresent();
        Assertions.assertEquals( result.get(), expected);
    }

    @org.junit.jupiter.api.Test
    void setIntegerToStringMap(){
        String mapProperty = "testMap";
        int key = 24;
        String value = "Webster";

        String expected = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + key + "\" : \"" + value + "\"\r\n" +
                "  }\r\n" +
                "}";

        Optional<String> result = jsonFile.setMapValue(mapProperty, key, value, EMPTY_TEXT);
        assert (result.isPresent());
        Assertions.assertEquals( result.get(), expected);
    }

    @org.junit.jupiter.api.Test
    void setStringToStringMap() {
        String mapProperty = "testMap";
        String key1 = "Jones";
        String key2 = "eric";
        String value1 = "present";
        String value2 = "absent";

        String expected = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + key1 + "\" : \"" + value1 + "\",\r\n" +
                "    \"" + key2 + "\" : \"" + value2 +"\"\r\n" +
                "  }\r\n" +
                "}";

        Optional<String> result = jsonFile.setMapValue(mapProperty, key1, value1, EMPTY_TEXT);
        assert (result.isPresent());
        result = jsonFile.setMapValue(mapProperty, key2, value2, result.get());
        assert  result.isPresent();
        Assertions.assertEquals(result.get(), expected);
    }

    @org.junit.jupiter.api.Test
    void setStringToStringMap_null() {

        String mapProperty = "testMap";
        String key = "count";
        String expected = "{\r\n" +
                "  \"" + mapProperty + "\" : {\r\n" +
                "    \"" + key + "\" : null\r\n" +
                "  }\r\n" +
                "}";

        Optional<String> result = jsonFile.setMapValue(mapProperty, key, null, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setStr() {
        String propertyName = "candidate";
        String value = "hector";
        String expected = "{\r\n  \"" + propertyName+ "\" : \"" + value + "\"\r\n}";
        Optional<String> result = jsonFile.setString(propertyName, value, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    @org.junit.jupiter.api.Test
    void setStrings() {
        String propertyName = "days";
        String[] list = new String[]{"monday","wednesday","friday"};
        String expected = "{\r\n  \"" + propertyName + "\" : [ \"" + Conversions.toString(Arrays.stream(list), "\", \"") + "\" ]\r\n}";
        Optional<String> result = jsonFile.setStrings(propertyName, list, EMPTY_TEXT);
        assert (result.isPresent() && result.get().equals(expected));
    }

    //endregion
}