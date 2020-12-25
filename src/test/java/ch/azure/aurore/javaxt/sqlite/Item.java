package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseClass;

import java.util.ArrayList;
import java.util.List;

@DatabaseClass
public class Item {
    private int _id;
    private boolean _modified;
    private String name;

    public Item(){
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<Link> links = new ArrayList<>();

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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                '}';
    }
}
