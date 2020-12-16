package ch.azure.aurore.sqlite;


import ch.azure.aurore.reflection.Reflection;
import ch.azure.aurore.tuples.Pair;
import ch.azure.aurore.tuples.Trio;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SQLite {

    private static final String JDBC_SQLITE = "jdbc:sqlite:";
    String dbName = "EmployeeDB.SQLite";
    private static SQLite instance = new SQLite();
    private Connection conn;

    public static SQLite getInstance() {
        return instance;
    }

    public void open(String databasePath) {
        String connectStr = JDBC_SQLITE + databasePath;
        try {
            conn = DriverManager.getConnection(connectStr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Object obj) {
        List<Trio<String, Method, Method>> classProperties = Reflection.getProperties(obj.getClass());
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

//    private void insert(Employee employee) {
//        Class c = employee.getClass();
//        String className = c.getSimpleName();
//
//        Arrays.stream(c.getMethods()).filter(new Predicate<Method>() {
//            @Override
//            public boolean test(Method method) {
//               return Reflection.isGetter(method);
//            }
//        }).forEach(new Consumer<Method>() {
//            @Override
//            public void accept(Method method) {
//                System.out.println(method.getName());
//            }
//        });
//
//
////        var p = c.getMethods();
////        Arrays.stream(p).forEach(m -> System.out.println(m));
////        System.out.println(p);
//
////        Arrays.stream(c.getFields()).forEach(f -> System.out.println(f));
////        System.out.println("-------------------");
////        Arrays.stream(c.getDeclaredFields()).forEach(f -> System.out.println(f));
//
//    }
}

//    public boolean open(String databasePath){
//        try {
//            if (conn != null)
//                close();
//
//            Disk.backupFile(databasePath);
//
//            String connectStr = JDBC_SQLITE + databasePath;
//            conn = DriverManager.getConnection(connectStr);
//
//            Statement statement = conn.createStatement();
//            for (String str:this.tableDeclarations()) {
//                statement.execute(str);
//            }
//            statement.close();
//            statements = prepareStatements(conn);
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Can't open database");
//            return false;
//        }
