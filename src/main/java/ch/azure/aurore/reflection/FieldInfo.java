package ch.azure.aurore.reflection;

import ch.azure.aurore.generics.Generics;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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

    public MethodInfo getAccessor() {
        return accessor;
    }
    //endregion

    //region mutators
    public void setMutator(MethodInfo methodInfo) {
        this.mutator = methodInfo;
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

    public String getName(){
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

    @Override
    public String toString() {
        return "FieldInfo{" +
                "f=" + f +
                '}';
    }
}
