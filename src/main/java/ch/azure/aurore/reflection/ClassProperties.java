package ch.azure.aurore.reflection;

import ch.azure.aurore.strings.Strings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A collection of accessible fields by name with their associated public getter and setter methods.
 */
public class ClassProperties extends ArrayList<ClassProperty> {

    private final String className;

    public ClassProperties(Class<?> aClass) {
        this.className = aClass.getSimpleName();
        List<Method> setterMethods = new ArrayList<>();
        List<Method> getterMethods = new ArrayList<>();

        for (Method m:aClass.getDeclaredMethods()) {
            if (Reflection.isSetter(m))
                setterMethods.add(m);
            if (Reflection.isGetter(m))
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

        for (int n = 0; n < getterFields.size(); n++) {
            String name = getterFields.get(n);
            for (int k = 0; k < setterFields.size(); k++) {

                if (name.equals(setterFields.get(k))){
                    Class<?> type = getterMethods.get(n).getReturnType();
                    ClassProperty p = new ClassProperty(name,type, getterMethods.get(n), setterMethods.get(k));
                    this.add(p);
                    break;
                }
            }
        }

        this.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
    }

    public String getClassName() {
        return className;
    }
//
//    public Class<?> getPropertyType(String fieldName) {
//
//
//        for (ClassProperty p:this) {
//            if (p.getName().equals(fieldName)){
//                return p.getType();
//            }
//        }
//        return null;
//        //throw new RuntimeException("Can't find field [" + fieldName + "] in [" + className + "] class");
//    }

//    public Object getPropertyValue(Object obj, String fieldName) {
//
//        var p = getProperty(fieldName);
//        if (p)
//        for (ClassProperty p:this) {
//            if (p.getName().equals(fieldName)){
//                try {
//
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public ClassProperty getProperty(String fieldName) {
        for (ClassProperty p:this) {
            if (p.getName().equals(fieldName)){
                return p;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ClassProperties{" +
                "className='" + className + '\'' +
                '}';
    }

}
