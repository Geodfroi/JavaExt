package ch.azure.aurore.sqlite;

import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseClass;
import ch.azure.aurore.sqlite.wrapper.annotations.PrimaryKey;

@DatabaseClass
public class World {

    @PrimaryKey
    private int _id;

    private String name;

    private int time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
