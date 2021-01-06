package ch.azure.aurore.javaxt.reflection;

import org.junit.jupiter.api.Test;

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
    void createInstance_fromClass()  {
        Puddle puddle = Reflection.createInstance(Puddle.class, "Mr. Waffles");
        assert puddle.getName().equals("Mr. Waffles");
    }

    @Test
    void createInstance_fromString()  {
        Puddle puddle = Reflection.createInstance("ch.azure.aurore.javax.reflection.Puddle", "Mr. Waffles");
        assert puddle.getName().equals("Mr. Waffles");
    }

}