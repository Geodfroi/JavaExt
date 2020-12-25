package ch.azure.aurore.javaxt.json;

import java.util.ArrayList;
import java.util.List;

public class Records {

    private int count;
    private String name;

    public Records(){}

    private List<Integer> ids = new ArrayList<>();

    public Records(int count, String name) {
        this.count = count;
        this.name = name;

        ids.add(1);

        ids.add(3);ids.add(5);
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}