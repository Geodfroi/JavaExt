package ch.azure.aurore.sqlite;

import ch.azure.aurore.IO.API.Disk;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SQLiteTest {

    private static final String DATABASE_PATH = "roster.sqlite";
    private static SQLite sqlite;

    @BeforeAll
    static void beforeAll() {
        Disk.removeFile(DATABASE_PATH);
        sqlite = new SQLite(DATABASE_PATH);

        Employee employee = new Employee();
        employee.setName("Jean");
        employee.setFired(true);
        sqlite.insertItem(employee);

        Employee employee2 = new Employee();
        employee2.setName("Webster");
        employee2.setFired(false);
        employee2.setBurned(true);
        sqlite.insertItem(employee2);
    }

    @AfterAll
    static void afterAll() {
        sqlite.close();
    }

    @Test
    void set() {
        Car car = new Car();
        car.setMaker("Toyota");
        car.setMillage(12000);
        sqlite.insertItem(car);
    }

    @Test
    void get_entry(){
        Employee employee = sqlite.queryItem(Employee.class, 1);
        Assertions.assertEquals(employee.getId(), 1);
    }

    @Test
    void getEntries(){
        assert false;
    }
}