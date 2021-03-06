package ch.azure.aurore.javaxt.generic;

import ch.azure.aurore.javaxt.generics.Generics;
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
        Class<?>[] type = Generics.getComponentType(GenericsTest.class, "strList");
        assert type != null && type[0].equals(String.class);
    }

    @Test
    public void getComponentType_null() {
        Class<?>[] type = Generics.getComponentType(GenericsTest.class, "don'tExist");
        assert type != null && type.length == 0;
    }

    @Test
    public void getComponentType_array() {
        Class<?>[] type = Generics.getComponentType(GenericsTest.class, "intArray");
        assert type != null && type[0].equals(int.class);
    }
}