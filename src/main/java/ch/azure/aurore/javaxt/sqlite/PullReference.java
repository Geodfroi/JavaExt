package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.reflection.FieldInfo;
import ch.azure.aurore.javaxt.reflection.FieldType;
import ch.azure.aurore.javaxt.reflection.Reflection;

import java.lang.reflect.Array;
import java.util.*;

public class PullReference extends PullData {
    private Map<Object, DatabaseRef> refMap = new HashMap<>();
    private DatabaseRef rf = null;
    private List<DatabaseRef> refList = new ArrayList<>();

    public PullReference(FieldData fieldData) {
        super(fieldData);
    }

    public PullReference(FieldData fieldData, DatabaseRef rf) {
        super(fieldData);
        this.rf = rf;
    }

    public PullReference(FieldData fieldData, List<DatabaseRef> refs) {
        super(fieldData);
        this.refList = refs;
    }

    public PullReference(FieldData fieldData, Map<Object, DatabaseRef> map) {
        super(fieldData);
        this.refMap = map;
    }

    @Override
    public <T> void execute(SQLiteImplementation sq, T data) {
        int id;
        Object obj;
        switch (fieldData.getRelationship()) {
            case ONE_TO_ONE:
                if (rf != null) {
                    id = rf.getId();
                    Class<?> clazz = Reflection.getClass(rf.getType());
                    obj = sq.fetchReferencedObject(clazz, id);
                    if (obj == null)
                        obj = sq.queryItem(clazz, id);

                    sq.putInMemory(obj, id);
                    fieldData.setValue(data, obj);
                }
                break;
            case ONE_TO_MANY_COLLECTION:
                List<Object> collection = new ArrayList<>();
                if (refList.size() == 0)
                    return;

                for (DatabaseRef entry : refList) {
                    id = entry.getId();
                    Class<?> clazz = Reflection.getClass(entry.getType());
                    obj = sq.fetchReferencedObject(clazz, id);
                    if (obj == null)
                        obj = sq.queryItem(clazz, id);

                    sq.putInMemory(obj, id);
                    collection.add(obj);
                }
                setCollectionValue(collection, data);

                break;
            case ONE_TO_MANY_MAP:
                Map<Object, Object> map = new HashMap<>();
                if (refMap.size() == 0)
                    return;

                for (Object key : refMap.keySet()) {
                    DatabaseRef ref = refMap.get(key);
                    id = ref.getId();
                    Class<?> clazz = Reflection.getClass(ref.getType());
                    obj = sq.fetchReferencedObject(clazz, id);
                    if (obj == null)
                        obj = sq.queryItem(clazz, id);

                    sq.putInMemory(obj, id);
                    map.put(key, obj);
                }
                setMapValue(map, data);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + fieldData.getRelationship());
        }
    }

    public void setCollectionValue(List<Object> collection, Object data) {
        FieldType fieldType = fieldData.getFieldType();
        FieldInfo fieldInfo = fieldData.getFieldInfo();

        if (fieldType.isArray()) {
            Object array = Array.newInstance(fieldInfo.getTypeParameters()[0], collection.size());
            for (int n = 0; n < collection.size(); n++) {
                Array.set(array, n, collection.get(n));
            }
            fieldInfo.getMutator().invoke(data, array);

        } else if (Collection.class.isAssignableFrom(fieldInfo.getType()))
            fieldInfo.getMutator().invoke(data, collection);
    }

    @SuppressWarnings("rawtypes")
    public void setMapValue(Map map, Object data) {
        FieldInfo fieldInfo = fieldData.getFieldInfo();
        fieldInfo.getMutator().invoke(data, map);
    }
}