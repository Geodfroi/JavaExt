package ch.azure.aurore.sqlite;

import ch.azure.aurore.json.JSON;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class PullField extends PullData {

    private Object val;

    public PullField(FieldData f, ResultSet resultSet, int index) throws SQLException {
        super(f);
        if (fieldData.isConvertToJSON()) {
            String txt = resultSet.getString(index);
            if (Collection.class.isAssignableFrom(f.getType())) {
                if (f.getInternalType() == null)
                    throw new IllegalStateException("internal type not set");

                val = JSON.readCollection(f.getInternalType(), txt);
            } else
                val = JSON.readValue(f.getType(), txt);
        } else {
            switch (f.getType().getSimpleName()) {
                case "boolean":
                    val = resultSet.getBoolean(index);
                    break;
                case "byte[]":
                    val = resultSet.getBytes(index);
                    break;
                case "int":
                    val = resultSet.getInt(index);
                    break;
                case "String":
                    val = resultSet.getString(index);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + f.getType().getSimpleName());
            }
        }
    }

    @Override
    public <T> void execute(SQLiteImplementation sq, T data) {
        fieldData.setValue(data, val);
    }
}
