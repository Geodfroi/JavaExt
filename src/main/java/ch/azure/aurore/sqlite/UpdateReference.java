package ch.azure.aurore.sqlite;

import ch.azure.aurore.generics.Generics;
import ch.azure.aurore.json.JSON;

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

    public void forwardReference(SQLiteImplementation sqLite, Object data, List<Object> trace0) {

        boolean canForward = !trace0.contains(data);

        Object fieldValue = getFieldData().getFieldValue(data);
        if (fieldValue == null || !canForward)
            return;

        List<Object> trace1 = new ArrayList<>(trace0);
        trace1.add(data);

        if (getFieldData().getRelationship() == Relationship.ONE_TO_ONE) {
            sqLite.updateItem(fieldValue, trace1);

        } else if (getFieldData().getRelationship() == Relationship.ONE_TO_MANY) {
            List<Object> i = Generics.getCollectionFromField(fieldValue); //.stream().filter(obj -> !trace.contains(obj)).collect(Collectors.toList());
            if (i.size() == 0)
                return;
            for (Object obj : i) {
                if (!sqLite.updateItem(obj, trace1))
                    return;
            }
        }
    }

    @Override
    public String toString() {
        return "UpdateReference - " + getFieldData().getColumnName();
    }
}

