package ch.azure.aurore.sqlite;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class InsertData {
    private final FieldData fieldData;
    private final int index;

    public InsertData(FieldData fieldData, int i) {
        this.fieldData = fieldData;
        index = i;
    }

    public abstract void execute(PreparedStatement statement, Object data, boolean isModified) throws SQLException;

    public FieldData getFieldData() {
        return fieldData;
    }

    public int getIndex() {
        return index;
    }
}
