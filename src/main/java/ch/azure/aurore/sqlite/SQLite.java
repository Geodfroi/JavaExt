package ch.azure.aurore.sqlite;


import ch.azure.aurore.reflection.ClassProperties;
import ch.azure.aurore.reflection.ClassProperty;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * https://www.baeldung.com/jdbc-database-metadata
 */
public class SQLite {
    private static final String JDBC_SQLITE = "jdbc:sqlite:";

    private final String connectStr;
    private final Map<Class<?>, ClassProperties> classPropertiesMap = new HashMap<>();
    private final Map<Class<?>, PreparedStatement> insertStatements = new HashMap<>();
    private final Map<Class<?>, PreparedStatement> queryStatements = new HashMap<>();

    private Connection conn;
    private boolean opened;

    SQLiteMetadata metadata = new SQLiteMetadata();

    public SQLite(String databasePath) {
        connectStr = JDBC_SQLITE + databasePath;
    }

    public void close() {

        closeStatements(insertStatements.values());
        closeStatements(queryStatements.values());

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeStatements(Collection<PreparedStatement> statements) {
        try{
            for (PreparedStatement s: statements) {
                if (s != null)
                    s.close();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(String tableName, ClassProperties properties) {
        StringBuilder str = new StringBuilder();
        str.append("CREATE TABLE IF NOT EXISTS ");
        str.append(tableName);
        str.append(" (_id INTEGER PRIMARY KEY, ");
        for (int n = 0; n < properties.size(); n++) {
            if (properties.get(n).getName().matches("id"))
                continue;
            str.append(properties.get(n).getName());
            str.append(" ");
            str.append(getSQLType(properties.get(n).getType()));
            if (n < properties.size()-1){
                str.append(", ");
            }else {
                str.append(")");
            }
        }

        System.out.println("SQL: " + str.toString());
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.execute(str.toString());
        } catch (SQLException e) {
            try {
                System.out.println("failed to create [" + tableName + "] table");
                if (statement!= null)
                    statement.close();
                e.printStackTrace();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    private String getSQLType(Class<?> type) {
        if (type.equals(String.class))
            return "TEXT";
        if (type.equals(int.class))
            return "INTEGER";
        if (type.equals(boolean.class))
            return "NUMERIC";
        if (type.equals(float.class) || type.equals(double.class))
            return "REAL";

        throw new RuntimeException("invalid ["+type.getSimpleName()+"] type");
    }

    private void insertColumn(String className, ClassProperty p)  {
        StringBuilder str = new StringBuilder();
        str.append("ALTER TABLE ");
        str.append(className);
        str.append(" ADD ");
        str.append(p.getName());
        str.append(" ");
        str.append(getSQLType(p.getType()));
        System.out.println(str.toString());

        Statement s= null;
        try {
            s = conn.createStatement();
            s.execute(str.toString());
        } catch (SQLException e) {
            System.out.println("Failed to insert [" + p.getName() + "] column in [" + className + "]");
            e.printStackTrace();
        }finally {
            try {
                if (s!= null)
                   s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertItem(SQLiteData data) {
        if (data.getId() != 0)
            throw new RuntimeException("Data with preset id cannot be inserted: " + data.toString());

        open();
        if (!classPropertiesMap.containsKey(data.getClass())){
            classPropertiesMap.put(data.getClass(), new ClassProperties(data.getClass()));
        }

        ClassProperties classProperties =  classPropertiesMap.get(data.getClass());
        if (metadata.isMissing(classProperties.getClassName())) {
            createTable(classProperties.getClassName(), classProperties);
        }else{
            for (ClassProperty p:classProperties) {
                if (p.getName().matches("id"))
                    continue;
                if (!metadata.checkColumn(classProperties.getClassName(), p)){
                    insertColumn(classProperties.getClassName(), p);
                }
            }
        }

        List<String> fields = classProperties.stream().
                map(ClassProperty::getName).
                filter(s -> !s.matches("id")).
                collect(Collectors.toList());

        if (!insertStatements.containsKey(data.getClass())){

            StringBuilder str = new StringBuilder();
            str.append("INSERT INTO ").append(classProperties.getClassName()).append(" (");

            for (int n = 0; n < fields.size(); n++) {
                str.append(fields.get(n));
                if (n < fields.size() - 1){
                    str.append(", ");
                }else{
                    str.append(") VALUES (");
                }
            }
            for (int n = 0; n < fields.size(); n++) {
                str.append("?");
                if (n < fields.size() - 1)
                    str.append(",");
                else
                    str.append(")");
            }
            System.out.println("SQL : " + str.toString());
            try {
                insertStatements.put(data.getClass(), conn.prepareStatement(str.toString()));
            } catch (SQLException e) {
                System.out.println("failed to create insert statement");
                e.printStackTrace();
                return;
            }
        }

        ResultSet result = null;
        PreparedStatement statement = insertStatements.get(data.getClass());

        try{
            for (int n = 0; n < fields.size(); n++) {
                ClassProperty property = classProperties.getProperty(fields.get(n));
                if (property != null)
                    setStatement(statement, data, property, n + 1);
            }
            if (statement.executeUpdate() != 1)
                throw new SQLException();

            result = statement.getGeneratedKeys();
            if (result.next()) {
                int id = result.getInt(1);
                data.setId(id);
            }

        }catch (SQLException e){
            System.out.println("error inserting new value in [" + classProperties.getClassName() + "] class");
            e.printStackTrace();
        }
        finally {
            try {
                if (result != null)
                    result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public <T extends SQLiteData> T queryItem(Class<T> aClass, int id) {

        if (!SQLiteData.class.isAssignableFrom(aClass))
            throw new RuntimeException("Failure to query: [" + aClass.getSimpleName()+ "] doesn't implement [SQLiteData] interface");

        open();

        if (metadata.isMissing(aClass.getSimpleName())){
            System.out.println("no class of name [" + aClass.getSimpleName() + "] was found in database");
            return null;
        }
        if (!classPropertiesMap.containsKey(aClass)){
            classPropertiesMap.put(aClass, new ClassProperties(aClass));
        }
        ClassProperties classProperties =  classPropertiesMap.get(aClass);

        if (!queryStatements.containsKey(aClass)){
            StringBuilder str = new StringBuilder();
            str.append("SELECT * FROM ");
            str.append(classProperties.getClassName());
            str.append(" WHERE _id = ?");
            System.out.println(str.toString());

            try {
                queryStatements.put(aClass, conn.prepareStatement(str.toString()));
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to create query statement");
                return null;
            }
        }
        ResultSet resultSet = null;
        PreparedStatement statement = queryStatements.get(aClass);
        T data;
        try {
            data = aClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e ) {
            System.out.println("Failed to instantiate [" + aClass.getSimpleName() + "] object");
            e.printStackTrace();
            return null;
        }

        try {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            ResultSetMetaData md = resultSet.getMetaData();
            if (resultSet.next()){
                for (int n = 2; n < md.getColumnCount() + 1; n++) {
                    ClassProperty property = classProperties.getProperty(md.getColumnName(n));
                    if (property != null)
                        getStatement(resultSet, data, property, n);
                }
            }
            data.setId(id);
            return data;
        } catch (SQLException e) {
            e.printStackTrace();

        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void getStatement(ResultSet resultSet, Object obj, ClassProperty property, int index) throws SQLException {

        Object content;
        if (property.getType().equals(String.class)){
            content = resultSet.getString(index);
        }else if(property.getType().equals(boolean.class)){
            content = resultSet.getBoolean(index);
        }else if(property.getType().equals(int.class)){
            content = resultSet.getInt(index);
        } else{
            throw new RuntimeException("Can't fetch value from database for [" + property.getType() + "] type in [getStatement] method");
        }
        property.setPropertyValue(obj, content);

////                byte[] array = result.getBytes(4);
    }

    private void setStatement(PreparedStatement statement, Object obj, ClassProperty property, int statementIndex) throws SQLException {

        Class<?> type = property.getType();
        Object value = property.getPropertyValue(obj);

        if (type.equals(String.class)){
            statement.setString(statementIndex, (String)value);
        }else if (type.equals(boolean.class)){
            statement.setBoolean(statementIndex,(boolean)value);
        }else if(type.equals(int.class)){
            statement.setInt(statementIndex, (int)value);
        }
        else
            throw new RuntimeException("Can't insert value in database for [" + type.getSimpleName() + "] type in [setStatement] method");
//            insertContentStatement.setBytes(3, null);
    }

    public void open() {
        if (!opened){
            try {
                conn = DriverManager.getConnection(connectStr);
                metadata.load(conn);
                opened = true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to open database connection @" + connectStr);
            }
        }
    }
}