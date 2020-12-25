package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseClass;

import java.util.ArrayList;
import java.util.List;

@DatabaseClass
public class Link {

    private List<Item> itemList = new ArrayList<>();
    private boolean _modified;
    private int _id;

    public Link() {
    }

    public Link(Item a, Item b) {
        itemList.add(a);
        itemList.add(b);
        a.getLinks().add(this);
        b.getLinks().add(this);
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public boolean is_modified() {
        return _modified;
    }

    public void set_modified(boolean _modified) {
        this._modified = _modified;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Link{" +
                "itemList=" + itemList +
                '}';
    }
}
