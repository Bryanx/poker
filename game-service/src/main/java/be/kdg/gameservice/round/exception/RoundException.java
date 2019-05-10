package be.kdg.gameservice.round.exception;

/**
 * Custom exception with class name integration.
 * Classname is used in GetMessage() to get the source of the exception as well as the message.
 */
public class RoundException extends Exception {
    private final String source;

    public RoundException(Class source, String message) {
        super(message);
        this.source = source.getSimpleName();
    }

    @Override
    public String getMessage() {
        return source + ": " + super.getMessage();
    }
}
