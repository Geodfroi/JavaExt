package ch.azure.aurore.sqlite;

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
