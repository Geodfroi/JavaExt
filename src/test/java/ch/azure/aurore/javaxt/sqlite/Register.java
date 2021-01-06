package ch.azure.aurore.javaxt.sqlite;

import java.util.HashMap;
import java.util.Map;

public class Register {

    private Map<Integer, Employee> table = new HashMap<>();

    public Map<Integer, Employee> getTable() {
        return table;
    }

    public void setTable(Map<Integer, Employee> table) {
        this.table = table;
    }
}
