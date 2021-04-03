package jsonparser.lexical;

import jsonparser.exception.UnrecognisedTokenException;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonTokenizer {

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s*");

    private static final Pattern NUMBER_PATTERN = Pattern.compile(JsonTokenType.NUMBER.getPattern());

    private static final Pattern BOOLEAN_PATTERN = Pattern.compile(JsonTokenType.BOOLEAN.getPattern());

    private static final Pattern STRING_LITERAL_PATTERN = Pattern.compile(JsonTokenType.STRING_LITERAL.getPattern());

    private final String input;

    public JsonTokenizer(String input) {
        this.input = input;
    }

    private String removeLeadingWhitespace(String s) {
        Matcher whitespaceMatcher = WHITESPACE_PATTERN.matcher(s);

        if (whitespaceMatcher.lookingAt()) {
            return whitespaceMatcher.replaceFirst("");
        }

        return s;
    }

    public Queue<JsonToken> tokenize() {
        Queue<JsonToken> tokens = new LinkedList<>();
        String remaining = removeLeadingWhitespace(input);

        while (!remaining.isBlank()) {
            JsonToken token = tokenize(remaining);
            tokens.add(token);

            int tokenLength = token.getValue().length();
            remaining = removeLeadingWhitespace(remaining.substring(tokenLength));
        }

        return tokens;
    }

    private JsonToken tokenize(String remaining) {
        char head = remaining.charAt(0);

        return tokenizeOneChar(head).orElseGet(() -> {
            JsonToken token;

            if (remaining.startsWith("null")) {
                token = new JsonToken(JsonTokenType.NULL, "null");

            } else {
                Pattern pattern = isQuotation(head) ? STRING_LITERAL_PATTERN :
                        isDigit(head) ? NUMBER_PATTERN : BOOLEAN_PATTERN;

                token = tokenize(remaining, pattern);
            }

            if (token == null) {
                // todo add information about where this happened.
                throw new UnrecognisedTokenException(remaining);
            }

            return token;
        });
    }

    private Optional<JsonToken> tokenizeOneChar(char character) {
        Optional<JsonTokenType> type = JsonTokenType.fromPattern(Character.toString(character));

        return type.map(t -> new JsonToken(t, Character.toString(character)));
    }

    private JsonToken tokenize(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);

        JsonTokenType type = JsonTokenType.fromPattern(pattern.pattern()).orElseThrow();

        return matcher.lookingAt() ? new JsonToken(type, matcher.group()) : null;
    }

    private boolean isQuotation(char character) {
        return character == '"';
    }

    private boolean isDigit(char character) {
        return Character.isDigit(character);
    }

}