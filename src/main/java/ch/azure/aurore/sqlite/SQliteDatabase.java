package ch.azure.aurore.sqlite;

import ch.azure.aurore.IO.API.Disk;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public abstract class SQliteDatabase {

    public static final String JDBC_SQLITE = "jdbc:sqlite:";
    private static SQliteDatabase instance;
    private Collection<PreparedStatement> statements = new ArrayList<>();

    public static <T extends SQliteDatabase> T getInstance(Class<T> a) throws RuntimeException {
        if (instance == null){
            try {
                instance = a.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to get instance of database");
            }
        }
        //noinspection unchecked
        return (T) instance;
    }

    private Connection conn;

    protected abstract String[] tableDeclarations();

    public boolean open(String databasePath){
        try {
            if (conn != null)
                close();

            Disk.backupFile(databasePath);

            String connectStr = JDBC_SQLITE + databasePath;
            conn = DriverManager.getConnection(connectStr);

            Statement statement = conn.createStatement();
            for (String str:this.tableDeclarations()) {
                statement.execute(str);
            }
            statement.close();
            statements = prepareStatements(conn);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Can't open database");
            return false;
        }
    }

    public void close() {
        try{
            for (PreparedStatement s:statements) {
                if (s != null)
                    s.close();
            }
            if (conn != null)
                conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    protected abstract Collection<PreparedStatement> prepareStatements(Connection conn) throws SQLException;
}