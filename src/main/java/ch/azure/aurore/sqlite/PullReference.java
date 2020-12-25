package ch.azure.aurore.sqlite;

import ch.azure.aurore.reflection.Reflection;

import java.util.ArrayList;
import java.util.List;

public class PullReference extends PullData {
    private DatabaseRef rf = null;
    private List<DatabaseRef> refs = new ArrayList<>();

    public PullReference(FieldData fieldData) {
        super(fieldData);
    }

    public PullReference(FieldData fieldData, DatabaseRef rf) {
        super(fieldData);
        this.rf = rf;
    }

    public PullReference(FieldData fieldData, List<DatabaseRef> refs) {
        super(fieldData);
        this.refs = refs;
    }

    @Override
    public <T> void execute(SQLiteImplementation sq, T data) {
        int id;
        Object obj;
        switch (fieldData.getRelationship()) {
            case ONE_TO_ONE:
                if (rf != null){
                    id = rf.getId();
                    Class<?> clazz = Reflection.getClass(rf.getType());
                    obj = sq.fetchReferencedObject(clazz, id);
                    if (obj == null)
                        obj = sq.queryItem(clazz, id);

                    sq.putInMemory(obj, id);
                    fieldData.setValue(data, obj);
                }
                break;
            case ONE_TO_MANY:
                List<Object> collection = new ArrayList<>();
                if (refs.size() > 0){
                    for (DatabaseRef entry : refs) {
                        id = entry.getId();
                        Class<?> clazz = Reflection.getClass(entry.getType());
                        obj = sq.fetchReferencedObject(clazz, id);
                        if (obj == null)
                            obj = sq.queryItem(clazz, id);

                        sq.putInMemory(obj, id);
                        collection.add(obj);
                    }
                    fieldData.setCollectionValue(collection, data);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + fieldData.getRelationship());
        }
    }
}