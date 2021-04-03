package jsonparser.exception;

public class UnrecognisedTokenException extends RuntimeException {

    public UnrecognisedTokenException(String unrecognisedToken) {
        super("No recognised pattern for: \n" + unrecognisedToken);
    }
}