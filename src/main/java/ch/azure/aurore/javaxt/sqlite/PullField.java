package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.json.API.JSON;
import ch.azure.aurore.javaxt.reflection.FieldType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PullField extends PullData {

    private final Object val;

    public PullField(FieldData f, ResultSet resultSet, int index) throws SQLException {
        super(f);

        String txt = resultSet.getString(index);
        FieldType fieldType = f.getFieldType();
        switch (fieldType) {

            case BOOLEAN:
                val = resultSet.getBoolean(index);
                break;
            case INT:
                val = resultSet.getInt(index);
                break;
            case LIST:
                val = JSON.readList(fieldType.getTypeParameters()[0], txt);
                break;
            case MAP:
                val = JSON.readMap(fieldType.getTypeParameters()[0], fieldType.getTypeParameters()[1], txt);
                break;
            case SET:
                val = JSON.readSet(fieldType.getTypeParameters()[0], txt);
                break;
            case STRING:
                val = resultSet.getString(index);
                break;
            default:
                val = JSON.readValue(fieldType.getType(), txt);
                break;
        }

//            switch (f.getType().getSimpleName()) {
//        } else {
//            switch (f.getType().getSimpleName()) {
//                case "byte[]":
//                    val = resultSet.getBytes(index);
//                    break;
//                default:
//                    throw new IllegalStateException("Unexpected value: " + f.getType().getSimpleName());
//            }
    }

    @Override
    public <T> void execute(SQLiteImplementation sq, T data) {
        fieldData.setValue(data, val);
    }
}
