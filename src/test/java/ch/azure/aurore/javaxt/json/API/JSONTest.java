package ch.azure.aurore.javaxt.json.API;

import ch.azure.aurore.javaxt.json.Records;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

// import static org.junit.jupiter.api.Assertions.*;

class JSONTest {

    @Test
    void toJSON_list() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1962);
        list.add(1963);
        list.add(1965);

        String str = JSON.toJSON(list);
        Assertions.assertEquals("[ 1962, 1963, 1965 ]", str);
        System.out.println(str);
    }

    @Test
    void item_toStr(){
        Records r = new Records(24, "Gregory");
        var str = JSON.toJSON(r);
        System.out.println(str);
    }

    @Test
    void readStr(){
        Records r = new Records(24, "Gregory");
        var str = JSON.toJSON(r);
        System.out.println(str);

        var q = JSON.readItem(Records.class, str);
        System.out.println(q == null);
    }
}