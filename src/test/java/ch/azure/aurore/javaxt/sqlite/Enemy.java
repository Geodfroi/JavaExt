package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;

public class Enemy extends SQLiteData {

    private World world;
    private String name;

    public Enemy() {
    }

    public Enemy(String name) {
        this.name = name;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setAsModified();
    }
}
