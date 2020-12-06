package ch.azure.aurore.Strings;

import java.util.ArrayList;
import java.util.List;

//import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    private static final String CAMEL_TEST_STRING = "TeSt strIng";
    private static final String CAMEL_EXPECTED_STRING = "testString";

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
        assert (str.equals(CAMEL_EXPECTED_STRING));
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
        assert Strings.toString(list).equals(expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_ints_nullMember() {

        List<Integer> list = new ArrayList<>();
        list.add(43);
        list.add(null);
        list.add(89);

        String expected = "43, 89";
        assert Strings.toString(list).equals(expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_strings() {
        List<String> list = new ArrayList<>();
        list.add("albert");
        list.add("john");
        list.add("Ivy");

        String expected = "albert--john--Ivy";
        assert Strings.toString(list, "--").equals(expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_strings_emptyString() {
        List<String> list = new ArrayList<>();
        list.add("albert");
        list.add("");
        list.add("Ivy");

        String expected = "albert--Ivy";
        assert Strings.toString(list, "--").equals(expected);
    }

    @org.junit.jupiter.api.Test
    void listToString_empty() {
        List<String> list = new ArrayList<>();
        assert Strings.toString(list, "--").equals("");
    }

    @org.junit.jupiter.api.Test
    void listToString_null() {
        assert Strings.toString(null, "--").equals("");
    }
}