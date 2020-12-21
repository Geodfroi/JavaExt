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
    private final Map<Class<?>, PreparedStatement> updateStatements = new HashMap<>();

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
        closeStatements(updateStatements.values());

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
        if (classLoadedItems.containsKey(type)) {
            Map<Integer, Object> map = classLoadedItems.get(type);
            if (map.containsKey(id)) {
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
                for (Map.Entry<String, String> entry : f.getPolyRefMap().entrySet()) {
                    int id = Integer.parseInt(entry.getKey());
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

    /**
     * Insert will also insert all [DatabaseClass] field items absent from the database.
     *
     * @param data object to be inserted; data without a [DatabaseClass] annotation will cause an IllegalStateException.
     * @return The Integer id of the insert object.
     */
    //region queries
    public int insertItem(Object data) {

        FieldsData fieldsData = loadFieldsData(data.getClass());
        if (fieldsData.getID(data) != 0)
            throw new IllegalStateException("Data with preset id cannot be inserted: " + data.toString());

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

        // check all fields
        // insert without foreign keys and get key;
        // insert inside memory
        // review foreign keys and insert new items
        // update item with foreign keys

        List<InsertData> inserts = new ArrayList<>();
        int insertCount = 1;
        for (FieldData f : fieldsData.getFields()) {
            InsertData r = f.prepareInsert(insertCount++, data, FieldData.InsertOperation.INSERT_FOR_NEW_ENTRY);
            inserts.add(r);

            //   private void insertReferences(List<InsertReference> references, Object data) {
//        for (InsertReference r:references) {
//            if (r.isUniqueRef()){
//                Object obj = r.getFieldData().getValue(data);
//                int id = insertItem(obj);
//                r.getFieldData().setValue(data, id);
//                aaa
//            }else{
        }

        try {
            for (InsertData i : inserts) {
                if (i instanceof InsertField) {
                    i.execute(statement, data);
                }
            }

            if (statement.executeUpdate() == 1) {
                result = statement.getGeneratedKeys();
                if (result.next()) {
                    id = result.getInt(1);
                    fieldsData.setID(data, id);
                }
            }
        } catch (SQLException e) {
            System.out.println("error inserting new value in [" + fieldsData.getClassName() + "] class");
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (id != -1) {
            putInMemory(data, id);
            boolean updated = false;
            for (InsertData i : inserts) {
                if (i instanceof InsertReference) {
                    updated = true;
                    InsertReference ref = (InsertReference) i;
                    boolean ins = ref.processRef(this, data);
                    if (!ins)
                        System.out.println("Failure to insert [" + ref.getFieldData().getField().getName() + "] value in [" + data.getClass().getSimpleName() + "]");
                }
            }

            if (updated) {
               if (!updateItem(data, new ArrayList<>())){
                   System.out.println("Failure to update [" + data.getClass().getSimpleName() + "] value with relations ids");
               }
            }
        }

        return id;
    }

    /**
     * Insert the data in database; if the item is not present in database, it will be inserted instead.
     * @param data the item to be updated inside the database.
     * @return return true if update is successful.
     */
    public boolean updateItem(Object data){
        return updateItem(data, new ArrayList<>());
    }

    /**
     * Insert the data in database; if the item is not present in database, it will be inserted instead.
     * @param data the item to be updated inside the database.
     * @param updateTrack list of previously updated items with the current public update query; used to avoid circular update calls.
     * @return return true if update is successful.
     */
    private boolean updateItem(Object data, List<Object> updateTrack) {

        updateTrack.add(data);
        FieldsData fieldsData = loadFieldsData(data.getClass());
        int id = fieldsData.getID(data);
        if (id == 0) {
            System.out.println("[" + data + "] data without preset id will be inserted instead of updated");
            return insertItem(data) != 0;
        }
        putInMemory(data, id);

        if (getMetadata().isMissing(fieldsData.getClassName())) {
            SQLiteHelper.createTable(conn, fieldsData);
            metadata = null;
        } else if (SQLiteHelper.checkMissingFields(conn, fieldsData, getMetadata())) {
            metadata = null;
        }

        fieldsData.pack(data);

        if (!updateStatements.containsKey(data.getClass())) {
            String str = SQLiteHelper.composeUpdateStatement(fieldsData);
            try {
                updateStatements.put(data.getClass(), conn.prepareStatement(str));
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to create update statement for class [" + data.getClass() + "]");
            }
        }

        List<InsertData> inserts = new ArrayList<>();
        int insertCount = 1;
        for (FieldData f : fieldsData.getFields()) {
            InsertData i = f.prepareInsert(insertCount++, data, FieldData.InsertOperation.INSERT_FOR_UPDATE);
            inserts.add(i);

            if (i instanceof InsertReference) {
                boolean ins = ((InsertReference) i).processRef(this, data);
                if (!ins)
                    System.out.println("Failure to insert [" + i.getFieldData().getField().getName() + "] value in [" + data.getClass().getSimpleName() + "]");
            } else if (i instanceof UpdateReference) {
                UpdateReference uRef = (UpdateReference) i;
                if (uRef.getFieldData().getRelationship() == Relationship.ONE_TO_ONE) {
                    Object updateObj = uRef.getUpdateRef(data, updateTrack);
                    if (updateObj != null && !updateItem(updateObj, updateTrack))
                            System.out.println("Failure to update [" + i.getFieldData().getField().getName() + "] value in [" + data.getClass().getSimpleName() + "]");
                } else {
                    for (Object updateObj : uRef.getUpdateRefs(data, updateTrack)) {
                        if (!updateItem(updateObj, updateTrack))
                            System.out.println("Failure to update [" + i.getFieldData().getField().getName() + "] value in [" + data.getClass().getSimpleName() + "]");
                    }
                }
            }
        }

        PreparedStatement statement = updateStatements.get(data.getClass());

        try {
            for (var i : inserts) {
                i.execute(statement, data);
            }
            statement.setInt(insertCount, id);

            if (statement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e0) {
            System.out.println("Failure to execute update statement for [" + data + "]");
            e0.printStackTrace();
        }
        return false;

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
                            fieldData.setValueToData(resultSet, n, data);
                            break;
                        case ONE_TO_ONE:
                            String txt = resultSet.getString(n);
                            if (!Strings.isNullOrEmpty(txt)) {
                                DatabaseRef rf = (DatabaseRef) JSON.fromJSON(DatabaseRef.class, txt);
                                references.add(new LoadReference(fieldData, rf));
                            }
                            break;
                        case ONE_TO_MANY:
                            String arrayTxt = resultSet.getString(n);
                            if (!Strings.isNullOrEmpty(arrayTxt)) {
                                @SuppressWarnings("unchecked")
                                Map<String, String> map = ((Map<String, String>) JSON.fromJSON(Map.class, arrayTxt));
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
