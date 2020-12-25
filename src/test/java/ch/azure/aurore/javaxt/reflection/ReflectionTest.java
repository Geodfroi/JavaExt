package ch.azure.aurore.javaxt.reflection;

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

//    @Test
//    void getGetterMethods(){
//        Method[] m = Reflection.getGetterMethods(Employee.class);
//        assert m.length ==4;
//    }
//
//    @Test
//    void getSetterMethods(){
//        Method[] m = Reflection.getSetterMethods(Employee.class);
//        assert m.length ==3;
//    }
}