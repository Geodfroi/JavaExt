package ch.azure.aurore.sqlite.wrapper;

import ch.azure.aurore.json.JSON;
import ch.azure.aurore.reflection.Reflection;
import ch.azure.aurore.sqlite.*;
import ch.azure.aurore.strings.Strings;

import java.sql.*;
import java.util.*;

/**
 * https://www.baeldung.com/jdbc-database-metadata
 */
public class SQLite {
    private static final String JDBC_SQLITE = "jdbc:sqlite:";

    private final Map<Class<?>, FieldsData> classInfo = new HashMap<>();
    private final Map<Class<?>, PreparedStatement> insertStatements = new HashMap<>();
    private final Map<Class<?>, PreparedStatement> idQueryStatements = new HashMap<>();

    private final Map<Class<?>, Map<Integer, Object>> classLoadedItems = new HashMap<>();

//    private final Map<Class<?>, PreparedStatement> queryAllStatements = new HashMap<>();
//    private final Map<Class<?>, PreparedStatement> removeStatements = new HashMap<>();
//    private final Map<Class<?>, PreparedStatement> updateStatements = new HashMap<>();

    private final Connection conn;
    private DatabaseMetaDataWrapper metadata;

    private SQLite(Connection conn) {
        this.conn = conn;
    }

    public static SQLite connect(String databasePath) {
        String connectStr = JDBC_SQLITE + databasePath;
        try {
            Connection conn = DriverManager.getConnection(connectStr);
            return new SQLite(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to open database connection @" + connectStr);
        }
        return null;
    }

    public void clearMemory() {
        classLoadedItems.clear();
    }

    public void close() {
        closeStatements(insertStatements.values());
        closeStatements(idQueryStatements.values());

//        closeStatements(queryAllStatements.values());
//        closeStatements(removeStatements.values());
//        closeStatements(updateStatements.values());

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeStatements(Collection<PreparedStatement> statements) {
        try {
            for (PreparedStatement s : statements) {
                if (s != null)
                    s.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean failChecks(FieldsData data) {
        if (getMetadata().isMissing(data.getClassName())) {
            System.out.println("no class of name [" + data.getClassName() + "] was found in database");
            return true;
        }
        return false;
    }

    private Object fetchReferencedObject(Class<?> type, int id) {
        if (classLoadedItems.containsKey(type)){
            Map<Integer, Object> map = classLoadedItems.get(type);
            if (map.containsKey(id)){
                return map.get(id);
            }
        }
        return this.queryItem(type, id);
    }

    private DatabaseMetaDataWrapper getMetadata() {
        if (metadata == null) {
            try {
                metadata = DatabaseMetaDataWrapper.create(conn);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failure to refresh database metadata");
            }
        }
        return metadata;
    }

    private FieldsData loadFieldsData(Class<?> aClass) {
        if (!classInfo.containsKey(aClass)) {
            FieldsData f = new FieldsData(aClass);
            //failChecks(f);
            classInfo.put(aClass, f);
        }
        return classInfo.get(aClass);
    }

    private void loadReferences(List<LoadReference> references, Object data) {
        for (LoadReference f : references) {
            if (f.isUniqueRef()) {
                Class<?> fieldType = f.getFieldData().getField().getType();
                int id = f.getUniqueRef().getId();
                Object obj = fetchReferencedObject(fieldType, id);
                putInMemory(obj, id);
                f.getFieldData().setValue(data, obj);
            } else {
                List<Object> collection = new ArrayList<>();
                for (Map.Entry<String, String> entry:f.getPolyRefMap().entrySet()) {
                    int id = Integer.parseInt( entry.getKey());
                    Class<?> clazz = Reflection.getClass(entry.getValue());
                    Object obj = fetchReferencedObject(clazz, id);
                    putInMemory(obj, id);
                    collection.add(obj);
                }
                f.getFieldData().setCollectionValue(collection, data);
            }
        }
    }

    private void putInMemory(Object data, int id) {
        if (!classLoadedItems.containsKey(data.getClass())) {
            classLoadedItems.put(data.getClass(), new HashMap<>());
        }

        Map<Integer, Object> map = classLoadedItems.get(data.getClass());
        map.put(id, data);
    }

    //region queries
    public int insertItem(Object data) {

        FieldsData fieldsData = loadFieldsData(data.getClass());
        if (fieldsData.getID(data) != 0)
            throw new RuntimeException("Data with preset id cannot be inserted: " + data.toString());

        if (getMetadata().isMissing(fieldsData.getClassName())) {
            SQLiteHelper.createTable(conn, fieldsData);
            metadata = null;
        } else {
            if (SQLiteHelper.checkMissingFields(conn, fieldsData, getMetadata())) {
                metadata = null;
            }
        }

        fieldsData.pack(data);
        int id = -1;

        if (!insertStatements.containsKey(data.getClass())) {
            try {
                String str = SQLiteHelper.composeInsertStatement(fieldsData);
                insertStatements.put(data.getClass(), conn.prepareStatement(str));
            } catch (SQLException e) {
                System.out.println("failed to create insert statement for class [" + data.getClass() + "]");
                e.printStackTrace();
                return -1;
            }
        }

        ResultSet result = null;
        PreparedStatement statement = insertStatements.get(data.getClass());

        int insertCount = 1;
        try {

            for (FieldData f : fieldsData.getFields()) {
                f.setContentInDatabase(statement, insertCount, data);
                insertCount++;
            }

            if (statement.executeUpdate() == 1) {
                result = statement.getGeneratedKeys();
                if (result.next()) {
                    id = result.getInt(1);
                    fieldsData.setID(data, id);
                }
            }
        } catch (SQLException e0) {
            System.out.println("error inserting new value in [" + fieldsData.getClassName() + "] class");
            e0.printStackTrace();
        }
        finally {
            try {
                if (result != null)
                    result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (id != -1)
            putInMemory(data, id);
        return id;
    }

    public <T> T queryItem(Class<T> aClass, int id) {

        FieldsData fieldsData = loadFieldsData(aClass);
        if (failChecks(fieldsData))
            return null;

        if (!idQueryStatements.containsKey(aClass)) {
            String str = SQLiteHelper.composeQueryStatement(fieldsData);

            try {
                idQueryStatements.put(aClass, conn.prepareStatement(str));
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to create query statement");
                return null;
            }
        }
        ResultSet resultSet = null;
        PreparedStatement statement = idQueryStatements.get(aClass);

        T data;
        try {
            data = aClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            System.out.println("Failed to instantiate [" + aClass.getSimpleName() + "] object");
            e.printStackTrace();
            return null;
        }

        List<LoadReference> references = new ArrayList<>();

        try {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            ResultSetMetaData md = resultSet.getMetaData();

            if (resultSet.next()) {
                for (int n = 2; n < md.getColumnCount() + 1; n++) {

                    FieldData fieldData = fieldsData.getField(md.getColumnName(n));
                    if (fieldData == null)
                        continue;

                    switch (fieldData.getRelationship()) {

                        case NONE:
                            fieldData.NEWSetValueToData(resultSet, n, data);
                            break;
                        case ONE_TO_ONE:
                            String txt = resultSet.getString(n);
                            if (!Strings.isNullOrEmpty(txt)){
                                DatabaseRef rf = (DatabaseRef) JSON.fromJSON(DatabaseRef.class, txt);
                                references.add(new LoadReference(fieldData, rf));
                            }
                            break;
                        case ONE_TO_MANY:
                            String arrayTxt = resultSet.getString(n);
                            if (!Strings.isNullOrEmpty(arrayTxt)){
                                @SuppressWarnings("unchecked")
                                Map<String, String> map =  ((Map<String, String>)JSON.fromJSON(Map.class, arrayTxt));
                               // List<DatabaseRef> array = (List<DatabaseRef>) JSON.fromJSON(List.class, arrayTxt);
                                references.add(new LoadReference(fieldData, map));
                            }
                            break;
                    }
                }
                fieldsData.setID(data, id);
                fieldsData.unpack(data);
            } else {
                return null; // <- no result
            }

            putInMemory(data, id);
            loadReferences(references, data);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }


    //endregion
}

//    public <T> List<T> queryItems(Class<T> aClass) {
//        FieldsData fieldsData = loadFieldsData(aClass);
//        if (failChecks(fieldsData))
//            return null;
//
//        if (!queryAllStatements.containsKey(aClass)){
//            String str = SQLiteHelper.composeQueryAllStatement(fieldsData);
//            System.out.println(str);
//            try {
//                queryAllStatements.put(aClass, conn.prepareStatement(str));
//            } catch (SQLException e) {
//                e.printStackTrace();
//                System.out.println("Failure to create query statement");
//            }
//        }
//
//        List<T> items = new ArrayList<>();
//        ResultSet resultSet = null;
//        try{
//            resultSet = queryAllStatements.get(aClass).executeQuery();
//            ResultSetMetaData md = resultSet.getMetaData();
//            while (resultSet.next()){
//                T data;
//                HashMap<String, Integer> hierarchyClassFieldIds = new HashMap<>();
//
//                try {
//                    data = aClass.getDeclaredConstructor().newInstance();
//                    items.add(data);
//                    var count = md.getColumnCount();
//                    System.out.println(count);
//                    int n = 1;
//                    while (n <= md.getColumnCount()){
//                        Object value;
//                        String d = md.getColumnName(n);
//                        System.out.println(d);
//                        switch (fieldsData.getFieldCategory(md.getColumnName(n))) {
//                            case hierarchyClass:
//                                int classID = (int)getContent(resultSet, int.class,n);
//                                hierarchyClassFieldIds.put(md.getColumnName(n), classID);
//                                break;
//                            case primitiveType:
//                                Class<?> type = fieldsData.getPrimitiveFieldType(md.getColumnName(n));
//                                value = getContent(resultSet, type, n);
//                                fieldsData.setValueToObj(FieldCategory.primitiveType, data, value, md.getColumnName(n));
//                                break;
//                            case idField:
//                                value = getContent(resultSet, int.class, n);
//                                fieldsData.setID(data, (int)value);
//                                break;
//                        }
//                        n++;
//                    }
//
//                    for (Map.Entry<String, Integer> i: hierarchyClassFieldIds.entrySet()) {
//                        loadIntoHierarchyField(fieldsData,data, i.getKey(), i.getValue());
//                    }
//
//                } catch (ReflectiveOperationException e ) {
//                    System.out.println("Failure to instantiate [" + aClass.getSimpleName() + "] object");
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            if (resultSet!= null) {
//                try {
//                    resultSet.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        items.forEach(fieldsData::unpack);
//        return items;
//    }
//
//    private <T> void loadIntoHierarchyField(FieldsData fieldsData, T data, String fieldName, int fieldID) {
//        Field field = fieldsData.getField(FieldCategory.hierarchyClass, fieldName);
//        Object obj = queryItem(field.getType(), fieldID);
//        fieldsData.setValueToObj(FieldCategory.hierarchyClass, data, obj, fieldName);
//    }
//
//    public <T> boolean removeItem(T data) {
//        Class<?> aClass = data.getClass();
//        FieldsData fieldsData = loadFieldsData(aClass);
//        if (failChecks(fieldsData))
//            return false;
//
//        if (!removeStatements.containsKey(aClass)){
//            String str = SQLiteHelper.composeRemoveStatement(fieldsData);
//            try {
//                removeStatements.put(aClass, conn.prepareStatement(str));
//            } catch (SQLException e) {
//                e.printStackTrace();
//                System.out.println("failure to create remove statement");
//            }
//        }
//
//        PreparedStatement removeStatement = removeStatements.get(aClass);
//
//        int id = fieldsData.getID(data);
//        try {
//            removeStatement.setInt(1, id);
//            if (removeStatement.executeUpdate() == 1){
//
//                for (var item: fieldsData.getFields(FieldCategory.hierarchyClass)) {
//                    try {
//                        Field field = item.getValue();
//                        Object childData = field.get(data);
//                        removeItem(childData);
//
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                return true;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("failed to execute remove statement");
//        }
//        System.out.println("no item with [" + id + "] id found");
//        return false;
//    }

//    public boolean aaaupdateItem(Object data) {
//
//        FieldsData fieldsData = loadFieldsData(data.getClass());
//        if (failChecks(fieldsData))
//            return false;
//
//        if (fieldsData.getID(data) == 0){
//            System.out.println("Data without preset id cannot be updated: " + data.toString());
//            return false;
//        }
//
//        fieldsData.pack(data);
//        checkMissingFields(fieldsData);
//
//        if (!updateStatements.containsKey(data.getClass())){
//
//            String str = SQLiteHelper.composeUpdateStatement(fieldsData);
//
//            try {
//                updateStatements.put(data.getClass(), conn.prepareStatement(str.toString()));
//            } catch (SQLException e) {
//                e.printStackTrace();
//                System.out.println("Failed to create update statement for class [" + data.getClass() + "]");
//            }
//        }
//
//        PreparedStatement statement = updateStatements.get(data.getClass());
//        List<String> fields = fieldsData.getFieldNames(FieldCategory.primitiveType);
//        try {
//            int index = 1;
//            for (var item: fieldsData.getFields(FieldCategory.primitiveType)) {
//                Object value = fieldsData.getFieldValue(FieldCategory.primitiveType,data, item.getKey());
//                setContent(statement, value, item.getValue().getType(), index++);
//            }
//            for (var item:fieldsData.getFields(FieldCategory.hierarchyClass)){
//                Object value = fieldsData.getFieldValue(FieldCategory.hierarchyClass, data, item.getKey());
//                if (value == null)
//                    setContent(statement,0 , int.class, index++);
//                else {
//
//                }
//            }
//
//            int id = fieldsData.getID(data);
//            int count = fieldsData.getFieldsCount(FieldCategory.all);
//            statement.setInt(count + 1, id);
//
//            if (statement.executeUpdate() == 1){
//                return true;
//            }
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//        System.out.println("Failure to execute update statement for [" + data + "]");
//        return false;
//    }
