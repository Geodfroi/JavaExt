package ch.azure.aurore.generics;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Generics {

    public static Class<?> getComponentType(Class<?> aClass, String collectionName) {
        Field field;
        try {
            field = aClass.getDeclaredField(collectionName);
        } catch (NoSuchFieldException e) {
            System.out.println("Can't find [" + collectionName + "] declared field");
            return null;
        }
        Class<?> fieldType = field.getType();
        if (ClassUtils.isPrimitiveOrWrapper(fieldType)) {
            return null;
        }
        if (fieldType.isArray()) {
            return fieldType.getComponentType();
        }

        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        return (Class<?>) stringListType.getActualTypeArguments()[0];
    }

    public static Class<?> getComponentType(Field field) {
        return getComponentType(field.getDeclaringClass(), field.getName());
    }

    @SuppressWarnings("rawtypes")
    public static List<Object> getCollectionFromField(Object val) {
        List<Object> list = new ArrayList<>();
        if (Collection.class.isAssignableFrom(val.getClass())) {
            for (Object obj : (Iterable) val) {
                list.add(obj);
            }
        } else if (val.getClass().isArray()) {
            for (int n = 0; n < Array.getLength(val); n++) {
                Object item = Array.get(val, n);
                list.add(item);
            }
        }
        return list;
    }
}
