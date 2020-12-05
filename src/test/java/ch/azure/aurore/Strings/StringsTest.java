package ch.azure.aurore.Strings;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    private static final String CAMEL_TEST_STRING = "TeSt strIng";
    private static final String CAMEL_EXPECTED_STRING = "testString";

    private static final String UNCAMEL_TEST_STRING = "ArgentMonty";
    private static final String UNCAMEL_EXPECTED_STRING = "Argent monty";

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
        String str = Strings.unCamel(UNCAMEL_TEST_STRING);
        assert (str.equals(UNCAMEL_EXPECTED_STRING));
    }

    @org.junit.jupiter.api.Test
    void unCamel_same()
    {
        String str = Strings.unCamel(UNCAMEL_EXPECTED_STRING);
        assert (str.equals(UNCAMEL_EXPECTED_STRING));
    }
}