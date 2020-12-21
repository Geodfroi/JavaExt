package ch.azure.aurore.sqlite;

import ch.azure.aurore.generics.Generics;
import ch.azure.aurore.json.JSON;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdateReference extends InsertData {

    public UpdateReference(FieldData fieldData, int i) {
        super(fieldData, i);
    }

    @Override
    public void execute(PreparedStatement statement, Object data) throws SQLException {
        Object value = getFieldData().getValue(data);
        if (getFieldData().getRelationship() == Relationship.ONE_TO_ONE) {
            int id = getFieldData().getRelationId(value);
            String txt = getFieldData().getReferenceStr(id);
            statement.setString(getIndex(), txt);
        } else if (getFieldData().getRelationship() == Relationship.ONE_TO_MANY) {
            Map<Integer, String> map = getFieldData().getRelationIDs(value).getMap();
            if (map.size() == 0)
                statement.setObject(getIndex(), null);

            String txt = JSON.toJSON(map);
            statement.setString(getIndex(), txt);
        }
    }

    public Object getUpdateRef(Object data, List<Object> updateTrack) {
        Object value = getFieldData().getValue(data);
        if (updateTrack.contains(value))
            return null;
        return value;
    }

    public Collection<Object> getUpdateRefs(Object data, List<Object> updateTrack) {
        Object value = getFieldData().getValue(data);
        return Generics.getCollectionFromField(value).stream().
                filter(updateTrack::contains).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "UpdateReference - " + getFieldData().getColumnName();
    }
}