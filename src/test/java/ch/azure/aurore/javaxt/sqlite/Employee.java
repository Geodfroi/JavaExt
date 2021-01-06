package ch.azure.aurore.javaxt.sqlite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Employee {

    String name;
    int id;

    List<String> tags = new ArrayList<>();

    public Employee() {
    }

    public Employee(int id, String name, String... tags) {
        this.id = id;
        this.name = name;
        this.tags.addAll(Arrays.asList(tags));
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
