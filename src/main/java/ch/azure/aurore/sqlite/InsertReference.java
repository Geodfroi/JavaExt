package ch.azure.aurore.sqlite;

import ch.azure.aurore.sqlite.wrapper.SQLite;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertReference extends InsertData{

    private Object object;
    private int id;

    public InsertReference(FieldData fieldData, int i) {
        super(fieldData, i);
    }

    @Override
    public void execute(PreparedStatement statement, Object data) throws SQLException {
        if (getFieldData().getRelationship() == Relationship.ONE_TO_ONE){
            String str = getFieldData().getReferenceStr(id);
            statement.setString(getIndex(), str);
        }else{
            throw new IllegalStateException("execute");
        }
    }

    public boolean processRef(SQLite sl, Object data) {
        if (getFieldData().getRelationship() == Relationship.ONE_TO_ONE){
             object = getFieldData().getValue(data);
             id = sl.insertItem(object);

//                if (id != -1){
//                    var str = fieldData.getReferenceStr(id);
//                   // fieldData.setValue(data, str);
//                }
                return id != -1;
        }else{
            throw new IllegalStateException("insertReferences");
        }
    }

}
