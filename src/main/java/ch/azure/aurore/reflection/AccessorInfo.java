package ch.azure.aurore.reflection;

public class AccessorInfo {
    private boolean isGetter;
    private String backingField;

    public AccessorInfo(boolean isGetter, String fieldName) {
        this.isGetter = isGetter;
        this.backingField = fieldName;
    }

    public boolean isGetter() {
        return isGetter;
    }

    public String getBackingField() {
        return backingField;
    }
}
