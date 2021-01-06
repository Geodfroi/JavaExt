package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.generics.Generics;
import ch.azure.aurore.javaxt.json.API.JSON;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateReference extends InsertData {

    public UpdateReference(FieldData fieldData, int i) {
        super(fieldData, i);
    }

    @Override
    public void execute(PreparedStatement statement, Object data) throws SQLException {
        Object value = getFieldData().getFieldValue(data);

        String txt;
        switch (getFieldData().getRelationship()) {
            case NONE:
                break;
            case ONE_TO_ONE:
                int id = getFieldData().getRelationId(value);
                txt = getFieldData().getReferenceStr(id);
                statement.setString(getIndex(), txt);
                break;
            case ONE_TO_MANY_COLLECTION:
                List<DatabaseRef> list = getFieldData().getRelationIDs(value).getList();
                if (list.size() == 0)
                    statement.setObject(getIndex(), null);
                txt = JSON.toJSON(list);
                statement.setString(getIndex(), txt);
                break;
            case ONE_TO_MANY_MAP:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getFieldData().getRelationship());
        }
    }

    public void forwardReference(SQLiteImplementation sqLite, Object data, List<Object> trace0) {

        boolean canForward = !trace0.contains(data);

        Object fieldValue = getFieldData().getFieldValue(data);
        if (fieldValue == null || !canForward)
            return;

        List<Object> trace1 = new ArrayList<>(trace0);
        trace1.add(data);

        switch (getFieldData().getRelationship()) {

            case NONE:
                break;
            case ONE_TO_ONE:
                sqLite.updateItem(fieldValue, trace1);
                break;
            case ONE_TO_MANY_COLLECTION:
                List<Object> i = Generics.getCollectionFromField(fieldValue); //.stream().filter(obj -> !trace.contains(obj)).collect(Collectors.toList());
                if (i.size() == 0)
                    return;
                for (Object obj : i) {
                    if (!sqLite.updateItem(obj, trace1))
                        return;
                }
                break;
            case ONE_TO_MANY_MAP:
                throw new IllegalStateException("Unexpected value: " + getFieldData().getRelationship());

            default:
                throw new IllegalStateException("Unexpected value: " + getFieldData().getRelationship());
        }
    }

    @Override
    public String toString() {
        return "UpdateReference - " + getFieldData().getColumnName();
    }
}

