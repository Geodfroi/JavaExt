package ch.azure.aurore.reflection;

public class MutatorInfo {
    private final boolean isSetter;
    private final String backingField;

    public MutatorInfo(boolean result, String s) {
        this.isSetter = result;
        this.backingField =s;
    }

    public boolean isSetter() {
        return isSetter;
    }

    public String getBackingField() {
        return backingField;
    }
}
