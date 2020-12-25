package ch.azure.aurore.sqlite;

import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseClass;
import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseIgnore;
import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseName;

import java.util.HashSet;
import java.util.Set;

@DatabaseClass(dbName = "Setting")
public class World {

    boolean _modified;

    private int _id;
    private Set<String> tags = new HashSet<>();

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @DatabaseName(value = "hero")
    private GameObject protagonist;

    private String name;

    @DatabaseIgnore
    private int time;

    public int getTime() {
        return time;
    }

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
    }

    public void setTime(int i) {
        time = i;
    }
}
