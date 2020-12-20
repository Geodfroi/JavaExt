package ch.azure.aurore.sqlite;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseMetaDataWrapper {

    private final Map<String,ClassMetadata> classes = new HashMap<>();

    public static DatabaseMetaDataWrapper create(Connection conn) throws SQLException {
        DatabaseMetaDataWrapper wrapper = new DatabaseMetaDataWrapper();
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
        while(resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            ClassMetadata classMetadata = new ClassMetadata(databaseMetaData, tableName);
            //   String remarks = resultSet.getString("REMARKS");
            wrapper.classes.put(tableName, classMetadata);
        }
        resultSet.close();
        return wrapper;
    }

    public boolean checkColumn(String className, String columnName) {
        ClassMetadata aClass = classes.get(className);
        return aClass.hasColumn(columnName);
    }

    public boolean isMissing(String className){
        return !classes.containsKey(className);
    }
}

class ClassMetadata{

    private final Map<String, ColumnData> columns = new HashMap<>();
    private final String tableName;

    public ClassMetadata(DatabaseMetaData databaseMetaData, String tableName) throws SQLException {
        this.tableName = tableName;

        ResultSet r = databaseMetaData.getColumns(null,null, tableName, null);
        while(r.next()) {
            var data = new ColumnData(r);
            columns.put(data.getColumnName(), data);
        }
    }

//    public boolean checkColumn(String fieldName, Class<?> type) {
//        if (columns.containsKey(fieldName)){
//            var column = columns.get(fieldName);
//            return column.getDatatype().equals(type.getSimpleName());
//        }
//        return false;
//    }

    @Override
    public String toString() {
        return "ClassMetadata{" +
                "tableName='" + tableName + '\'' +
                '}';
    }

    public boolean hasColumn(String columnName) {
        return columns.containsKey(columnName);
    }
}

class ColumnData{

    private final String columnName;
    private final String datatype;

    @SuppressWarnings("CommentedOutCode")
    public ColumnData(ResultSet columns) throws SQLException {
        columnName = columns.getString("COLUMN_NAME");
        //columnSize = columns.getString("COLUMN_SIZE");
        datatype = getType(columns.getString("DATA_TYPE"));
        //isNullable = columns.getString("IS_NULLABLE");
        //isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDatatype() {
        return datatype;
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
