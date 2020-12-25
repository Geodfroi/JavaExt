package ch.azure.aurore.javaxt.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.function.Function;

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
        boolean modified = false;

        for (FieldData e : fieldsData.getFields()) {
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
                modified = true;
            }
        }
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

    public static String composeQueryAllStatement(FieldsData fieldsData) {
        String str = "SELECT * FROM " + fieldsData.getClassName();
        System.out.println(str);
        return str;
    }

    public static String composeRemoveStatement(FieldsData fieldsData) {
        String str = "DELETE FROM " + fieldsData.getClassName() + " WHERE _id = ?";
        System.out.println(str);
        return str;
    }

    public static String composeUpdateStatement(FieldsData fieldsData) {
        StringBuilder str = new StringBuilder().
                append("UPDATE ").
                append(fieldsData.getClassName()).
                append(" SET ");

        int count = 0;
        for (var e : fieldsData.getFields()) {
            str.append(e.getColumnName());
            if (count++ < fieldsData.getFields().size() - 1) {
                str.append(" = ?, ");
            }
        }

        str.append(" = ? WHERE _id = ?");
        System.out.println("SQL: " + str.toString());
        return str.toString();
    }

    //endregion

    public static void checkStatement(Connection conn, FieldsData fields, Map<Class<?>, PreparedStatement> statements, Class<?> aClass, Function<FieldsData, String> func) {
        if (!statements.containsKey(aClass)) {
            try {
                //  String str = SQLiteHelper.composeInsertStatement(fields);
                String str =  func.apply(fields);
                statements.put(aClass, conn.prepareStatement(str));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IllegalStateException("failed to create insert statement for class [" + aClass + "]");
            }
        }
    }
}