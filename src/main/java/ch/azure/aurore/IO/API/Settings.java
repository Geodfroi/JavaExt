package ch.azure.aurore.IO.API;

import ch.azure.aurore.IO.SimpleJSON;
import ch.azure.aurore.IO.exceptions.MissingSettingException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * https://mkyong.com/java/jackson-tree-model-example/
 */
public class Settings {

   // private static final String SETTINGS_FILE_NAME = "ch/azure/aurore/IO/settings.json";
    private static final String SETTINGS_FILE_NAME = "settings.json";

    SimpleJSON JSONFile = new SimpleJSON();
    private static final Settings instance = new Settings();
    private final String txt;

    private Settings(){
         txt = FileResources.getResourceText(SETTINGS_FILE_NAME);
    }

    public static Settings getInstance(){
        return instance;
    }
    
    public Double getDouble(String propertyName) {
        Optional<Double> r = JSONFile.getDouble(propertyName, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    private void throwException(String propertyName) {
        throw new MissingSettingException("Can't find [" + propertyName + "] property in settings file");
    }


    public List<Double>getDoubles(String propertyName){
        Optional<List<Double>> r= JSONFile.getDoubles(propertyName, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    public int getInteger(String propertyName) {
        Optional<Integer> r =  JSONFile.getInt(propertyName, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    public List<Integer>getIntegers(String propertyName){
        Optional<List<Integer>> r = JSONFile.getInts(propertyName, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    public String getString(String propertyName) {
        Optional<String> r = JSONFile.getString(propertyName, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    public List<String>getStrings(String propertyName) {
        Optional<List<String>> r = JSONFile.getStrings(propertyName, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    public boolean getBoolean(String propertyName){
        Optional<Boolean> r = JSONFile.getBoolean(propertyName, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    public Integer getMapInteger(String propertyName, String key){
        Optional<Integer> r = JSONFile.getMapInteger(propertyName, key, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    public int getMapInteger(String propertyName, int key){
        Optional<Integer> r = JSONFile.getMapInteger(propertyName, key, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    public String getMapString(String propertyName, int key) {
        Optional<String> r = JSONFile.getMapString(propertyName, key, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    public String getMapString(String propertyName, String key) {
        Optional<String> r = JSONFile.getMapString(propertyName, key, txt);
        if (r.isEmpty()) throwException(propertyName);
        return r.get();
    }

    public Map<String, String> getMapValues(String propertyName){
        Map<String, String> r = JSONFile.getMapValues(propertyName, txt);
        if (r == null)
            throw new RuntimeException("Can't find [" + propertyName + "] property in settings file");
        return r;
    }
}