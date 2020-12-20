package ch.azure.aurore.sqlite;

import java.util.HashMap;
import java.util.Map;

public class LoadReference {

    private final FieldData fieldData;
    private DatabaseRef uniqueRef;
    private Map<String, String> polyRefMap = new HashMap<>();

    public LoadReference(FieldData fieldData, Map<String, String> map) {
        this.fieldData = fieldData;
        this.polyRefMap = map;
    }

    public LoadReference(FieldData fieldData, DatabaseRef rf) {
        this.fieldData = fieldData;
        this.uniqueRef = rf;
    }

    public FieldData getFieldData() {
        return fieldData;
    }

    public DatabaseRef getUniqueRef() {
        return uniqueRef;
    }

    public boolean isUniqueRef() {
        return uniqueRef != null;
    }

    public Map<String, String> getPolyRefMap() {
        return polyRefMap;
    }
}
