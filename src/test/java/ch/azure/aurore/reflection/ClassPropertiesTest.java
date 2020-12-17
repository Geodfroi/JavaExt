package ch.azure.aurore.reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClassPropertiesTest {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Test
    void getProperties()
    {
        ClassProperties properties = new ClassProperties(Employee.class);

        String[] expected = new String[]{"fired", "found", "name"};
        for (int n = 0; n < properties.size(); n++) {
            Assertions.assertEquals(expected[n], properties.get(n).getName());
        }
    }
}