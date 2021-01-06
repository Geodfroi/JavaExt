package ch.azure.aurore.javaxt.sqlite;

public class DatabaseRef {

    private int id;
    private String type;

    public DatabaseRef(){
    }

    public DatabaseRef(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @SuppressWarnings("unused")
    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }
}
