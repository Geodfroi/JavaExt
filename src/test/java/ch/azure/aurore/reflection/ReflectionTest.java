package ch.azure.aurore.reflection;

import ch.azure.aurore.tuples.Trio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("unused")
class Employee{
    private String name;
    private int id;
    boolean fired = true;
    boolean found = false;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFired() {
        return fired;
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}

class ReflectionTest {

    @Test
    void getProperties()
    {
        List<Trio<String, Method, Method>> list = Reflection.getProperties(Employee.class);

        String[] expected = new String[]{"fired", "found", "name"};
        for (int n = 0; n < list.size(); n++) {
            Assertions.assertEquals(expected[n], list.get(n).getVal0());
        }
    }

    @Test
    void getGetterMethods(){
        Method[] m = Reflection.getGetterMethods(Employee.class);
        assert m.length ==4;
    }
    
    @Test
    void getSetterMethods(){
        Method[] m = Reflection.getSetterMethods(Employee.class);
        assert m.length ==4;
    }
}