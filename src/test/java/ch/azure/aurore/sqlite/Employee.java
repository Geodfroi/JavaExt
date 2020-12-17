package ch.azure.aurore.sqlite;

@SuppressWarnings("unused")
public class Employee implements SQLiteData {
    private String name;
    private int id;
    boolean fired = true;
    boolean burned = false;

    boolean arrow;

    public boolean isArrow() {
        return arrow;
    }

    public void setArrow(boolean arrow) {
        this.arrow = arrow;
    }

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

    int newField;

    public int getNewField() {
        return newField;
    }

    public void setNewField(int newField) {
        this.newField = newField;
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }

    public boolean isBurned() {
        return burned;
    }

    public void setBurned(boolean burned) {
        this.burned = burned;
    }
}
