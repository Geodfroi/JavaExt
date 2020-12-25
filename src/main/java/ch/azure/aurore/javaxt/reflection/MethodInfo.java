package ch.azure.aurore.javaxt.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInfo {
    private final FieldInfo backingField;
    private final Annotation[] annotations;
    private final Method m;
    private final MethodType methodType;

    public MethodInfo(Method m, FieldInfo backingField, MethodType methodType) {
        this.m = m;
        this.backingField = backingField;
        this.methodType = methodType;
        this.annotations = m.getAnnotations();
    }

    //region Accessors
    public FieldInfo getBackingField() {
        return backingField;
    }

    public MethodType getMethodType() {
        return methodType;
    }
    //endregion

    public Object invoke(Object data) {
        if (methodType == MethodType.ACCESSOR || methodType == MethodType.UNDETERMINED) {
            try {
                return m.invoke(data);
            } catch (InvocationTargetException | IllegalAccessException e) {
                System.out.println("Failed to get value from field [" + backingField.getName() + "] for class [" + backingField.getDeclaringClass().getSimpleName() + "]");
                e.printStackTrace();
            }
        }
        return null;
    }

    public void invoke(Object data, Object value) {
        if (methodType == MethodType.MUTATOR) {
            try {
                m.invoke(data, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.println("Failed to set value in field [" + backingField.getName() + "] for class [" + backingField.getDeclaringClass().getSimpleName() + "]");
                e.printStackTrace();
            }
        }
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
        return getAnnotationIfPresent(clazz) != null;
    }

    public <T extends Annotation> T getAnnotationIfPresent(Class<T> clazz) {
        for (Annotation a : annotations) {
            if (a.getClass().equals(clazz)) {
                //noinspection unchecked
                return (T) a;
            }
        }
        return null;
    }

    public String getName() {
        return m.getName();
    }

    public enum MethodType {
        ACCESSOR, MUTATOR, UNDETERMINED
    }
}