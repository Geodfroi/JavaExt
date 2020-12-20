package ch.azure.aurore.sqlite;

public class DatabaseRef {

    private int id;
    private String className;

    public DatabaseRef(){
    }

    public DatabaseRef(int id, String className) {

        this.id = id;
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
