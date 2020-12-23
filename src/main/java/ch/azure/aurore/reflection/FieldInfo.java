package ch.azure.aurore.reflection;

import ch.azure.aurore.generics.Generics;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldInfo {

    private final Field f;
    private MethodInfo mutator;
    private MethodInfo accessor;

    public FieldInfo(Field f) {
        this.f = f;
    }

    //region accessors
    public MethodInfo getMutator() {
        return mutator;
    }

    //region mutators
    public void setMutator(MethodInfo methodInfo) {
        this.mutator = methodInfo;
    }
    //endregion

    public MethodInfo getAccessor() {
        return accessor;
    }

    public void setAccessor(MethodInfo methodInfo) {
        this.accessor = methodInfo;
    }
    //endregion

    public <T extends Annotation> T getAnnotationIfPresent(Class<T> aClass) {
        if (isAnnotationPresent(aClass))
            return f.getAnnotation(aClass);

        return null;
    }

    public Class<?> getComponentType() {
        return Generics.getComponentType(f);
    }

    public Class<?> getDeclaringClass() {
        return f.getDeclaringClass();
    }

    public String getName() {
        return f.getName();
    }

    public Class<?> getType() {
        return f.getType();
    }

    public boolean hasAccessor() {
        return accessor != null;
    }

    public boolean hasMutator() {
        return mutator != null;
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
        return f.isAnnotationPresent(clazz);
    }

    public boolean isNamed(String name) {
        Pattern p = Pattern.compile("^_?" + name + "$", Pattern.CASE_INSENSITIVE );
        Matcher m = p.matcher(f.getName());
        return m.matches();
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "f=" + f +
                '}';
    }
}
