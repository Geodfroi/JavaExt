package ch.azure.aurore.javaxt.conversions;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

class ConversionsTest {

    @org.junit.jupiter.api.Test
    void toBoolean(){
        assert Conversions.toBoolean("true");
        assert Conversions.toBoolean("1");
        assert Conversions.toBoolean(1);
        assert !Conversions.toBoolean(-13);
        assert !Conversions.toBoolean(null);
        assert !Conversions.toBoolean("irrelevant");
    }

    @org.junit.jupiter.api.Test
    void toString_intList() {

        List<Integer> list = new ArrayList<>();
        list.add(43);
        list.add(64);
        list.add(89);

        String expected = "43,64,89";
        Assertions.assertEquals(Conversions.toString(list), expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_ints_order() {

        List<Integer> list = new ArrayList<>();
        list.add(300);
        list.add(100);
        list.add(200);
        String expected = "100,200,300";
        Assertions.assertEquals(Conversions.toString(list, Integer::compareTo), expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_ints_nullMember() {

        List<Integer> list = new ArrayList<>();
        list.add(43);
        list.add(null);
        list.add(89);

        String expected = "43,89";
        Assertions.assertEquals(Conversions.toString(list), expected);
    }

    @org.junit.jupiter.api.Test
    void collectionToString_stream()
    {
        List<String> list = new ArrayList<>();
        list.add("adam");
        list.add("eve");
        String expected = "adam&eve";

        Assertions.assertEquals(Conversions.toString(list.stream(),"&"), expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_strings() {
        List<String> list = new ArrayList<>();
        list.add("albert");
        list.add("john");
        list.add("Ivy");

        String expected = "albert--john--Ivy";
        Assertions.assertEquals(Conversions.toString(list, "--"), expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_strings_emptyString() {
        List<String> list = new ArrayList<>();
        list.add("albert");
        list.add("");
        list.add("Ivy");

        String expected = "albert--Ivy";
        Assertions.assertEquals(Conversions.toString(list, "--"), expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_empty() {
        List<String> list = new ArrayList<>();
        Assertions.assertEquals(Conversions.toString(list, "--"),"");
    }

}