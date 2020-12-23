package ch.azure.aurore.sqlite;

import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseClass;

@DatabaseClass
public class World {

    boolean _modified;

    private int _id;

    private GameObject protagonist;

    private String name;

    private int time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        _modified = true;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public boolean is_modified() {
        return _modified;
    }

    public void set_modified(boolean _modified) {
        this._modified = _modified;
    }

    public GameObject getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(GameObject protagonist) {
        this.protagonist = protagonist;
        _modified = true;
    }
}
