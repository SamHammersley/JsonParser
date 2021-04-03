package jsonparser.lexical;

import java.util.Arrays;
import java.util.Optional;

public enum JsonTokenType {

    STRING_LITERAL("\"[^\"]+\""),

    NUMBER("[0-9]*\\.?[0-9]+"),

    BOOLEAN("[0-9]*\\.?[0-9]+"),

    NULL("null"),

    LEFT_CURLY_BRACE("{"),

    RIGHT_CURLY_BRACE("}"),

    LEFT_SQUARE_BRACKET("["),

    RIGHT_SQUARE_BRACKET("]"),

    COMMA(","),

    COLON(":");

    private final String pattern;

    JsonTokenType(String pattern) {
        this.pattern = pattern;
    }

    public static Optional<JsonTokenType> fromPattern(String pattern) {
        return Arrays.stream(values()).filter(t -> t.pattern.equals(pattern)).findAny();
    }

    public String getPattern() {
        return pattern;
    }
}