package ch.azure.aurore.reflection;

import ch.azure.aurore.strings.Strings;
import ch.azure.aurore.tuples.Trio;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * @param aClass the class where the properties are defined
     * @return a map of accessible fields by name with their associated public getter and setter methods.
     */
    public static List<Trio<String, Method, Method>> getProperties(Class<?> aClass) {

        List<Method> setterMethods = new ArrayList<>();
        List<Method> getterMethods = new ArrayList<>();

        for (Method m:aClass.getDeclaredMethods()) {
            if (isSetter(m))
                setterMethods.add(m);
            if (isGetter(m))
                getterMethods.add(m);
        }

        List<String> getterFields = getterMethods.stream().
                map(m -> {
                    if (m.getName().startsWith("is"))
                        return Strings.toFirstLower(m.getName().substring(2));
                    else
                        return (Strings.toFirstLower(m.getName().substring(3)));
                }).collect(Collectors.toList());

        List<String> setterFields = setterMethods.stream().
                map(m -> Strings.toFirstLower(m.getName().substring(3))).
                collect(Collectors.toList());

        List<Trio<String,Method,Method>> list = new ArrayList<>();
        for (int n = 0; n < getterFields.size(); n++) {
            String name = getterFields.get(n);
            for (int k = 0; k < setterFields.size(); k++) {
                if (name.equals(setterFields.get(k))){
                    list.add(new Trio<>(name, getterMethods.get(n), setterMethods.get(k)));
                    break;
                }
            }
        }
        list.sort((o1, o2) -> o1.getVal0().compareToIgnoreCase(o2.getVal0()));
        return list;
    }

    public static Method[] getSetterMethods(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredMethods()).
                filter(Reflection::isSetter).
                toArray(Method[]::new);
    }

    private static boolean isGetter(Method method){
        if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
            if (method.getName().matches("^get[A-Z].*$")) {
                return !method.getReturnType().equals(void.class);
            }
            if (method.getName().matches("^is[A-Z].*$"))
                return method.getReturnType().equals(boolean.class);
        }
        return false;
    }

    private static boolean isSetter(Method method){
        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z].*$");
    }
}
