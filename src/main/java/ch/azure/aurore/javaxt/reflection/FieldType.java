package ch.azure.aurore.javaxt.reflection;

import java.lang.reflect.Field;

public enum FieldType {
    BOOLEAN("NUMERIC"), BYTE("NUMERIC"), CHAR("TEXT"), DOUBLE("REAL"),
    FLOAT("REAL"), INT("INTEGER"), LONG("INTEGER"), SHORT("INTEGER"),
    STRING("TEXT"), OBJECT("TEXT"),
    ARRAY_BOOLEANS("TEXT"), ARRAY_BYTES("BLOB"), ARRAY_CHARS("TEXT"), ARRAY_DOUBLES("TEXT"),
    ARRAY_FLOATS("TEXT"), ARRAY_INTEGERS("TEXT"), ARRAY_LONGS("TEXT"), ARRAY_SHORTS("TEXT"),
    ARRAY_STRINGS("TEXT"), ARRAY_OBJECTS("TEXT"),
    LIST("TEXT"),
    MAP("TEXT"),
    SET("TEXT");

    private final String _SQLType;

    FieldType(String SQLType) {
        this._SQLType = SQLType;
    }

    public static FieldType getFieldType(Field field) {
        FieldType val;
        switch (field.getType().getSimpleName()) {
            case "boolean":
            case "Boolean":
                val = BOOLEAN;
                break;
            case "boolean[]":
            case "Boolean[]":
                val = ARRAY_BOOLEANS;
                break;
            case "byte":
            case "Byte":
                val = BYTE;
                break;
            case "byte[]":
            case "Byte[]":
                val = ARRAY_BYTES;
                break;
            case "char":
            case "Character":
                val = CHAR;
                break;
            case "char[]":
            case "Character[]":
                val = ARRAY_CHARS;
                break;
            case "double":
            case "Double":
                val = DOUBLE;
                break;
            case "double[]":
            case "Double[]":
                val = ARRAY_DOUBLES;
                break;
            case "float":
            case "Float":
                val = FLOAT;
                break;
            case "float[]":
            case "Float[]":
                val = ARRAY_FLOATS;
                break;
            case "int":
            case "Integer":
                val = INT;
                break;
            case "int[]":
            case "Integer[]":
                val = ARRAY_INTEGERS;
                break;
            case "List":
                val = LIST;
                break;
            case "long":
            case "Long":
                val = LONG;
                break;
            case "long[]":
            case "Long[]":
                val = ARRAY_LONGS;
                break;
            case "Map":
                val = MAP;
                break;
            case "Set":
                val = SET;
                break;
            case "short":
            case "Short":
                val = SHORT;
                break;
            case "short[]":
            case "Short[]":
                val = ARRAY_SHORTS;
                break;
            case "String":
                val = STRING;
                break;
            case "String[]":
                val = ARRAY_STRINGS;
                break;
            default:
                val = OBJECT;
                break;
        }
        return val;
    }

    public String get_SQLType() {
        return _SQLType;
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
            case ARRAY_BOOLEANS:
            case OBJECT:
            case STRING:
            case MAP:
            case LIST:
            case ARRAY_BYTES:
            case ARRAY_INTEGERS:
            case ARRAY_FLOATS:
            case ARRAY_DOUBLES:
            case SET:
            case ARRAY_CHARS:
            case ARRAY_LONGS:
            case ARRAY_SHORTS:
            case ARRAY_STRINGS:
            case ARRAY_OBJECTS:
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
            case ARRAY_BOOLEANS:
            case ARRAY_CHARS:
            case ARRAY_DOUBLES:
            case ARRAY_FLOATS:
            case ARRAY_INTEGERS:
            case ARRAY_LONGS:
            case ARRAY_SHORTS:
            case ARRAY_STRINGS:
            case ARRAY_OBJECTS:
            case ARRAY_BYTES:
            case LIST:
            case SET:
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    public boolean isMap() {
        return this.equals(MAP);
    }

    public boolean isArray() {

        switch (this) {
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
            case STRING:
            case OBJECT:
            case LIST:
            case SET:
            case MAP:
                return false;
            case ARRAY_BOOLEANS:
            case ARRAY_BYTES:
            case ARRAY_CHARS:
            case ARRAY_DOUBLES:
            case ARRAY_FLOATS:
            case ARRAY_INTEGERS:
            case ARRAY_LONGS:
            case ARRAY_SHORTS:
            case ARRAY_STRINGS:
            case ARRAY_OBJECTS:
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}