package ch.azure.aurore.sqlite;

import ch.azure.aurore.generics.Generics;
import ch.azure.aurore.json.JSON;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateReference extends InsertData {

    public UpdateReference(FieldData fieldData, int i) {
        super(fieldData, i);
    }

    @Override
    public void execute(PreparedStatement statement, Object data, boolean isModified) throws SQLException {
        if (!isModified)
            return;

        Object value = getFieldData().getFieldValue(data);
        if (getFieldData().getRelationship() == Relationship.ONE_TO_ONE) {
            int id = getFieldData().getRelationId(value);
            String txt = getFieldData().getReferenceStr(id);
            statement.setString(getIndex(), txt);
        } else if (getFieldData().getRelationship() == Relationship.ONE_TO_MANY) {
            List<DatabaseRef> list = getFieldData().getRelationIDs(value).getList();
            if (list.size() == 0)
                statement.setObject(getIndex(), null);

            String txt = JSON.toJSON(list);
            statement.setString(getIndex(), txt);
        }
    }

    public boolean forwardReference(SQLiteImplementation sqLite, Object data, List<Object> trace) {
        Object fieldValue = getFieldData().getFieldValue(data);
        if (fieldValue == null)
            return false;

        if (getFieldData().getRelationship() == Relationship.ONE_TO_ONE) {
            if (!trace.contains(fieldValue))
                return sqLite.updateItem(fieldValue, trace);
            return false;

        } else if (getFieldData().getRelationship() == Relationship.ONE_TO_MANY) {
            List<Object> i = Generics.getCollectionFromField(fieldValue).stream().
                    filter(o -> !trace.contains(0)).collect(Collectors.toList());
            if (i.size() == 0)
                return true;
            for (Object obj : i) {
                if (!sqLite.updateItem(obj, trace))
                    return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "UpdateReference - " + getFieldData().getColumnName();
    }
}

