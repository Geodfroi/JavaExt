package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.generics.Generics;
import ch.azure.aurore.javaxt.json.API.JSON;
import ch.azure.aurore.javaxt.reflection.ClassInfo;
import ch.azure.aurore.javaxt.reflection.FieldInfo;
import ch.azure.aurore.javaxt.reflection.FieldType;
import ch.azure.aurore.javaxt.reflection.Reflection;
import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseClass;
import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseName;
import ch.azure.aurore.javaxt.strings.Strings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FieldData {

    private static final String ID_FIELD = "id";

    private final FieldInfo fieldInfo;
    private Relationship relationship = Relationship.NONE;
    private String columnName;
    private boolean convertToJSON;
    private FieldInfo relationFieldID;

    public FieldData(FieldInfo f) {
        this.fieldInfo = f;
        DatabaseName fieldAnnotation = f.getAnnotationIfPresent(DatabaseName.class);
        this.columnName = fieldAnnotation == null ? f.getName() : (Strings.isNullOrEmpty(fieldAnnotation.value()) ? f.getName() : fieldAnnotation.value());

        if (columnName.equals("world")){
            System.out.println("huh");
        }

        FieldType fieldType = getFieldType();
        if (fieldType.isPrimitiveOrString() || fieldType == FieldType.ARRAY_BYTES)
            return;

        if (Reflection.isAnnotationPresent(fieldInfo.getType(), DatabaseClass.class)){ //fieldType.isAnnotationPresent(DatabaseClass.class)) {
            relationship = Relationship.ONE_TO_ONE;
            columnName += "_ID";
            relationFieldID = getRelationFieldID(f.getType());
            return;
        }

        if (fieldType.isCollection()) {
            if (Reflection.isAnnotationPresent(fieldInfo.getTypeParameters()[0], DatabaseClass.class)) {
                relationship = Relationship.ONE_TO_MANY_COLLECTION;
                columnName += "_IDs";
                relationFieldID = getRelationFieldID(fieldInfo.getTypeParameters()[0]);
                return;
            }
        }

        if (fieldType.isMap()) {
            if (Reflection.isAnnotationPresent(fieldInfo.getTypeParameters()[0], DatabaseClass.class))
                throw new IllegalStateException("[" + fieldInfo.getTypeParameters()[0].getSimpleName() + "] object marked as [DatabaseClass] cannot be a map key");
            if (Reflection.isAnnotationPresent(fieldInfo.getTypeParameters()[1], DatabaseClass.class)) {
                relationship = Relationship.ONE_TO_MANY_MAP;
                columnName += "_IDs";
                relationFieldID = getRelationFieldID(fieldInfo.getTypeParameters()[1]);
                return;
            }
        }

        convertToJSON = true; //<- not relational custom class
    }

    private static FieldInfo getRelationFieldID(Class<?> clazz) {
        ClassInfo classInfo = Reflection.getInfo(clazz);
        FieldInfo f = classInfo.getField(ID_FIELD);
        if (f != null)
            return f;
        throw new RuntimeException("Class [" + clazz.getSimpleName() + "] marked has [DatabaseClass] must have a [Primary Key] integer field");
    }

    //region accessors
    public String getColumnName() {
        return columnName;
    }

    public FieldType getFieldType() {
        return fieldInfo.getFieldType();
    }

    public String getName() {
        return fieldInfo.getName();
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public String getSQLType() {
        return getFieldType().get_SQLType();
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    //endregion

    public int getRelationId(Object fieldValue) {
        return (int) relationFieldID.getAccessor().invoke(fieldValue);
    }

    IDsMap getMapIDs(Object fieldValue) {
        boolean hasZero = false;
        HashMap<Object, DatabaseRef> resMap = new HashMap<>();
        if (!Map.class.isAssignableFrom(fieldValue.getClass()))
            throw new IllegalStateException("Invalid [getMapIDs] method called on [" + fieldValue + "] field value");

        @SuppressWarnings("unchecked")
        Map<Object, Object> map = (Map<Object, Object>) fieldValue;
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            int id = getRelationId(value);
            resMap.put(key, new DatabaseRef(id, value.getClass().getName()));
            if (id == 0)
                hasZero = true;
        }

        return new IDsMap(hasZero, resMap);
    }

    IDsCollection getCollectionIDs(Object fieldValue) {
        List<DatabaseRef> list = new ArrayList<>();
        boolean hasZero = false;

        for (Object item : Generics.getCollectionFromField(fieldValue)) {
            int id = getRelationId(item);
            list.add(new DatabaseRef(id, item.getClass().getName()));
            if (id == 0)
                hasZero = true;
        }

        return new IDsCollection(hasZero, list);
    }

    public String getReferenceStr(int id) {
        DatabaseRef r = new DatabaseRef(id, fieldInfo.getType().getName());
        return JSON.toJSON(r);
    }

    public Object getFieldValue(Object data) {
        return fieldInfo.getAccessor().invoke(data);
    }

    public InsertData prepareInsert(int i, Object data, InsertOperation insertOperation) {

        Object val = getFieldValue(data);
        if (val == null || insertOperation == InsertOperation.INSERT_FOR_NEW_ENTRY)
            return new InsertField(this, i, null, "setObject");

//        if (insertOperation == InsertOperation.INSERT_FOR_NEW_ENTRY)
//            return new InsertField(this, i, null, "setObject");

        if (convertToJSON)
            return new InsertField(this, i, JSON.toJSON(val), "setString");

        switch (relationship) {
            case NONE:
                switch (getFieldType().get_SQLType()) {
                    case "BLOB":
                        return new InsertField(this, i, val, "setBytes");
                    case "INTEGER":
                        return new InsertField(this, i, val, "setInt");
                    case "TEXT":
                        return new InsertField(this, i, val, "setString");
                    default:
                        break;
                }
                throw new IllegalStateException("Can't insert value in database for [" + getFieldType().get_SQLType() + "] SQL type in [setStatement] method");

            case ONE_TO_ONE:
                int linkID = getRelationId(val);
                if (linkID != 0) {
                    String str = getReferenceStr(linkID);
                    return new InsertField(this, i, str, "setString");
                } else
                    return new UpdateReference(this, i);

            case ONE_TO_MANY_COLLECTION:
                IDsCollection result = getCollectionIDs(val);
                if (result.getList().size() == 0)
                    return new InsertField(this, i, null, "setObject");
                if (result.AreAllIDsSet())
                    return new InsertField(this, i, JSON.toJSON(result.getList()), "setString");

                return new UpdateReference(this, i);
            case ONE_TO_MANY_MAP:
                var mapResult = getMapIDs(val);
                if (mapResult.getMap().size() == 0)
                    return new InsertField(this, i, null, "setObject");
                if (mapResult.AreAllIDsSet())
                    return new InsertField(this, i, JSON.toJSON(mapResult.getMap()), "setString");
                return new UpdateReference(this, i);
            default:
                throw new IllegalStateException("Unexpected value: " + relationship);
        }
    }

    public PullData preparePull(ResultSet resultSet, int n) throws SQLException {
        if (fieldInfo == null)
            return null;
        String txt;
        switch (relationship) {
            case NONE:
                return new PullField(this, resultSet, n);
            case ONE_TO_ONE:
                txt = resultSet.getString(n);
                if (!Strings.isNullOrEmpty(txt)) {
                    DatabaseRef rf = JSON.readObjectValue(DatabaseRef.class, txt);
                    return new PullReference(this, rf);
                }
                return new PullReference(this);
            case ONE_TO_MANY_COLLECTION:
                txt = resultSet.getString(n);
                if (!Strings.isNullOrEmpty(txt)) {
                    List<DatabaseRef> list = JSON.readList(DatabaseRef.class, txt);
                    return new PullReference(this, list);
                }
                return new PullReference(this);

            case ONE_TO_MANY_MAP:
                txt = resultSet.getString(n);
                if (!Strings.isNullOrEmpty(txt)) {
                    var map = JSON.readMap(Object.class, DatabaseRef.class, txt);
                    return new PullReference(this, map);
                }
                return new PullReference(this);
            default:
                break;
        }
        throw new IllegalStateException("Unexpected value: " + relationship);
    }

    public void setValue(Object obj, Object val) {
        fieldInfo.getMutator().invoke(obj, val);
    }

    @Override
    public String toString() {
        return "FieldData{" +
                "columnName='" + columnName + '\'' +
                '}';
    }

    public enum InsertOperation {
        INSERT_FOR_NEW_ENTRY,
        INSERT_FOR_UPDATE,
    }
}

class IDsCollection {

    private final boolean hasZero;
    private final List<DatabaseRef> list;

    public IDsCollection(boolean hasZero, List<DatabaseRef> list) {
        this.hasZero = hasZero;
        this.list = list;
    }

    public boolean AreAllIDsSet() {
        return !hasZero;
    }

    public List<DatabaseRef> getList() {
        return list;
    }
}

class IDsMap {
    private final Boolean hasZero;
    private final Map<Object, DatabaseRef> map;

    public IDsMap(Boolean hasZero, Map<Object, DatabaseRef> map) {

        this.hasZero = hasZero;
        this.map = map;
    }

    public boolean AreAllIDsSet() {
        return !hasZero;
    }

    public Map<Object, DatabaseRef> getMap() {
        return map;
    }
}