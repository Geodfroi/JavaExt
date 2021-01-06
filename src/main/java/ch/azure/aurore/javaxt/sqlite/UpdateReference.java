package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.generics.Generics;
import ch.azure.aurore.javaxt.json.API.JSON;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
                List<DatabaseRef> list = getFieldData().getCollectionIDs(value).getList();
                if (list.size() == 0)
                    statement.setObject(getIndex(), null);
                else {
                    txt = JSON.toJSON(list);
                    statement.setString(getIndex(), txt);
                }
                break;
            case ONE_TO_MANY_MAP:
                @SuppressWarnings("rawtypes")
                Map map = getFieldData().getMapIDs(value).getMap();
                if (map.size() == 0)
                    statement.setObject(getIndex(), null);
                else {
                    txt = JSON.toJSON(map);
                    statement.setString(getIndex(), txt);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getFieldData().getRelationship());
        }
    }

    public void forwardReference(SQLiteImplementation sqLite, Object data, QueryTrace trace) {

        boolean canForward = !trace.contains(data);

        Object fieldValue = getFieldData().getFieldValue(data);
        if (fieldValue == null || !canForward)
            return;

        trace.add(data);

        switch (getFieldData().getRelationship()) {

            case NONE:
                break;
            case ONE_TO_ONE:
                sqLite.updateItem(fieldValue, trace);
                break;
            case ONE_TO_MANY_COLLECTION:
                for (Object obj : Generics.getCollectionFromField(fieldValue)) {
                    if (!sqLite.updateItem(obj, trace))
                        return;
                }
                break;
            case ONE_TO_MANY_MAP:
                //noinspection rawtypes
                for (Object obj : ((Map) fieldValue).values()) {
                    if (!sqLite.updateItem(obj, trace))
                        return;
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + getFieldData().getRelationship());
        }
    }

    @Override
    public String toString() {
        return "UpdateReference - " + getFieldData().getColumnName();
    }
}

