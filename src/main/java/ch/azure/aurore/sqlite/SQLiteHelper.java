package ch.azure.aurore.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteHelper {
    public static void createTable(Connection conn, FieldsData fieldsData) {
        String str = SQLiteHelper.composeCreateClassStatement(fieldsData);
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.execute(str);
        } catch (SQLException e) {
            try {
                System.out.println("failed to create [" + fieldsData.getClassName() + "] table");
                if (statement != null)
                    statement.close();
                e.printStackTrace();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static boolean checkMissingFields(Connection conn, FieldsData fieldsData, DatabaseMetaDataWrapper m) {
        //  DatabaseMetaDataWrapper m = getMetadata();
        boolean modified = false;

        for (var e : fieldsData.getFields()) {

            String type = e.getSQLType();

            if (!m.checkColumn(fieldsData.getClassName(), e.getColumnName())) {

                String str = composeAddColumnStatement(fieldsData.getClassName(), e.getColumnName(), e.getSQLType());
                Statement s = null;
                try {
                    s = conn.createStatement();
                    s.execute(str);
                } catch (SQLException e0) {
                    System.out.println("Failed to insert [" + e.getColumnName() + "] column in [" + fieldsData.getClassName() + "]");
                    e0.printStackTrace();
                } finally {
                    try {
                        if (s != null)
                            s.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                insertColumn(conn, fieldsData.getClassName(), e.getColumnName(), type);
                modified = true;
            }
        }

//        for (String fieldName: fieldsData.getFieldNames(FieldCategory.ALL)) {
//
//            String type;
//            switch (fieldsData.getFieldCategory(fieldName)) {
//                case ONE_TO_ONE_HIERARCHY:
//                    type = getSQLType(int.class);
//                    break;
//                case ONE_TO_MANY_HIERARCHY:
//                    type = getSQLType(String.class);
//                    break;
//                case PRIMITIVE_TYPE:
//                    Field f = fieldsData.getField(FieldCategory.PRIMITIVE_TYPE, fieldName);
//                    type = getSQLType(f.getType());
//                    break;
//                default:
//                    throw new RuntimeException("Error checking [" + fieldsData.getClassName() + "] missing fields");
//            }
//
//            if (!m.checkColumn(fieldsData.getClassName(), fieldName)){
//                insertColumn(conn, fieldsData.getClassName(), fieldName, type);
//                modified = true;
//            }
//        }
//
////        for (Map.Entry<String, Field> i : fieldsData.getFields(FieldCategory.primitiveType)) {
////            String fieldName = i.getKey();
////            Field f = i.getValue();
////            if (!m.checkColumn(fieldsData.getClassName(), fieldName, f.getType())) {
////                insertColumn(conn, fieldsData.getClassName(), fieldName, f.getType());
////                modified = true;
////            }
////        }
////
////        for (String fieldName:fieldsData.getFieldNames(FieldCategory.hierarchyClass)){
////            if (!m.checkColumn(fieldsData.getClassName(), fieldName, int.class)){
////                insertColumn(conn, fieldsData.getClassName(), fieldName, int.class);
////                modified = true;
////            }
////        }
        return modified;
    }

    private static String composeAddColumnStatement(String className, String columnName, String type) {
        String str = "ALTER TABLE " +
                className +
                " ADD " +
                columnName +
                " " +
                type;
        System.out.println(str);
        return str;
    }

    public static void insertColumn(Connection conn, String className, String name, String type) {

    }

    //region compose statements
    public static String composeCreateClassStatement(FieldsData fieldsData) {
        StringBuilder str = new StringBuilder();
        str.append("CREATE TABLE IF NOT EXISTS ");
        str.append(fieldsData.getClassName());
        str.append(" (_id INTEGER PRIMARY KEY, ");

        //  throw new RuntimeException("composeCreateClassStatement");

        int index = 0;
        for (FieldData f : fieldsData.getFields()) {

            str.append(f.getColumnName());
            str.append(" ");
            str.append(f.getSQLType());
            if (index++ < fieldsData.getFields().size() - 1) {
                str.append(", ");
            } else {
                str.append(")");
            }
        }

        System.out.println("SQL: " + str.toString());
        return str.toString();
    }

    public static String composeInsertStatement(FieldsData fieldsData) {

        StringBuilder str = new StringBuilder().
                append("INSERT INTO ").
                append(fieldsData.getClassName()).
                append(" (");

        int index = 0;
        for (var e : fieldsData.getFields()) {
            str.append(e.getColumnName());
            if (index++ < fieldsData.getFields().size() - 1) {
                str.append(", ");
            } else {
                str.append(") VALUES (");
            }
        }

        for (int n = 0; n < fieldsData.getFields().size(); n++) {
            str.append("?");
            if (n < fieldsData.getFields().size() - 1)
                str.append(",");
        }
        str.append(")");
        System.out.println("SQL : " + str.toString());
        return str.toString();
    }

    public static String composeQueryStatement(FieldsData fieldsData) {
        String str = "SELECT * FROM " +
                fieldsData.getClassName() +
                " WHERE _id = ?";
        System.out.println(str);
        return str;
    }
    //endregion
}

//    public static String composeQueryAllStatement(FieldsData fieldsData) {
//        String str = "SELECT * FROM " + fieldsData.getClassName();
//        System.out.println(str);
//        return str;
//    }
//
//    public static String composeRemoveStatement(FieldsData fieldsData) {
//        String str ="DELETE FROM " + fieldsData.getClassName() + " WHERE _id = ?";
//        System.out.println(str);
//        return str;
//    }
//
//    public static String composeUpdateStatement(FieldsData fieldsData) {
//        StringBuilder str = new StringBuilder().
//                append("UPDATE ").
//                append(fieldsData.getClassName()).
//                append(" SET ");
//        List<String> fields = fieldsData.getFieldNames(FieldCategory.primitiveType);
//        fields.addAll(fieldsData.getFieldNames(FieldCategory.hierarchyClass));
//
//        for (int n = 0; n < fields.size(); n++) {
//            str.append(fields.get(n));
//            if (n < fields.size() -1 )
//                str.append(" = ?, ");
//        }
//
//        str.append(" = ? WHERE _id = ?");
//        System.out.println("SQL: " + str.toString());
//        return str.toString();
//    }