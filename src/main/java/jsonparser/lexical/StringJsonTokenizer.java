package jsonparser.lexical;

import jsonparser.exception.UnrecognisedTokenException;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringJsonTokenizer {

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s*");

    private static final Pattern NUMBER_PATTERN = Pattern.compile(JsonTokenType.NUMBER.getPattern());

    private static final Pattern BOOLEAN_PATTERN = Pattern.compile(JsonTokenType.BOOLEAN.getPattern());

    private static final Pattern STRING_LITERAL_PATTERN = Pattern.compile(JsonTokenType.STRING_LITERAL.getPattern());

    private String removeLeadingWhitespace(String s) {
        Matcher whitespaceMatcher = WHITESPACE_PATTERN.matcher(s);

        if (whitespaceMatcher.lookingAt()) {
            return whitespaceMatcher.replaceFirst("");
        }

        return s;
    }

    public Queue<JsonToken> tokenize(String input) {
        Queue<JsonToken> tokens = new LinkedList<>();
        String remaining = removeLeadingWhitespace(input);

        while (!remaining.isBlank()) {
            JsonToken token = nextToken(remaining);
            tokens.add(token);

            int tokenLength = token.getValue().length();
            remaining = removeLeadingWhitespace(remaining.substring(tokenLength));
        }

        return tokens;
    }

    private JsonToken nextToken(String remaining) {
        char head = remaining.charAt(0);

        return oneLengthToken(head).orElseGet(() -> {
            JsonToken token;

            if (remaining.startsWith("null")) {
                token = new JsonToken(JsonTokenType.NULL, "null");

            } else {
                Pattern pattern = isQuotation(head) ? STRING_LITERAL_PATTERN :
                                  isDigit(head)     ? NUMBER_PATTERN :
                                                      BOOLEAN_PATTERN;

                token = variableLengthToken(remaining, pattern);
            }

            if (token == null) {
                // todo add information about where this happened.
                throw new UnrecognisedTokenException(remaining);
            }

            return token;
        });
    }

    private Optional<JsonToken> oneLengthToken(char character) {
        Optional<JsonTokenType> type = JsonTokenType.forPattern(Character.toString(character));

        return type.map(t -> new JsonToken(t, Character.toString(character)));
    }

    private JsonToken variableLengthToken(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);

        JsonTokenType type = JsonTokenType.forPattern(pattern.pattern()).orElseThrow();

        return matcher.lookingAt() ? new JsonToken(type, matcher.group()) : null;
    }

    private boolean isQuotation(char character) {
        return character == '"';
    }

    private boolean isDigit(char character) {
        return Character.isDigit(character);
    }

}