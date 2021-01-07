package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;

import java.util.HashMap;
import java.util.Map;

public class Weapon extends SQLiteData {

    private Map<Integer, Attack> attacks = new HashMap<>();

    public Map<Integer, Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(Map<Integer, Attack> attacks) {
        this.attacks = attacks;
    }
}
