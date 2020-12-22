package ch.azure.aurore.sqlite;

import ch.azure.aurore.reflection.ClassInfo;
import ch.azure.aurore.reflection.FieldInfo;
import ch.azure.aurore.reflection.MethodInfo;
import ch.azure.aurore.reflection.Reflection;
import ch.azure.aurore.sqlite.wrapper.annotations.DBPack;
import ch.azure.aurore.sqlite.wrapper.annotations.DBUnpack;
import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseClass;
import ch.azure.aurore.sqlite.wrapper.annotations.PrimaryKey;
import ch.azure.aurore.strings.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * https://www.baeldung.com/java-custom-annotation
 * https://stackoverflow.com/questions/1942644/get-generic-type-of-java-util-list
 */
public class FieldsData {

    public static final String MODIFIER_ACCESSOR = "isModified";
    private final String className;
    private final List<FieldData> fields = new ArrayList<>();
    private FieldInfo idField;
    private final MethodInfo unpackMethod;
    private final MethodInfo packMethod;
    private final MethodInfo isModifiedAccessor;

    public FieldsData(Class<?> aClass) {
        if (!aClass.isAnnotationPresent(DatabaseClass.class)) {
            throw new IllegalStateException("Class [" + aClass.getSimpleName() + "] must have [DatabaseClass] annotation to be imported into database");
        }
        ClassInfo classInfo = Reflection.getInfo(aClass);

        this.className = FieldsData.getClassDBName(aClass);

        if (!classInfo.hasAccessibleConstructor())
            throw new RuntimeException("Class [" + aClass.getSimpleName() + "] does not contains a public parameterless constructor");

        for (FieldInfo f : classInfo.getFields())
        {
            if (f.isAnnotationPresent(PrimaryKey.class))
                idField = f;
            else{
                if (f.hasAccessor() && f.hasMutator())
                    fields.add(new FieldData(f));
            }
        }

        isModifiedAccessor = classInfo.getMethod(MODIFIER_ACCESSOR);

        if (idField == null)
            throw new IllegalStateException("Class [" + aClass.getSimpleName() + "] does not contains a [id] primary key field with public accessor and mutator");
        if (isModifiedAccessor == null)
            throw new IllegalStateException("Class [" + aClass.getSimpleName() + "] does not contains a public [isModified] accessor method ");

        unpackMethod = classInfo.getMethodWith(DBPack.class);
        packMethod = classInfo.getMethodWith(DBUnpack.class);

        this.fields.sort((o1, o2) -> o1.getColumnName().compareToIgnoreCase(o2.getColumnName()));
    }

    public static String getClassDBName(Class<?> aClass) {
        DatabaseClass annotation = aClass.getAnnotation(DatabaseClass.class);
        return Strings.isNullOrEmpty(annotation.dbName()) ? aClass.getSimpleName() : annotation.dbName();
    }

    //region accessors
    public String getClassName() {
        return className;
    }
    //endregion

    public List<FieldData> getFields() {
        return fields;
    }

    public FieldData getField(String columnName) {
        for (FieldData f : fields) {
            if (f.getColumnName().equals(columnName))
                return f;
        }
        return null;
    }

    public int getID(Object data) {
        return (int) idField.getAccessor().invoke(data);
    }

    public boolean isModified(Object data) {
        return (Boolean) isModifiedAccessor.invoke(data);
    }

    public void pack(Object data) {
        if (packMethod != null)
            packMethod.invoke(data);
    }

    public void setID(Object data, int id) {
        idField.getMutator().invoke(data, id);
    }

    @Override
    public String toString() {
        return "FieldsData{" +
                "className='" + className + '\'' +
                '}';
    }

    public void unpack(Object data) {
        if (unpackMethod != null)
            unpackMethod.invoke(data);
    }
}

//    @Deprecated
//    public Map<String, Field> getPrimitiveTypeFields() {
//        return primitiveTypeFields;
//    }
//
//    @Deprecated
//    public Map<String, Field> getHierarchyClassFields() {
//        return hierarchyClassFields;
//    }
//
//    @Deprecated
//    public Field getPrimitiveTypeField(String dbName)
//    {
//        if (primitiveTypeFields.containsKey(dbName))
//            return primitiveTypeFields.get(dbName);
//
//        System.out.println("Failure to find field [" + dbName + "] in class [" + className + "]");
//        return null;
//    }
//
//    public Field getField(FieldCategory category, String dbName){
//        switch (category) {
//            case hierarchyClass:
//                if (hierarchyClassFields.containsKey(dbName))
//                    return hierarchyClassFields.get(dbName);
//                break;
//            case primitiveType:
//                if (primitiveTypeFields.containsKey(dbName))
//                    return primitiveTypeFields.get(dbName);
//        }
//        System.out.println("Failure to find field [" + dbName + "] in class [" + className + "]");
//        return null;
//    }
//
//    public boolean isIDField(String columnName) {
//        return columnName.equals(idField.getName());
//    }
//
//    @Override
//    public String toString() {
//        return "ClassSQLData{" +
//                "className='" + className + '\'' +
//                '}';
//    }

//    public Class<?> getPrimitiveFieldType(String dbName) {
//        Field field = primitiveTypeFields.get(dbName);
//        return field.getType();
//    }

//    public Object getFieldValue(FieldCategory category, Object data, String fieldName){
//        var field = getField(category, fieldName);
//        if (field != null) {
//            try {
//                return field.get(data);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//                System.out.println("Failed to get field [" + field.getName() + "] from class [" + className + "]");
//            }
//        }
//
//        return null;
//
//    }
//
//    @Deprecated
//    public Object getPrimitiveTypeValue(Object data, String fieldName) {
//        Field field = getPrimitiveTypeField(fieldName);
//        if (field != null)
//        {
//            try {
//                return field.get(data);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//                System.out.println("Failed to get field [" + field.getName() + "] from class [" + className + "]");
//            }
//        }
//        return null;
//    }
//
//    @Deprecated
//    public List<String> getHierarchyClassFieldNames() {
//        return hierarchyClassFields.keySet().stream().
//                sorted(String::compareToIgnoreCase).
//                collect(Collectors.toList());
//    }
//
//    @Deprecated
//    public List<String> getPrimitiveTypeFieldNames() {
//        return primitiveTypeFields.keySet().stream().
//                sorted(String::compareToIgnoreCase).
//                collect(Collectors.toList());
//    }
//
//    public List<String> getFieldNames(FieldCategory type){
//        switch (type){
//
//            case hierarchyClass:
//                return hierarchyClassFields.keySet().stream().
//                        sorted(String::compareToIgnoreCase).
//                        collect(Collectors.toList());
//
//            case primitiveType:
//                return primitiveTypeFields.keySet().stream().
//                        sorted(String::compareToIgnoreCase).
//                        collect(Collectors.toList());
//
//            case all:
//                List<String> fields = new ArrayList<>();
//                fields.addAll(hierarchyClassFields.keySet());
//                fields.addAll(primitiveTypeFields.keySet());
//                fields.stream().sorted(String::compareToIgnoreCase).collect(Collectors.toList());
//                return fields;
//        }
//        throw new RuntimeException("invalid [FieldType] parameter");
//    }
//
//    public void setID(Object obj, int value){
//        try {
//            idField.set(obj, value);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            System.out.println("Failed to set id field for class [" + className + "]");
//        }
//    }

//    public FieldCategory getFieldCategory(String dbName) {
//        if (isIDField(dbName))
//            return FieldCategory.idField;
//        else if (primitiveTypeFields.containsKey(dbName)){
//            return FieldCategory.primitiveType;
//        }else if(hierarchyClassFields.containsKey(dbName))
//            return FieldCategory.hierarchyClass;
//        else
//            return FieldCategory.notFound;
//    }
//
//    public Set<Map.Entry<String, Field>> getFields(FieldCategory category) {
//        switch (category) {
//            case hierarchyClass:
//                return hierarchyClassFields.entrySet();
//            case primitiveType:
//                return primitiveTypeFields.entrySet();
//        }
//        throw new RuntimeException("Invalid [" + category.toString() + "] category");
//    }
//
//    public int getFieldsCount(FieldCategory category) {
//        switch (category) {
//            case hierarchyClass:
//                return hierarchyClassFields.size();
//            case primitiveType:
//                return primitiveTypeFields.size();
//            case all:
//                return hierarchyClassFields.size() + primitiveTypeFields.size();
//        }
//        throw new RuntimeException("Invalid [" + category.toString() + "] category");
//    }


//    public ClassSQLData(Class<?> aClass) {
//
//        List<Method> setterMethods = new ArrayList<>();
//        List<Method> getterMethods = new ArrayList<>();
//

//        Field[] fields = aClass.getDeclaredFields();
//        for (var f: fields
//             ) {
//            System.out.println(f.getName());
//            if (f.trySetAccessible()){
//                System.out.println("accessible");
//            }
//        }

//        List<String> getterFields = getterMethods.stream().
//                map(m -> {
//                    if (m.getName().startsWith("is"))
//                        return Strings.toFirstLower(m.getName().substring(2));
//                    else
//                        return (Strings.toFirstLower(m.getName().substring(3)));
//                }).collect(Collectors.toList());
//
//        List<String> setterFields = setterMethods.stream().
//                map(m -> Strings.toFirstLower(m.getName().substring(3))).
//                collect(Collectors.toList());
//
//        for (int n = 0; n < getterFields.size(); n++) {
//            String name = getterFields.get(n);
//            for (int k = 0; k < setterFields.size(); k++) {
//
//                if (name.equals(setterFields.get(k))){
//                    Class<?> type = getterMethods.get(n).getReturnType();
//                    ClassProperty p = new ClassProperty(name,type, getterMethods.get(n), setterMethods.get(k));
//                    this.add(p);
//                    break;
//                }
//            }
//        }
//
//        this.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
//    }
//
//    public ClassProperty getProperty(String fieldName) {
//        for (ClassProperty p:this) {
//            if (p.getName().matches(fieldName)){
//                return p;
//            }
//        }
//        return null;
//    }
