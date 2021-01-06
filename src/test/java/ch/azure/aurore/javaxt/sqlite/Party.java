package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;
import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseClass;

import java.util.ArrayList;
import java.util.List;

@DatabaseClass
public class Party extends SQLiteData {

    List<GameObject> members = new ArrayList<>();

    public List<GameObject> getMembers() {
        return members;
    }

    public void setMembers(List<GameObject> members) {
        this.members = members;
    }
}
