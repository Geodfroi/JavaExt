package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.json.API.JSON;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PullField extends PullData {

    private final Object val;

    public PullField(FieldData f, ResultSet resultSet, int index) throws SQLException {
        super(f);

        if (fieldData.isConvertToJSON()) {
            String txt = resultSet.getString(index);
            switch (f.getType().getSimpleName()) {
                case "List":
                    if (f.getInternalType() == null)
                        throw new IllegalStateException("internal type not set");
                    val = JSON.readList(f.getInternalType(), txt);
                    break;
                case "Set":
                    if (f.getInternalType() == null)
                        throw new IllegalStateException("internal type not set");
                    val = JSON.readSet(f.getInternalType(), txt);
                    break;
                case "Map":
                    throw new IllegalStateException("Map collection is not yet implemented");
                default:
                    val = JSON.readValue(f.getType(), txt);
                    break;
            }
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
