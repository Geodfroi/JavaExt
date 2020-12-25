package ch.azure.aurore.javaxt.sqlite;

public abstract class PullData {
    protected final FieldData fieldData;

    public PullData(FieldData fieldData) {
        this.fieldData = fieldData;
    }

    public abstract <T> void execute(SQLiteImplementation sq, T data);

    @Override
    public String toString() {
        return "PullData{" +
                "fieldData=" + fieldData +
                '}';
    }
}
