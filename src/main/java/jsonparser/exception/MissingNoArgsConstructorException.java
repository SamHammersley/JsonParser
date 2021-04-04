package jsonparser.exception;

public final class MissingNoArgsConstructorException extends RuntimeException {

    public MissingNoArgsConstructorException(Throwable t) {
        super("Requires no args constructor", t);
    }

}