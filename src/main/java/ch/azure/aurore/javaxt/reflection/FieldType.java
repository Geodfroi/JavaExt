package ch.azure.aurore.javaxt.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

public enum FieldType {
    BOOLEAN("NUMERIC"), BYTE("NUMERIC"), CHAR("TEXT"), DOUBLE("REAL"),
    FLOAT("REAL"), INT("INTEGER"), LONG("INTEGER"), SHORT("INTEGER"),
    STRING("TEXT"), OBJECT("TEXT"),
    ARRAY_BYTES("BLOB"), ARRAY("TEXT"), LIST("TEXT"), MAP("TEXT"), SET("TEXT");

    private final String _SQLType;
    private Class<?>[] typeParameters;
    private Field field;

    FieldType(String SQLType) {
        this._SQLType = SQLType;
    }

    public static FieldType getFieldType(Field field) {

        FieldType val;
        Class<?>[] typeParameters = new Class[0];

        switch (field.getType().getSimpleName()) {
            case "boolean":
            case "Boolean":
                val = BOOLEAN;
                break;
            case "boolean[]":
            case "Boolean[]":
                val = ARRAY;
                typeParameters = new Class[]{boolean.class};
                break;
            case "byte":
            case "Byte":
                val = BYTE;
                break;
            case "byte[]":
            case "Byte[]":
                val = ARRAY_BYTES;
                typeParameters = new Class[]{byte.class};
                break;
            case "char":
            case "Character":
                val = CHAR;
                break;
            case "char[]":
            case "Character[]":
                val = ARRAY;
                typeParameters = new Class[]{char.class};
                break;
            case "double":
            case "Double":
                val = DOUBLE;
                break;
            case "double[]":
            case "Double[]":
                val = ARRAY;
                typeParameters = new Class[]{double.class};
                break;
            case "float":
            case "Float":
                val = FLOAT;
                break;
            case "float[]":
            case "Float[]":
                val = ARRAY;
                typeParameters = new Class[]{float.class};
                break;
            case "int":
            case "Integer":
                val = INT;
                break;
            case "int[]":
            case "Integer[]":
                val = ARRAY;
                typeParameters = new Class[]{int.class};
                break;
            case "List":
                typeParameters = getTypeParameters(field);
                val = LIST;
                break;
            case "long":
            case "Long":
                val = LONG;
                break;
            case "long[]":
            case "Long[]":
                val = ARRAY;
                typeParameters = new Class[]{long.class};
                break;
            case "Map":
                typeParameters = getTypeParameters(field);
                val = MAP;
                break;
            case "Set":
                typeParameters = getTypeParameters(field);
                val = SET;
                break;
            case "short":
            case "Short":
                val = SHORT;
                break;
            case "short[]":
            case "Short[]":
                val = ARRAY;
                typeParameters = new Class[]{short.class};
                break;
            case "String":
                val = STRING;
                break;
            case "String[]":
                val = ARRAY;
                typeParameters = new Class[]{String.class};
                break;
            default:
                val = OBJECT;
                break;
        }
        val.setField(field);
        val.setParameters(typeParameters);
        return val;
    }


    private static Class<?>[] getTypeParameters(Field field) {
        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        return Arrays.stream(stringListType.getActualTypeArguments()).
                map(type -> (Class<?>) type).
                toArray(Class<?>[]::new);
    }

    public String get_SQLType() {
        return _SQLType;
    }

    private void setField(Field field) {
        this.field = field;
    }

    public Class<?>[] getTypeParameters() {
        return typeParameters;
    }

    private void setParameters(Class<?>[] typeParameters) {
        this.typeParameters = typeParameters;
    }

    public Class<?> getType() {
        return field.getType();
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
        return field.isAnnotationPresent(clazz);
    }

    public boolean isPrimitiveOrString() {
        return isPrimitive() || this.equals(STRING);
    }

    public boolean isPrimitive() {
        switch (this) {
            case BOOLEAN:
            case LONG:
            case INT:
            case FLOAT:
            case DOUBLE:
            case CHAR:
            case BYTE:
            case SHORT:
                return true;
            case OBJECT:
            case STRING:
            case MAP:
            case LIST:
            case ARRAY:
            case ARRAY_BYTES:
            case SET:
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
    public boolean isCollectionOrMap() {
        return this.equals(MAP) || isCollection();
    }

    public boolean isCollection() {
        switch (this) {
            case BOOLEAN:
            case CHAR:
            case FLOAT:
            case LONG:
            case SHORT:
            case STRING:
            case OBJECT:
            case INT:
            case DOUBLE:
            case MAP:
            case BYTE:
                return false;
            case ARRAY_BYTES:
            case LIST:
            case SET:
            case ARRAY:
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    public boolean isMap() {
        return this.equals(MAP);
    }
}