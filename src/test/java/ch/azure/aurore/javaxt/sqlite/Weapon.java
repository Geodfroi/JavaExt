package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;

import java.util.HashMap;
import java.util.Map;

public class Weapon extends SQLiteData {

    private Map<String, Attack> attacks = new HashMap<>();

    public Map<String, Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(Map<String, Attack> attacks) {
        this.attacks = attacks;
    }
}
