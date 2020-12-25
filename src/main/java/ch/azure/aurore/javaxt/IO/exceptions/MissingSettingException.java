package ch.azure.aurore.javaxt.IO.exceptions;

public class MissingSettingException extends RuntimeException {

    public MissingSettingException() {
        super();
    }

    public MissingSettingException(String message) {
        super(message);
    }
}
