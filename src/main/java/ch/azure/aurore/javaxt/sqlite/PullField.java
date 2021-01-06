package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.json.API.JSON;
import ch.azure.aurore.javaxt.reflection.FieldInfo;
import ch.azure.aurore.javaxt.reflection.FieldType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PullField extends PullData {

    private final Object val;

    public PullField(FieldData f, ResultSet resultSet, int index) throws SQLException {
        super(f);

        String txt = resultSet.getString(index);
        FieldType fieldType = f.getFieldType();
        FieldInfo fieldInfo = f.getFieldInfo();
        switch (fieldType) {

            case BOOLEAN:
                val = resultSet.getBoolean(index);
                break;
            case BYTE:
                val = resultSet.getByte(index);
                break;
            case DOUBLE:
                val = resultSet.getDouble(index);
                break;
            case FLOAT:
                val = resultSet.getFloat(index);
                break;
            case INT:
                val = resultSet.getInt(index);
                break;
            case LIST:
                val = JSON.readList(fieldInfo.getTypeParameters()[0], txt);
                break;
            case MAP:
                val = JSON.readMap(fieldInfo.getTypeParameters()[0], fieldInfo.getTypeParameters()[1], txt);
                break;
            case SET:
                val = JSON.readSet(fieldInfo.getTypeParameters()[0], txt);
                break;
            case LONG:
                val = resultSet.getLong(index);
                break;
            case SHORT:
                val = resultSet.getShort(index);
                break;
            case STRING:
                val = resultSet.getString(index);
                break;
            case OBJECT:
            case ARRAY_BOOLEANS:
            case ARRAY_BYTES:
            case ARRAY_CHARS:
            case ARRAY_DOUBLES:
            case ARRAY_SHORTS:
            case ARRAY_FLOATS:
            case ARRAY_INTEGERS:
            case ARRAY_LONGS:
            case ARRAY_STRINGS:
            case ARRAY_OBJECTS:
                val = JSON.readItem(fieldInfo.getType(), txt);
                break;
            case CHAR:
            default:
                throw new IllegalStateException("Unexpected value: " + fieldType);
        }
    }

    @Override
    public <T> void execute(SQLiteImplementation sq, T data) {
        fieldData.setValue(data, val);
    }
}
