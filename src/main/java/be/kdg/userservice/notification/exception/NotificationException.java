package be.kdg.userservice.notification.exception;

/**
 * Custom exception with class name integration.
 * Classname is used in GetMessage() to get the source of the exception as well as the message.
 */
public class NotificationException extends Exception {
    private final String source;

    public NotificationException(Class source, String message) {
        super(message);
        this.source = source.getSimpleName();
    }

    @Override
    public String getMessage() {
        return source + ": " + super.getMessage();
    }
}
