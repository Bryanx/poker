package be.kdg.userservice.user.exception;

public class UserException extends RuntimeException {
    private final String source;

    public UserException(Class source, String message) {
        super(message);
        this.source = source.getSimpleName();
    }

    @Override
    public String getMessage() {
        return source + ": " + super.getMessage();
    }
}
