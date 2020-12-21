package ch.azure.aurore.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

//import static org.junit.jupiter.api.Assertions.*;

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
}