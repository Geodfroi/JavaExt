package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;
import ch.azure.aurore.javaxt.sqlite.wrapper.annotations.DatabaseClass;

import java.util.ArrayList;
import java.util.List;

@DatabaseClass
public class IDCollection extends SQLiteData {

    private List<Integer> idList = new ArrayList<>();

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
        setAsModified();
    }
}
