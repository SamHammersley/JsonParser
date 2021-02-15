package jsonparser.lexical;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonTokenizer {

    private final String input;

    public JsonTokenizer(String input) {
        this.input = input;
    }

    public Queue<JsonToken> tokenize() {
        Queue<JsonToken> tokens = new LinkedList<>();
        String remaining = input;

        while (!remaining.isBlank()) {
            char head = remaining.charAt(0);

            if (Character.isWhitespace(head)) {
                remaining = remaining.substring(1);
                continue;
            }

            final String tempRemaining = remaining;
            Optional<JsonToken> token = tokenizeOneChar(head).or(() -> tokenizeOneOrMoreChar(tempRemaining));

            if (token.isEmpty()) {
                throw new RuntimeException();
            }

            JsonToken t = token.get();
            tokens.add(t);
            remaining = remaining.substring(t.getValue().length());
        }

        return tokens;
    }

    private Optional<JsonToken> tokenizeOneOrMoreChar(String remaining) {
        char head = remaining.charAt(0);
        JsonToken token = null;

        if (head == '"') {
            StringBuilder bldr = new StringBuilder().append(head);

            for (int index = 1; index < remaining.length(); index++) {
                bldr.append(remaining.charAt(index));

                if (remaining.charAt(index) == '"') {
                    break;
                }
            }
            token = new JsonToken(JsonTokenType.STRING_LITERAL, bldr.toString());

        } else if (Character.isDigit(head)) {
            StringBuilder bldr = new StringBuilder().append(head);

            for (int index = 1; index < remaining.length(); index++) {
                if (!Character.isDigit(remaining.charAt(index + 1))) {
                    break;
                }

                bldr.append(remaining.charAt(index));
            }
            token = new JsonToken(JsonTokenType.NUMBER, bldr.toString());

        } else {
            Matcher matcher = Pattern.compile("(tru|fals)e").matcher(remaining);

            if (matcher.lookingAt()) {
                token = new JsonToken(JsonTokenType.BOOLEAN_VALUE, matcher.group());
            }

            matcher.usePattern(Pattern.compile("null"));

            if (matcher.lookingAt()) {
                token = new JsonToken(JsonTokenType.NULL, "null");
            }
        }

        return Optional.ofNullable(token);
    }

    private Optional<JsonToken> tokenizeOneChar(char character) {
        JsonTokenType type;

        switch (character) {
            case '{':
                type = JsonTokenType.LEFT_CURLY_BRACE;
                break;
            case '}':
                type = JsonTokenType.RIGHT_CURLY_BRACE;
                break;
            case '[':
                type = JsonTokenType.LEFT_SQUARE_BRACKET;
                break;
            case ']':
                type = JsonTokenType.RIGHT_SQUARE_BRACKET;
                break;
            case ',':
                type = JsonTokenType.COMMA;
                break;
            case ':':
                type = JsonTokenType.COLON;
                break;
            default:
                return Optional.empty();
        }

        return Optional.of(new JsonToken(type, Character.toString(character)));
    }

}