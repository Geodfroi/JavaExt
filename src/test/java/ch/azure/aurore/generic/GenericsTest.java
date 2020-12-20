package ch.azure.aurore.generic;

import ch.azure.aurore.generics.Generics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unused")
class GenericsTest {

    private final Collection<String> strList = new ArrayList<>();
    private final int[] intArray = new int[0];

    @Test
    public void getComponentType_list() {
        Class<?> type = Generics.getComponentType(GenericsTest.class, "strList");
        Assertions.assertEquals(String.class, type);
    }

    @Test
    public void getComponentType_null() {
        Class<?> type = Generics.getComponentType(GenericsTest.class, "don'tExist");
        Assertions.assertNull(type);
    }

    @Test
    public void getComponentType_array() {
        Class<?> type = Generics.getComponentType(GenericsTest.class, "intArray");
        Assertions.assertEquals(int.class, type);
    }
}