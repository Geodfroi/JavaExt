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

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FieldData {

    private static final String ID_FIELD = "id";

    private final FieldInfo f;
    private Relationship relationship = Relationship.NONE;
    private String columnName;
    private boolean convertToJSON;
    private FieldInfo relationFieldID;

    public FieldData(FieldInfo f) {
        this.f = f;
        DatabaseName fieldAnnotation = f.getAnnotationIfPresent(DatabaseName.class);
        this.columnName = fieldAnnotation == null ? f.getName() : (Strings.isNullOrEmpty(fieldAnnotation.value()) ? f.getName() : fieldAnnotation.value());

        FieldType ffieldType = getFieldType();
        if (ffieldType.isPrimitiveOrString() || ffieldType == FieldType.ARRAY_BYTES)
            return;

        if (ffieldType.isAnnotationPresent(DatabaseClass.class)) {
            relationship = Relationship.ONE_TO_ONE;
            columnName += "_ID";
            relationFieldID = getRelationFieldID(f.getType());
            return;
        }

        if (ffieldType.isCollection()){
            if (ffieldType.getTypeParameters()[0].isAnnotationPresent(DatabaseClass.class)) {
                relationship = Relationship.ONE_TO_MANY_COLLECTION;
                columnName += "_IDs";
                relationFieldID = getRelationFieldID(ffieldType.getTypeParameters()[0]);
                return;
            }
        }
        if (ffieldType.isMap()){
            if (ffieldType.getTypeParameters()[1].isAnnotationPresent(DatabaseClass.class)) {
                relationship = Relationship.ONE_TO_MANY_MAP;
                throw new IllegalStateException("not implemented");
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

    public String getName() {
        return f.getName();
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public String getSQLType() {
        return getFieldType().get_SQLType();
    }

    //endregion

    public int getRelationId(Object fieldValue) {
        return (int) relationFieldID.getAccessor().invoke(fieldValue);
    }

    IDsResult getRelationIDs(Object fieldValue) {
        List<DatabaseRef> list = new ArrayList<>();
        boolean hasZero = false;

        for (Object item : Generics.getCollectionFromField(fieldValue)) {
            int id = getRelationId(item);
            list.add(new DatabaseRef(id, item.getClass().getName()));
            if (id == 0)
                hasZero = true;
        }

        return new IDsResult(hasZero, list);
    }

    public String getReferenceStr(int id) {
        DatabaseRef r = new DatabaseRef(id, relationFieldID.getDeclaringClass().getName());
        return JSON.toJSON(r);
    }

    public Object getFieldValue(Object data) {
        return f.getAccessor().invoke(data);
    }

    public InsertData prepareInsert(int i, Object data, InsertOperation insertOperation) {

        Object val = getFieldValue(data);
        if (val == null)
            return new InsertField(this, i, null, "setObject");

        if (convertToJSON)
            return new InsertField(this, i, JSON.toJSON(val), "setString");

        if (relationship == Relationship.NONE) {
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
        }

        if (insertOperation == InsertOperation.INSERT_FOR_NEW_ENTRY)
            return new InsertField(this, i, null, "setObject");

        switch (relationship) {
            case ONE_TO_ONE:
                int linkID = getRelationId(val);
                if (linkID != 0) {
                    String str = getReferenceStr(linkID);
                    return new InsertField(this, i, str, "setString");
                } else
                    return new UpdateReference(this, i);

            case ONE_TO_MANY_COLLECTION:
                IDsResult r = getRelationIDs(val);
                if (r.getList().size() == 0)
                    return new InsertField(this, i, null, "setObject");
                if (r.AreAllIDsSet())
                    return new InsertField(this, i, JSON.toJSON(r.getList()), "setString");

                return new UpdateReference(this, i);
            default:
                throw new IllegalStateException("Unexpected value: " + relationship);
        }
    }

    public void setCollectionValue(List<Object> collection, Object data) {
        if (f.getType().isArray()) {
            Object array = Array.newInstance(f.getFieldType().getTypeParameters()[0], collection.size());
            for (int n = 0; n < collection.size(); n++) {
                Array.set(array, n, collection.get(n));
            }
            f.getMutator().invoke(data, array);

        } else if (Collection.class.isAssignableFrom(f.getType()))
            f.getMutator().invoke(data, collection);
    }

    public void setValue(Object obj, Object val) {
        f.getMutator().invoke(obj, val);
    }

    @Override
    public String toString() {
        return "FieldData{" +
                "columnName='" + columnName + '\'' +
                '}';
    }

    public PullData preparePull(ResultSet resultSet, int n) throws SQLException {
        if (f == null)
            return null;

        switch (relationship) {
            case NONE:
                return new PullField(this, resultSet, n);
            case ONE_TO_ONE:
                String txt = resultSet.getString(n);
                if (!Strings.isNullOrEmpty(txt)) {
                    DatabaseRef rf = JSON.readValue(DatabaseRef.class, txt);
                    return new PullReference(this, rf);
                }
                return new PullReference(this);
            case ONE_TO_MANY_COLLECTION:
                String arrayTxt = resultSet.getString(n);
                if (!Strings.isNullOrEmpty(arrayTxt)) {
                    List<DatabaseRef> list = JSON.readList(DatabaseRef.class, arrayTxt);
                    return new PullReference(this, list);
                }
                return new PullReference(this);
            default:
                break;
        }
        throw new IllegalStateException("Unexpected value: " + relationship);
    }

    public FieldType getFieldType() {
        return f.getFieldType();
    }

    public enum InsertOperation {
        INSERT_FOR_NEW_ENTRY,
        INSERT_FOR_UPDATE,
    }
}

class IDsResult {

    private final boolean hasZero;
    private final List<DatabaseRef> list;

    public IDsResult(boolean hasZero, List<DatabaseRef> list) {
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