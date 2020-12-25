package ch.azure.aurore.javaxt.strings;

import org.junit.jupiter.api.Assertions;

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
    void toFirstLower(){
        String str = "Abel";
        Assertions.assertEquals("abel", Strings.toFirstLower(str));
    }

    @org.junit.jupiter.api.Test
    void toFirstUpper(){
        String str = "abel";
        Assertions.assertEquals("Abel", Strings.toFirstUpper(str));
    }

    @org.junit.jupiter.api.Test
    void toFirstLower_empty(){
        String str = " ";
        Assertions.assertEquals(" ", Strings.toFirstLower(str));
    }
}