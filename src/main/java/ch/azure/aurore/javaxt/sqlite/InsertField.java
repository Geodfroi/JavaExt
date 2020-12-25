package ch.azure.aurore.javaxt.sqlite;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertField extends InsertData {

    private final Object value;
    private final String setMethod;

    public InsertField(FieldData fieldData, int i, Object value, String setMethod) {
        super(fieldData, i);
        this.value = value;
        this.setMethod = setMethod;
    }

    @Override
    public void execute(PreparedStatement statement, Object data) throws SQLException {
        switch (setMethod){
            case "setBytes":
                statement.setBytes(getIndex(), (byte[])value);
                break;
            case "setInt":
                statement.setInt(getIndex(), (int)value);
                break;
            case "setObject":
                statement.setObject(getIndex(), value);
                break;
            case "setString":
                statement.setString(getIndex(), (String)value);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + setMethod);
        }
    }
}
