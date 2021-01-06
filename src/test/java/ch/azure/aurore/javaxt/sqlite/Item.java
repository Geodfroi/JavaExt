package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;
import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseClass;

import java.util.ArrayList;
import java.util.List;

@DatabaseClass
public class Item extends SQLiteData {
    private String name;
    private List<Link> links = new ArrayList<>();

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setAsModified();
    }

    public List<Link> getLinks() {
        return links;
    }

    @SuppressWarnings("unused")
    public void setLinks(List<Link> links) {
        this.links = links;
        setAsModified();
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                '}';
    }
}
