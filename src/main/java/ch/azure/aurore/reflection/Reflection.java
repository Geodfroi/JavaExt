package ch.azure.aurore.reflection;

import ch.azure.aurore.strings.Strings;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * http://tutorials.jenkov.com/java-reflection/index.html
 * https://asgteach.com/2012/11/finding-getters-and-setters-with-java-reflection/
 */
public class Reflection {

    public static Class<?> getClass(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            System.out.println("No [" + clazz + "] found in project");
            e.printStackTrace();
        }
        return null;
    }

    public static AccessorInfo isGetter(Method method) {
        boolean result = false;
        String name = "";
        if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
            if (method.getName().matches("^get[A-Z_].*$")) {
                result = !method.getReturnType().equals(void.class);
                name = Strings.toFirstLower(method.getName().substring(3));
            } else if (method.getName().matches("^is[A-Z_].*$")) {
                result = method.getReturnType().equals(boolean.class);
                name = Strings.toFirstLower(method.getName().substring(2));
            }
        }
        return new AccessorInfo(result, name);
    }

    public static MutatorInfo isSetter(Method method) {
        var result = Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z_].*$");

        return new MutatorInfo(result, Strings.toFirstLower(method.getName().substring(3)));
    }
}

//    public static AccessorInfo[] getGetterMethods(Class<?> aClass) {
//
//        return Arrays.stream(aClass.getDeclaredMethods()).
//                filter(Reflection::isGetter).
//                toArray(AccessorInfo[]::new);
//    }
//
//    public static Method[] getSetterMethods(Class<?> aClass) {
//        return Arrays.stream(aClass.getDeclaredMethods()).
//                filter(Reflection::isSetter).
//                toArray(Method[]::new);
//    }