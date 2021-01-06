package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;

import java.util.HashMap;
import java.util.Map;


public class Register extends SQLiteData {

    private Map<Integer, Employee> entries = new HashMap<>();

    public Map<Integer, Employee> getEntries() {
        return entries;
    }

    public void setEntries(Map<Integer, Employee> entries) {
        this.entries = entries;
    }
}
