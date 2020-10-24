package JavaExt.Tests;

import JavaExt.Collections.CollectionSt;

import java.util.ArrayList;
import java.util.List;

class CollectionStTest {

    @org.junit.jupiter.api.Test
    void testToString() {

        List<Integer> list = new ArrayList<>();
        list.add(43);
        list.add(64);
        list.add(89);

        System.out.println(CollectionSt.toString(list));
    }

    @org.junit.jupiter.api.Test
    void testToString2() {

        List<String> list = new ArrayList<>();
        list.add("albert");
        list.add("john");
        list.add("Ivy");

        System.out.println(CollectionSt.toString(list, "--"));
    }

    @org.junit.jupiter.api.Test
    void testToString_empty() {

        List<String> list = new ArrayList<>();
        System.out.println(CollectionSt.toString(list, "--"));
    }

    @org.junit.jupiter.api.Test
    void testToString_null() {
        System.out.println(CollectionSt.toString(null, "--"));
    }

}