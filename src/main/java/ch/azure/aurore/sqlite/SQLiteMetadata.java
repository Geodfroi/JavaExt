package ch.azure.aurore.sqlite;

import ch.azure.aurore.reflection.ClassProperty;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SQLiteMetadata {

    private final Map<String,ClassMetadata> classes = new HashMap<>();

    public void load(Connection conn) throws SQLException {
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
        while(resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            ClassMetadata classMetadata = new ClassMetadata(databaseMetaData, tableName);
            //   String remarks = resultSet.getString("REMARKS");
            classes.put(tableName, classMetadata);
        }
    }

    public boolean isMissing(String className){
        return !classes.containsKey(className);
    }

    public boolean checkColumn(String className, ClassProperty column) {
        ClassMetadata aClass = classes.get(className);
        return aClass.checkColumn(column);
    }

//    public List<ColumnData> getColumns(String className) {
//        ClassMetadata aClass = classes.get(className);
//        return aClass.getColumns();
//    }
}

class ClassMetadata{

    private final List<ColumnData> columns = new ArrayList<>();

    public ClassMetadata(DatabaseMetaData databaseMetaData, String tableName) throws SQLException {

        ResultSet r = databaseMetaData.getColumns(null,null, tableName, null);
        while(r.next()) {
            columns.add(new ColumnData(r));
        }
    }

    public boolean checkColumn(ClassProperty classProperty) {
        ColumnData column = getColumn(classProperty.getName());
        if (column !=null)
            return column.getDatatype().equals(classProperty.getType().getSimpleName());

        return false;
    }

    private ColumnData getColumn(String name) {
        for (var c:columns) {
            if (c.getColumnName().equals(name))
                return c;
        }
        return null;
    }

    public List<ColumnData> getColumns() {
        return columns;
    }
}

class ColumnData{

    private final String columnName;
    private final String datatype;

    public String getColumnName() {
        return columnName;
    }

    public String getDatatype() {
        return datatype;
    }

    @SuppressWarnings("CommentedOutCode")
    public ColumnData(ResultSet columns) throws SQLException {
        columnName = columns.getString("COLUMN_NAME");
        //columnSize = columns.getString("COLUMN_SIZE");
        datatype = getType(columns.getString("DATA_TYPE"));
        //isNullable = columns.getString("IS_NULLABLE");
        //isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
    }

    private String getType(String data_type) {
        switch (data_type){
            case "6":
                return "boolean";
            case "4":
                return "int";
            case "12":
                return "String";
        }
        throw new RuntimeException("Can't convert from [" + data_type + "] SQLite type to java type");
    }
}
