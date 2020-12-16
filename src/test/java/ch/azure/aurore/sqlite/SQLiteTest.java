package ch.azure.aurore.sqlite;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Employee{
    private String name;
    private int id;
    boolean fired = true;
    boolean borned = false;

    public boolean isFired() {
        return fired;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }

    public boolean isBorned() {
        return borned;
    }

    public void setBorned(boolean borned) {
        this.borned = borned;
    }
}

class SQLiteTest {

    private static final String DATABASE_PATH = "roster.sqlite";

    @BeforeAll
    static void beforeAll() {
        SQLite.getInstance().open(DATABASE_PATH);
    }

    @AfterAll
    static void afterAll() {
        SQLite.getInstance().close();
    }

    @Test
    void set() {
        Employee employee = new Employee();
        employee.setName("Jean");
        employee.setFired(true);

        SQLite.getInstance().insert(employee);
    }
}