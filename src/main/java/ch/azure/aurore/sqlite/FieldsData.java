package ch.azure.aurore.sqlite;

import ch.azure.aurore.reflection.ClassInfo;
import ch.azure.aurore.reflection.FieldInfo;
import ch.azure.aurore.reflection.MethodInfo;
import ch.azure.aurore.reflection.Reflection;
import ch.azure.aurore.sqlite.wrapper.annotations.DBPack;
import ch.azure.aurore.sqlite.wrapper.annotations.DBUnpack;
import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseClass;
import ch.azure.aurore.strings.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * https://www.baeldung.com/java-custom-annotation
 * https://stackoverflow.com/questions/1942644/get-generic-type-of-java-util-list
 */
public class FieldsData {

    public static final String MODIFIER_FIELD = "modified";
    private static final String ID_FIELD = "id";

    private final String className;
    private final List<FieldData> fields = new ArrayList<>();
    private final MethodInfo unpackMethod;
    private final MethodInfo packMethod;
    private FieldData idField;
    private FieldInfo modifiedField;

    public FieldsData(Class<?> aClass) {
        if (!aClass.isAnnotationPresent(DatabaseClass.class)) {
            throw new IllegalStateException("Class [" + aClass.getSimpleName() + "] must have [DatabaseClass] annotation to be imported into database");
        }
        ClassInfo classInfo = Reflection.getInfo(aClass);

        this.className = FieldsData.getClassDBName(aClass);

        if (!classInfo.hasAccessibleConstructor())
            throw new RuntimeException("Class [" + aClass.getSimpleName() + "] does not contains a public parameterless constructor");

        for (FieldInfo f : classInfo.getFields()) {
            if (!f.hasAccessor() || !f.hasMutator())
                continue;

            if (f.isNamed(ID_FIELD))
                idField = new FieldData(f);
            else if (f.isNamed(MODIFIER_FIELD))
                modifiedField = f;
            else
                fields.add(new FieldData(f));
        }

        if (idField == null)
            throw new IllegalStateException("Class [" + aClass.getSimpleName() + "] does not contains a [id] primary key field with public accessor and mutator.");
        if (modifiedField == null)
            throw new IllegalStateException("Class [" + aClass.getSimpleName() + "] does not contains public [modified] accessor and mutator methods.");

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
        if (idField.getName().equals(columnName))
            return idField;

        return null;
    }

    public int getID(Object data) {
        return (int)idField.getFieldValue(data);
    }

    public boolean isModified(Object data) {
        return (boolean) modifiedField.getAccessor().invoke(data);
    }

    public void pack(Object data) {
        if (packMethod != null)
            packMethod.invoke(data);
    }

    public void setModified(Object data, boolean value) {
        modifiedField.getMutator().invoke(data, value);
    }

    public void setID(Object data, int id) {
        idField.setValue(data, id);
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