package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseClass;

import java.util.ArrayList;
import java.util.List;

@DatabaseClass
public class Party {

    int _id;
    boolean _modified;
    List<GameObject> members = new ArrayList<>();

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

    public List<GameObject> getMembers() {
        return members;
    }

    public void setMembers(List<GameObject> members) {
        this.members = members;
    }
}
