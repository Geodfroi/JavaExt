package ch.azure.aurore.sqlite;

import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseClass;

@DatabaseClass
public class Enemy {

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    private World world;
    private int _id;

    private String name;

    private boolean _modified;

    public boolean is_modified() {
        return _modified;
    }

    public void set_modified(boolean _modified) {
        this._modified = _modified;
    }

    public Enemy(){
    }

    public Enemy( String name) {
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
