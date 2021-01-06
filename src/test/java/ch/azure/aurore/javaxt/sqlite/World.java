package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;
import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseClass;
import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseIgnore;
import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseName;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
@DatabaseClass(dbName = "Setting")
public class World extends SQLiteData {

    private Set<String> tags = new HashSet<>();
    @DatabaseName(value = "hero")
    private GameObject protagonist;
    private String name;
    @DatabaseIgnore
    private int time;

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int i) {
        time = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        set_modified(true);
    }

    public GameObject getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(GameObject protagonist) {
        this.protagonist = protagonist;
    }
}
