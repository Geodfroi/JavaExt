package ch.azure.aurore.reflection;

import ch.azure.aurore.strings.Strings;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassInfo {
    private final Class<?> clazz;
    private final List<FieldInfo> fields = new ArrayList<>();
    private final List<MethodInfo> methods = new ArrayList<>();

    public ClassInfo(Class<?> clazz) {
        this.clazz = clazz;

        for (Field f : FieldUtils.getAllFieldsList(clazz)) {
            fields.add(new FieldInfo(f));
        }

        for (Method m : clazz.getDeclaredMethods()) {
            Pair<Boolean, String> r = isMutator(m);
            if (r.getKey()) {
                FieldInfo backingField = getField(r.getValue());
                if (backingField != null) {
                    MethodInfo methodInfo = new MethodInfo(m, backingField, MethodInfo.MethodType.MUTATOR);
                    methods.add(methodInfo);
                    backingField.setMutator(methodInfo);
                }
            }
            r = isAccessor(m);
            if (r.getKey()) {
                FieldInfo backingField = getField(r.getValue());
                if (backingField != null) {
                    MethodInfo methodInfo = new MethodInfo(m, backingField, MethodInfo.MethodType.ACCESSOR);
                    methods.add(methodInfo);
                    backingField.setAccessor(methodInfo);
                }
            }
            methods.add(new MethodInfo(m, null, MethodInfo.MethodType.UNDETERMINED));
        }
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public FieldInfo getField(String name) {
        if (name.startsWith("_"))
            name = name.substring(1);

        Pattern p = Pattern.compile("^_?" + name + "$");
        for (FieldInfo f : fields) {
            Matcher m = p.matcher(f.getName());
            if (m.matches())
                return f;
        }
        return null;
    }

    public FieldInfo getFieldWith(Class<? extends Annotation> clazz) {
        for (var f : fields) {
            if (f.getAnnotationIfPresent(clazz) != null)
                return f;
        }
        return null;
    }

    public MethodInfo getMethod(String name) {
        if (name.startsWith("_"))
            name = name.substring(1);

        Pattern p = Pattern.compile("^_?" + name + "$");
        for (MethodInfo f : methods) {
            Matcher m = p.matcher(f.getName());
            if (m.matches())
                return f;
        }
        return null;
    }

    public MethodInfo getMethodWith(Class<? extends Annotation> clazz) {
        for (MethodInfo m : methods) {
            if (m.isAnnotationPresent(clazz))
                return m;
        }
        return null;
    }

    public boolean hasAccessibleConstructor() {
        Constructor<?> c = ConstructorUtils.getAccessibleConstructor(this.clazz);
        return c != null;
    }

    private Pair<Boolean, String> isMutator(Method method) {
        boolean result = Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z_].*$");

        return new ImmutablePair<>(result, Strings.toFirstLower(method.getName().substring(3)));
    }

    private Pair<Boolean, String> isAccessor(Method method) {
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
        return new ImmutablePair<>(result, name);
    }
}

//    public static AccessorInfo[] getGetterMethods(Class<?> aClass) {
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
