//package ch.azure.aurore.javaxt.reflection;
//
//import java.lang.annotation.Annotation;
//
//public class PropertyInfo {
//    private final MethodInfo accessor;
//    private final MethodInfo mutator;
//    private final FieldInfo backingField;
//
//    public PropertyInfo(MethodInfo accessor, MethodInfo mutator, FieldInfo backingField) {
//        this.accessor = accessor;
//        this.mutator = mutator;
//        this.backingField = backingField;
//    }
//
//    //region accessors
//    public MethodInfo getAccessor() {
//        return accessor;
//    }
//
//    public MethodInfo getMutator() {
//        return mutator;
//    }
//
//    public FieldInfo getBackingField() {
//        return backingField;
//    }
//    //endregion
//
//    public <T extends Annotation> T getAnnotationIfPresent(Class<T> clazz) {
//        return backingField.getAnnotationIfPresent(clazz);
//    }
//
//    public String getName() {
//        return backingField.getName();
//    }
//
//    @Override
//    public String toString() {
//        return "PropertyInfo{" +
//                "backingField=" + backingField.getName() +
//                '}';
//    }
//
//    public Class<?> getType() {
//        return backingField.getType();
//    }
//
//    public Class<?> getComponentType() {
//        return backingField.getComponentType();
//    }
//
//    public Class<?> getDeclaringClass() {
//        return backingField.getDeclaringClass();
//    }
//
//    public Object invokeAccessor(Object data) {
//        return accessor.invokeGetter(data);
//    }
//
//    public void invokeMutator(Object data, Object array) {
//        mutator.invokeSetter(data, array);
//    }
//}