package ch.azure.aurore.reflection;

import java.lang.reflect.Method;

public class ClassProperty {
    private final String name;
    private final Class<?> type;
    private final Method getterMethod;
    private final Method setterMethod;

    public ClassProperty(String name, Class<?> type, Method getterMethod, Method setterMethod) {
        this.name = name;
        this.type = type;
        this.getterMethod = getterMethod;
        this.setterMethod = setterMethod;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Method getGetterMethod() {
        return getterMethod;
    }

    public Method getSetterMethod() {
        return setterMethod;
    }

    @Override
    public String toString() {
        return "ClassProperty{" +
                "name='" + name + '\'' +
                ", type=" + type.getSimpleName() +
                '}';
    }

    public void setPropertyValue(Object obj, Object value) {
        try {
            getSetterMethod().invoke(obj, value);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new RuntimeException("Error invoking setter method for property [" + name + "] of type [" + type + "]");
        }
    }

    public Object getPropertyValue(Object obj)
    {
        try {
            return getGetterMethod().invoke(obj);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new RuntimeException("Error invoking getter method for property [" + name + "] of type [" + type + "]");
        }
    }
}
