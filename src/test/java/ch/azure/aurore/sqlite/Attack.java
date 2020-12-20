package ch.azure.aurore.sqlite;

import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseClass;
import ch.azure.aurore.sqlite.wrapper.annotations.PrimaryKey;

@DatabaseClass
public class Attack {

    @PrimaryKey
    private int _id;
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
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_APCost() {
        return _APCost;
    }

    public void set_APCost(int _APCost) {
        this._APCost = _APCost;
    }
}
