package ch.azure.aurore.javaxt.reflection;

import java.util.HashMap;
import java.util.Map;

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

//    public static MethodInfo getInfo(Method m){
//        var clazz = getInfo(m.getDeclaringClass());
//        return clazz.getMethod(m.getName());
//    }

    public static ClassInfo getInfo(Class<?> clazz) {
        if (!infoMap.containsKey(clazz))
            infoMap.put(clazz, new ClassInfo(clazz));
        return infoMap.get(clazz);
    }
}