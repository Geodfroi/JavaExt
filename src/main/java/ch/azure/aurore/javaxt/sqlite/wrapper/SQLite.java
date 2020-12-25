package ch.azure.aurore.javaxt.sqlite.wrapper;

import ch.azure.aurore.javaxt.sqlite.SQLiteImplementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * https://www.baeldung.com/jdbc-database-metadata
 */
public class SQLite {
    private static final String JDBC_SQLITE = "jdbc:sqlite:";
    private final SQLiteImplementation implementation;

    private SQLite(Connection conn) {
         implementation = new SQLiteImplementation(conn);
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

    void clearMemory() {
        implementation.clearMemory();
    }

    public void close() {
        implementation.close();
    }

    public <T> T queryItem(Class<T> clazz, int id) {
        return implementation.queryItem(clazz, id);
    }

    public <T> List<T> queryItems(Class<T > clazz) {
        return implementation.queryItems(clazz);
    }

    /**
     * @param data The data to be removed;  [DatabaseClass] field items will not be removed.
     * @return Returns true if the item was successfully removed; false if the removal fails or the data couldn't be found to be removed.
     */
    public boolean removeItem(Object data) {
        return implementation.removeItem(data);
    }

    /**
     * Update the data in database; if the item is not present in database, it will be inserted instead; the update query will also insert or update all [DatabaseClass] field items in the database
     * @param args the items to be inserted or updated inside the database; data without a [DatabaseClass] annotation will cause an IllegalStateException.
     * @return return true if update is successful.
     */
    public boolean updateItems(Object... args) {
        boolean allUpdated = true;
        for (Object data: args) {
            if (!implementation.updateItem(data, new ArrayList<>()))
                allUpdated = false;
        }
        return allUpdated;
    }

    /**
     * Update the data in database; if the item is not present in database, it will be inserted instead; the update query will also insert or update all [DatabaseClass] field items in the database
     * @param data the item to be inserted or updated inside the database; data without a [DatabaseClass] annotation will cause an IllegalStateException.
     * @return return true if update is successful.
     */
    public boolean updateItem(Object data) {
        return implementation.updateItem(data, new ArrayList<>());
    }
}

