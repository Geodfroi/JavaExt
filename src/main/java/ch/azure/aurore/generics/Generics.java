package ch.azure.aurore.generics;

import org.apache.commons.lang3.ClassUtils;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class Generics {

    public static Class<?> getComponentType(Class<?> aClass, String collectionName)
    {
        Field field;
        try {
            field = aClass.getDeclaredField(collectionName);
        } catch (NoSuchFieldException e) {
            System.out.println("Can't find [" + collectionName + "] declared field");
            return null;
        }
        Class<?> fieldType = field.getType();
        if (ClassUtils.isPrimitiveOrWrapper(fieldType)){
            return null;
        }
        if (fieldType.isArray()){
            return fieldType.getComponentType();
        }

        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        return (Class<?>) stringListType.getActualTypeArguments()[0];
    }

    public static Class<?> getComponentType(Field field) {
        return getComponentType(field.getDeclaringClass(), field.getName());
    }
}
