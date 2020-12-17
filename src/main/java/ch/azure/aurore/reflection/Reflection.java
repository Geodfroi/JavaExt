package ch.azure.aurore.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * http://tutorials.jenkov.com/java-reflection/index.html
 * https://asgteach.com/2012/11/finding-getters-and-setters-with-java-reflection/
 */
public class Reflection {

    public static Method[] getGetterMethods(Class<?> aClass) {

        return Arrays.stream(aClass.getDeclaredMethods()).
                filter(Reflection::isGetter).
                toArray(Method[]::new);
    }


    public static Method[] getSetterMethods(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredMethods()).
                filter(Reflection::isSetter).
                toArray(Method[]::new);
    }

    public static boolean isGetter(Method method){
        if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
            if (method.getName().matches("^get[A-Z].*$")) {
                return !method.getReturnType().equals(void.class);
            }
            if (method.getName().matches("^is[A-Z].*$"))
                return method.getReturnType().equals(boolean.class);
        }
        return false;
    }

    public static boolean isSetter(Method method){
        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z].*$");
    }
}
