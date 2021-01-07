package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;

import java.util.HashMap;
import java.util.Map;

public class AccountTable extends SQLiteData {

    private Map<Integer, Boolean> account = new HashMap<>();

    public Map<Integer, Boolean> getAccount() {
        return account;
    }

    public void setAccount(Map<Integer, Boolean> account) {
        this.account = account;
    }
}
