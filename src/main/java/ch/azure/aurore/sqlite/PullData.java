package ch.azure.aurore.sqlite;

public abstract class PullData {
    protected final FieldData fieldData;

    public PullData(FieldData fieldData) {
        this.fieldData = fieldData;
    }

    public abstract <T> void execute(SQLiteImplementation sq, T data);
}
