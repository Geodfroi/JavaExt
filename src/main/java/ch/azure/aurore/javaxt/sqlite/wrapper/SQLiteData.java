package ch.azure.aurore.javaxt.sqlite.wrapper;

import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseClass;

@DatabaseClass
public abstract class SQLiteData {

    private int _id;
    private boolean _modified;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @SuppressWarnings("unused")
    public boolean is_modified() {
        return _modified;
    }

    @SuppressWarnings("unused")
    public void set_modified(boolean _modified) {
        this._modified = _modified;
    }

    public void setAsModified(){
        this._modified = true;
    }

}
