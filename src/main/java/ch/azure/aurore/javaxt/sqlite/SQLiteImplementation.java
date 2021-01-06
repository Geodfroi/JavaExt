package ch.azure.aurore.javaxt.sqlite;

import java.sql.*;
import java.util.*;

public class SQLiteImplementation {

    private final Map<Class<?>, FieldsData> classInfo = new HashMap<>();
    private final Map<Class<?>, Map<Integer, Object>> classLoadedItems = new HashMap<>();
    private final Map<Class<?>, PreparedStatement> insertStatements = new HashMap<>();
    private final Map<Class<?>, PreparedStatement> idQueryStatements = new HashMap<>();
    private final Map<Class<?>, PreparedStatement> queryAllStatements = new HashMap<>();
    private final Map<Class<?>, PreparedStatement> removeStatements = new HashMap<>();
    private final Map<Class<?>, PreparedStatement> updateStatements = new HashMap<>();
    private final Connection conn;
    private DatabaseMetaDataWrapper metadata;

    public SQLiteImplementation(Connection conn) {
        this.conn = conn;
    }

    public void clearMemory() {
        classLoadedItems.clear();
    }

    public void close() {
        closeStatements(insertStatements.values());
        closeStatements(idQueryStatements.values());
        closeStatements(queryAllStatements.values());
        closeStatements(removeStatements.values());
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

    private <T> T createEmpty(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to instantiate [" + clazz.getSimpleName() + "] object");
        }
    }

    private boolean failChecks(FieldsData data) {
        if (getMetadata().isMissing(data.getClassName())) {
            System.out.println("no class of name [" + data.getClassName() + "] was found in database");
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> T fetchReferencedObject(Class<T> clazz, int id) {
        if (classLoadedItems.containsKey(clazz)) {
            Map<Integer, Object> map = classLoadedItems.get(clazz);
            if (map.containsKey(id)) {
                return (T)map.get(id);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> Map<Integer, T> fetchReferencedObjects(Class<?> clazz) {
        Map<Integer, T> map = new HashMap<>();
        if (classLoadedItems.containsKey(clazz)) {
            for (Map.Entry<Integer, Object> i: classLoadedItems.get(clazz).entrySet() ) {
                map.put(i.getKey(), (T)i.getValue());
            }
        }
        return map;
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

    public void putInMemory(Object data, int id) {
        if (!classLoadedItems.containsKey(data.getClass()))
            classLoadedItems.put(data.getClass(), new HashMap<>());

        Map<Integer, Object> map = classLoadedItems.get(data.getClass());
        map.put(id, data);
    }

    private void removeReferencedObject(Class<?> clazz, int id) {
        if (!classLoadedItems.containsKey(clazz))
            return;

        Map<Integer, Object> items = classLoadedItems.get(clazz);
        items.remove(id);
    }

    //region queries
    private int insertItem(Object data) {
        FieldsData fieldsData = loadFieldsData(data.getClass());
        int id = fieldsData.getID(data);
        if (id != 0)
            return id;

        fieldsData.pack(data);
        id = -1;
        
        SQLiteHelper.checkStatement(conn, fieldsData, insertStatements, data.getClass(), SQLiteHelper::composeInsertStatement);
        PreparedStatement statement = insertStatements.get(data.getClass());

        ResultSet result = null;
        int insertCount = 1;

        try {
            for (FieldData f : fieldsData.getFields()) {
                if (insertCount == 7)
                    System.out.println(insertCount);
                InsertField operation = (InsertField) f.prepareInsert(insertCount++, data, FieldData.InsertOperation.INSERT_FOR_NEW_ENTRY);
                operation.execute(statement, data);
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
        return id;
    }

    /**
     * @param data  update data;
     * @param trace list of previously updated items with the current public update query; used to avoid circular update calls.
     * @return return true if update is successful.
     */
    public boolean updateItem(Object data, QueryTrace trace) {

        FieldsData fieldsData = loadFieldsData(data.getClass());
        if (getMetadata().isMissing(fieldsData.getClassName())) {
            SQLiteHelper.createTable(conn, fieldsData);
            metadata = null;
        } else if (SQLiteHelper.checkMissingFields(conn, fieldsData, getMetadata()))
            metadata = null;

        int id = fieldsData.getID(data);
        boolean isModified = fieldsData.isModified(data);

        if (id == 0) {
            System.out.println("[" + data + "] data without preset id will be inserted instead of updated");
            id = insertItem(data);
            isModified = true;
        }
        putInMemory(data, id);
        fieldsData.pack(data);

        List<InsertData> inserts = new ArrayList<>();
        int insertCount = 1;

        for (FieldData f : fieldsData.getFields()) {
            InsertData i = f.prepareInsert(insertCount++, data, FieldData.InsertOperation.INSERT_FOR_UPDATE);
            inserts.add(i);

            if (i instanceof UpdateReference) {
                ((UpdateReference) i).forwardReference(this, data, trace);
                isModified = true;
            }
        }

        if (isModified) {
            SQLiteHelper.checkStatement(conn, fieldsData, updateStatements, data.getClass(), SQLiteHelper::composeUpdateStatement);
            PreparedStatement statement = updateStatements.get(data.getClass());

            try {
                for (InsertData i : inserts)
                    i.execute(statement, data);
                statement.setInt(insertCount, id);
                if (statement.executeUpdate() == 1)
                    return true;
            } catch (SQLException e0) {
                System.out.println("Failure to execute update statement for [" + data + "]");
                e0.printStackTrace();
                return false;
            }
        }
        fieldsData.setModified(data, false);
        return true;
    }

    public <T> T queryItem(Class<T> clazz) {
        List<T> collection = queryItems(clazz);
        if (collection != null && collection.size() > 0)
            return collection.get(0);
        return null;
    }

    public <T> T queryItem(Class<T> clazz, int id) {

        T obj = fetchReferencedObject(clazz, id);
        if (obj != null)
            return obj;

        FieldsData fieldsData = loadFieldsData(clazz);
        if (failChecks(fieldsData))
            return null;

        SQLiteHelper.checkStatement(conn, fieldsData, idQueryStatements, clazz, SQLiteHelper::composeQueryStatement);
        PreparedStatement statement = idQueryStatements.get(clazz);

        ResultSet resultSet = null;

        T data = createEmpty(clazz);
        List<PullData> pulls = new ArrayList<>();
        try {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            ResultSetMetaData md = resultSet.getMetaData();

            if (resultSet.next()) {
                for (int n = 2; n < md.getColumnCount() + 1; n++) {
                    FieldData f = fieldsData.getField(md.getColumnName(n));
                    if (f == null)
                        continue;

                    PullData p = f.preparePull(resultSet, n);
                    if (p != null)
                        pulls.add(p);
                }
                fieldsData.setID(data, id);
            } else
                return null; // <- no result

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

        putInMemory(data, id);
        for (PullData p : pulls) {
            p.execute(this, data);
        }

        fieldsData.unpack(data);
        fieldsData.setModified(data, false);
        return data;
    }

    public <T> List<T> queryItems(Class<T> clazz) {
        FieldsData fieldsData = loadFieldsData(clazz);
        if (failChecks(fieldsData))
            return new ArrayList<>();

        SQLiteHelper.checkStatement(conn, fieldsData, queryAllStatements, clazz, SQLiteHelper::composeQueryAllStatement);
        PreparedStatement statement = queryAllStatements.get(clazz);

        ResultSet resultSet = null;
        List<List<PullData>> pulls = new ArrayList<>();
        try {
            resultSet = statement.executeQuery();
            ResultSetMetaData md = resultSet.getMetaData();

            while (resultSet.next()) {
                List<PullData> list = new ArrayList<>();
                pulls.add(list);
                for (int n = 1; n <= md.getColumnCount(); n++) {
                    FieldData f = fieldsData.getField(md.getColumnName(n));
                    PullData p = f.preparePull(resultSet, n);
                    list.add(p);
                }
            }

        } catch (SQLException e0) {
            System.out.println("Failure to retrieve [" + clazz.getSimpleName() + "] items");
            e0.printStackTrace();
            return null;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        Map<Integer, T> existing = fetchReferencedObjects(clazz);
        List<T> dataList = new ArrayList<>();
        for (List<PullData> list : pulls) {
            T data = createEmpty(clazz);
            for (PullData p : list)
                p.execute(this, data);

            int id = fieldsData.getID(data);
            if (existing.containsKey(id))
                dataList.add(existing.get(id));
            else
            {
                dataList.add(data);
                putInMemory(data, id);
            }

            fieldsData.unpack(data);
            fieldsData.setModified(data, false);
        }
        return dataList;
    }

    public boolean removeItem(Object data) {
        Class<?> clazz = data.getClass();
        FieldsData fieldsData = loadFieldsData(clazz);
        if (failChecks(fieldsData))
            return false;

        int id = fieldsData.getID(data);
        removeReferencedObject(clazz, id);

        SQLiteHelper.checkStatement(conn, fieldsData, removeStatements, clazz, SQLiteHelper::composeRemoveStatement);
        PreparedStatement statement = removeStatements.get(clazz);

        try {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 1)
                return true;

        } catch (SQLException e0) {
            System.out.println("failed to execute remove statement for [" + data + "]");
            e0.printStackTrace();
        }
        return false;
    }
    //endregion
}
