package jsonparser.lexical;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

public enum JsonTokenType {

    STRING_LITERAL(Pattern.compile("\"[^\"]*\"")),

    NUMBER(Pattern.compile("[0-9]*\\.?[0-9]+")),

    BOOLEAN(Pattern.compile("(tru|fals)e")),

    NULL(Pattern.compile("null")),

    LEFT_CURLY_BRACE(Pattern.compile("\\{")),

    RIGHT_CURLY_BRACE(Pattern.compile("}")),

    LEFT_SQUARE_BRACKET(Pattern.compile("\\[")),

    RIGHT_SQUARE_BRACKET(Pattern.compile("]")),

    COMMA(Pattern.compile(",")),

    COLON(Pattern.compile(":"));

    private final Pattern pattern;

    JsonTokenType(Pattern pattern) {
        this.pattern = pattern;
    }

    public static Optional<JsonTokenType> forPattern(String pattern) {
        return Arrays.stream(values()).filter(t -> t.pattern.pattern().equals(pattern)).findAny();
    }

    public static Optional<JsonTokenType> match(String value) {
        return Arrays.stream(values()).filter(t -> t.pattern.asMatchPredicate().test(value)).findAny();
    }

    public String getPattern() {
        return pattern.pattern();
    }
}