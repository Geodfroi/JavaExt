package ch.azure.aurore.javaxt.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldInfo {

    private final Field f;
    private final FieldType fieldType;
    private MethodInfo mutator;
    private MethodInfo accessor;

    public FieldInfo(Field f) {
        this.f = f;
        fieldType = FieldType.getFieldType(f);
    }

    //region Accessors
    public MethodInfo getAccessor() {
        return accessor;
    }

    //region Mutators
    public void setAccessor(MethodInfo methodInfo) {
        this.accessor = methodInfo;
    }

    public MethodInfo getMutator() {
        return mutator;
    }
    //endregion

    public void setMutator(MethodInfo methodInfo) {
        this.mutator = methodInfo;
    }

    //endregion

    public <T extends Annotation> T getAnnotationIfPresent(Class<T> aClass) {
        if (isAnnotationPresent(aClass))
            return f.getAnnotation(aClass);

        return null;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public Class<?> getDeclaringClass() {
        return f.getDeclaringClass();
    }

    public String getName() {
        return f.getName();
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
        Pattern p = Pattern.compile("^_?" + name + "$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(f.getName());
        return m.matches();
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "f=" + f +
                '}';
    }

    public Class<?> getType() {
        return f.getType();
    }
}
