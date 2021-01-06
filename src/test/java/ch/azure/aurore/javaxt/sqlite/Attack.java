package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLite;
import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;
import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseClass;

@DatabaseClass
public class Attack extends SQLiteData {

    private String name;
    private int _APCost;

    public Attack(){
    }

    public Attack(String name, int _APCost) {

        this.name = name;
        this._APCost = _APCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setAsModified();
    }

    public int get_APCost() {
        return _APCost;
    }

    public void set_APCost(int _APCost) {
        this._APCost = _APCost;
        setAsModified();
    }
}
