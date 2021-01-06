package ch.azure.aurore.javaxt.sqlite;

import java.util.ArrayList;
import java.util.List;

public class QueryTrace {
    private List<Object> handled = new ArrayList<>();

    public boolean contains(Object data) {
        return handled.contains(data);
    }

    public void add(Object data) {
        handled.add(data);
    }
}
