package ch.azure.aurore.javaxt.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * http://tutorials.jenkov.com/java-reflection/index.html
 * https://asgteach.com/2012/11/finding-getters-and-setters-with-java-reflection/
 */
public class Reflection {

    private static final Map<Class<?>, ClassInfo> infoMap = new HashMap<>();

    public static Class<?> getClass(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            System.out.println("No [" + clazz + "] found in project");
            e.printStackTrace();
        }
        return null;
    }

    public static ClassInfo getInfo(Class<?> clazz) {
        if (!infoMap.containsKey(clazz))
            infoMap.put(clazz, new ClassInfo(clazz));
        return infoMap.get(clazz);
    }

    /**
     * @param objectClass The object class
     * @return All public methods including inherited from super class.
     */
    public static Method[] getMethods(Class<?> objectClass) {
        Set<Method> allMethods = new HashSet<>(Arrays.asList(objectClass.getMethods()));
        if (objectClass.getSuperclass() != null) {
            Collections.addAll(allMethods, getMethods(objectClass.getSuperclass()));
        }
        return allMethods.toArray(new Method[0]);
    }

    public static boolean isAnnotationPresent(Class<?> aClass, Class<? extends Annotation> annotation) {
        if (aClass.isAnnotationPresent(annotation))
            return true;
        if (aClass.getSuperclass() != null)
            return isAnnotationPresent(aClass.getSuperclass(), annotation);
        return false;
    }

    public static <T extends Annotation> T getAnnotation(Class<?> aClass, Class<T> annotation) {
        if (aClass.isAnnotationPresent(annotation))
            return aClass.getAnnotation(annotation);
        if (aClass.getSuperclass() != null)
            return getAnnotation(aClass.getSuperclass(), annotation);
        return null;
    }


    public static <T> T createInstance(String className, Object... params) {
        try {
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) Class.forName(className);
            return createInstance(clazz, params);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't find [" + className + "]");
        }
    }

    public static <T> T createInstance(Class<T> clazz, Object... params) {

        Class<?>[] types = Arrays.stream(params).
                map(Object::getClass).toArray(Class[]::new);

        Constructor<T> constructor;
        try {
            constructor = clazz.getConstructor(types);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to get [" + clazz + "] constructor with [" + params.length + "] parameters");
        }
        try {
            return constructor.newInstance(params);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failure to instantiate [" + clazz + "class");
        }
    }
}