package ch.azure.aurore.Strings;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

//import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    private static final String CAMEL_TEST_STRING = "Test string";
    private static final String CAMEL_EXPECTED_STRING = "TestString";

    private static final String REVERT_CAMEL_TEST_STRING = "ArgentMonty";
    private static final String REVERT_CAMEL_EXPECTED_STRING = "Argent monty";

    @org.junit.jupiter.api.Test
    void camel_null()
    {
        String str = Strings.camel(null);
        assert (str.equals(""));
    }
    @org.junit.jupiter.api.Test
    void camel_blank()
    {
        String str = Strings.camel("  ");
        assert (str.equals(""));
    }

    @org.junit.jupiter.api.Test
    void camel()
    {
        String str = Strings.camel(CAMEL_TEST_STRING);
        Assertions.assertEquals(str, CAMEL_EXPECTED_STRING);
    }

    @org.junit.jupiter.api.Test
    void camel_same()
    {
        String str = Strings.camel(CAMEL_EXPECTED_STRING);
        assert (str.equals(CAMEL_EXPECTED_STRING));
    }

    @org.junit.jupiter.api.Test
    void unCamel()
    {
        String str = Strings.unCamel(REVERT_CAMEL_TEST_STRING);
        assert (str.equals(REVERT_CAMEL_EXPECTED_STRING));
    }

    @org.junit.jupiter.api.Test
    void unCamel_same()
    {
        String str = Strings.unCamel(REVERT_CAMEL_EXPECTED_STRING);
        assert (str.equals(REVERT_CAMEL_EXPECTED_STRING));
    }

    @org.junit.jupiter.api.Test
    void listToString_ints() {

        List<Integer> list = new ArrayList<>();
        list.add(43);
        list.add(64);
        list.add(89);

        String expected = "43, 64, 89";
        Assertions.assertEquals(Strings.toString(list), expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_ints_nullMember() {

        List<Integer> list = new ArrayList<>();
        list.add(43);
        list.add(null);
        list.add(89);

        String expected = "43, 89";
        Assertions.assertEquals(Strings.toString(list), expected);
    }

    @org.junit.jupiter.api.Test
    void collectionToString_stream()
    {
        List<String> list = new ArrayList<>();
        list.add("adam");
        list.add("eve");
        String expected = "adam&eve";

        Assertions.assertEquals(Strings.toString(list.stream(),"&"), expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_strings() {
        List<String> list = new ArrayList<>();
        list.add("albert");
        list.add("john");
        list.add("Ivy");

        String expected = "albert--john--Ivy";
        Assertions.assertEquals(Strings.toString(list, "--"), expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_strings_emptyString() {
        List<String> list = new ArrayList<>();
        list.add("albert");
        list.add("");
        list.add("Ivy");

        String expected = "albert--Ivy";
        Assertions.assertEquals(Strings.toString(list, "--"), expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_empty() {
        List<String> list = new ArrayList<>();
        Assertions.assertEquals(Strings.toString(list, "--"),"");
    }

    @org.junit.jupiter.api.Test
    void listToString_null() {
        Assertions.assertEquals(Strings.toString((List<String>)null, "--"),"");
    }
}