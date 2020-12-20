package ch.azure.aurore.sqlite;

import ch.azure.aurore.generics.Generics;
import ch.azure.aurore.json.JSON;
import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseClass;
import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseName;
import ch.azure.aurore.sqlite.wrapper.annotations.PrimaryKey;
import ch.azure.aurore.strings.Strings;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldData {

    private static final Map<Class<?>, String> fieldTypeToSQL = new HashMap<>();

    static {
        fieldTypeToSQL.put(boolean.class, "NUMERIC");
        fieldTypeToSQL.put(byte.class, "NUMERIC");
        fieldTypeToSQL.put(char.class, "TEXT");
        fieldTypeToSQL.put(short.class, "INTEGER");
        fieldTypeToSQL.put(int.class, "INTEGER");
        fieldTypeToSQL.put(long.class, "INTEGER");
        fieldTypeToSQL.put(float.class, "REAL");
        fieldTypeToSQL.put(double.class, "REAL");

        fieldTypeToSQL.put(Boolean.class, "NUMERIC");
        fieldTypeToSQL.put(Byte.class, "NUMERIC");
        fieldTypeToSQL.put(Character.class, "TEXT");
        fieldTypeToSQL.put(Short.class, "INTEGER");
        fieldTypeToSQL.put(Integer.class, "INTEGER");
        fieldTypeToSQL.put(Long.class, "INTEGER");
        fieldTypeToSQL.put(Float.class, "REAL");
        fieldTypeToSQL.put(Double.class, "REAL");

        fieldTypeToSQL.put(byte[].class, "BLOB");
        fieldTypeToSQL.put(String.class, "TEXT");
    }

    private final Field field;
    private final Method getMethod;
    private final Method setMethod;

    private Relationship relationship = Relationship.NONE;
    private String SQLType = "TEXT";
    private String columnName;
    private boolean convertToJSON;
    private Field relationFieldID;
    private Class<?> internalType;

    public FieldData(Field field, Method getMethod, Method setMethod) {
        this.field = field;
        this.getMethod = getMethod;
        this.setMethod = setMethod;

        if (field.isAnnotationPresent(DatabaseName.class)) {
            DatabaseName fieldAnnotation = field.getAnnotation(DatabaseName.class);
            this.columnName = Strings.isNullOrEmpty(fieldAnnotation.value()) ? field.getName() : fieldAnnotation.value();
        } else
            this.columnName = field.getName();

        Class<?> fieldType = field.getType();

        if (fieldTypeToSQL.containsKey(fieldType)) {
            SQLType = fieldTypeToSQL.get(fieldType);
            return;
        }

        if (fieldType.isAnnotationPresent(DatabaseClass.class)) {
            relationship = Relationship.ONE_TO_ONE;
            columnName += "_ID";
            relationFieldID = getRelationFieldID(fieldType);
            return;
        }

        if (fieldType.isArray() || Collection.class.isAssignableFrom(fieldType))
            if (checkIfInternalData(Generics.getComponentType(field)))
                return;

        // not relational custom class
        convertToJSON = true;
    }

    private static Field getRelationFieldID(Class<?> aClass) {
        for (Field f : aClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(PrimaryKey.class)) {

                if (!f.getType().equals(int.class))
                    throw new RuntimeException("Primary key field [" + f.getName() + "] in [" + aClass.getSimpleName() + "] must be integer field");

                f.setAccessible(true);
                return f;
            }
        }
        throw new RuntimeException("Class [" + aClass.getSimpleName() + "] marked has [DatabaseClass] must have a [Primary Key] integer field");
    }

    //region accessors
    public String getColumnName() {
        return columnName;
    }

    public Field getField() {
        return field;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public String getSQLType() {
        return SQLType;
    }

    //endregion

    private boolean checkIfInternalData(Class<?> internalType) {
        if (internalType.isAnnotationPresent(DatabaseClass.class)) {
            SQLType = "TEXT";
            relationship = Relationship.ONE_TO_MANY;
            columnName += "_IDs";
            relationFieldID = getRelationFieldID(internalType);
            this.internalType = internalType;
            return true;
        }
        return false;
    }

    public void setContentInDatabase(PreparedStatement statement, int i, Object data) throws SQLException {

        Object val = null;

        try {
            val = getMethod.invoke(data);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (val == null) {
            statement.setObject(i, null);
            return;
        }

        if (convertToJSON) {
            statement.setString(i, JSON.toJSON(val));
            return;
        }

        switch (relationship) {
            case NONE:

                switch (SQLType) {
                    case "BLOB":
                        statement.setBytes(i, (byte[]) val);
                        break;
                    case "INTEGER":
                        statement.setInt(i, (int) val);
                        break;
                    case "TEXT":
                        statement.setString(i, (String) val);
                        break;
                    default:
                        throw new IllegalStateException("Can't insert value in database for [" + SQLType + "] SQL type in [setStatement] method");
                }

                return;

            case ONE_TO_ONE:
                int linkID = 0;
                try {
                    linkID = (int) relationFieldID.get(val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (linkID == 0)
                    System.out.println("[" + relationFieldID.getDeclaringClass() + "] id was not initialised when referenced by [" + field.getDeclaringClass() + "] in database");
                DatabaseRef r = new DatabaseRef(linkID, relationFieldID.getDeclaringClass().getSimpleName());
                statement.setString(i, JSON.toJSON(r));
                return;

            case ONE_TO_MANY:
                Map<Integer, String> map = new HashMap<>();
                if (Collection.class.isAssignableFrom(val.getClass())) {
                    //noinspection rawtypes
                    for (Object obj : (Iterable) val) {
                        int id = 0;
                        try {
                            id = (int) relationFieldID.get(obj);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        if (id != 0){
                            map.put(id, obj.getClass().getName());
                        }

                    }
                } else if (val.getClass().isArray()) {
                    for (int n = 0; n < Array.getLength(val); n++) {
                        Object item = Array.get(val, n);
                        int id = 0;
                        try {
                            id = (int) relationFieldID.get(item);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        if (id != 0) map.put(id, item.getClass().getName());
                    }
                }

//                List<DatabaseRef> rf = ints.stream().
//                        map(integer -> new DatabaseRef(integer, relationFieldID.getDeclaringClass().getSimpleName())).
//                        collect(Collectors.toList());

                statement.setString(i, JSON.toJSON(map));
        }
    }

    @Override
    public String toString() {
        return "FieldData{" +
                "columnName='" + columnName + '\'' +
                '}';
    }

    public void NEWSetValueToData(ResultSet resultSet, int i, Object data) throws SQLException {

        if (relationship != Relationship.NONE)
            return;

        Object val;

        if (convertToJSON) {
            String txt = resultSet.getString(i);
            val = JSON.fromJSON(field.getType(), txt);
        } else {
            switch (field.getType().getSimpleName()) {
                case "boolean":
                    val = resultSet.getBoolean(i);
                    break;
                case "int":
                    val = resultSet.getInt(i);
                    break;
                case "String":
                    val = resultSet.getString(i);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + field.getType().getSimpleName());
            }
        }

        setValue(data, val);


//    public static Object getContent(ResultSet resultSet, FieldData fieldData, int index) throws SQLException {
//        throw new RuntimeException();
////        String str;
////        String[] array;
////        switch (columnType.getSimpleName()) {
////
////            case "byte[]":
////                return resultSet.getBytes(index);
////            case "double":
////                return resultSet.getDouble(index);
////            case "double[]":
////                str = resultSet.getString(index);
////                array = str == null ? new String[0] : str.split(Strings.DEFAULT_SEPARATOR);
////                return Conversions.toDoubleArray(array);
////            case "int":
////            case "int[]":
////                str = resultSet.getString(index);
////                array = str == null ? new String[0] : str.split(Strings.DEFAULT_SEPARATOR);
////                return Conversions.toIntArray(array);
////            case "String":
////
////            case "String[]":
////                return resultSet.getString(index).split(Strings.DEFAULT_SEPARATOR);
////            default:
////                throw new RuntimeException("Can't fetch value from database for type [" + columnType.getSimpleName() + "] in [getStatement] method");
////        }
//    }

        //                    switch (fieldsData.getFieldCategory(md.getColumnName(n))){
//                        case primitiveType:
//                            Class<?> type = fieldsData.getPrimitiveFieldType(md.getColumnName(n));
//
//                            fieldsData.setValueToObj(FieldCategory.primitiveType, data, content, md.getColumnName(n));
//                            break;
//                        case hierarchyClass:
//                            int classID = (int)getContent(resultSet, int.class,n);
//                            hierarchyClassFieldIds.put(md.getColumnName(n), classID);
//                            break;
//                    }
    }

    public void setValue(Object obj, Object val) {
        try {
            setMethod.invoke(obj, val);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println("Failed to set field [" + field.getName() + "] for class [" + field.getDeclaringClass().getSimpleName() + "]");
            e.printStackTrace();
        }
    }

    public void setCollectionValue(List<Object> collection, Object data) {

        if (field.getType().isArray()){
            Object array = Array.newInstance(internalType, collection.size());
            for (int n = 0; n < collection.size(); n++) {
                Array.set(array, n, collection.get(n));
            }
            try {
                setMethod.invoke(data, array);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new IllegalStateException("can't set collection in array field");
            }
        }
        else if (Collection.class.isAssignableFrom(field.getType())){
            try {
                setMethod.invoke(data, collection);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new IllegalStateException("can't set collection in list field");
            }
        }
    }
}