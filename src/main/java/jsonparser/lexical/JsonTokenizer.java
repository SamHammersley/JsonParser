package jsonparser.lexical;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class JsonTokenizer {

    private static final Pattern WHITE_SPACE_PATTERN = Pattern.compile("\\s");

    private static final Predicate<Character> IS_QUOTE = character -> character == '"';

    public Queue<JsonToken> tokenize(InputStream input) {
        if (!input.markSupported()) {
            throw new RuntimeException("InputStream must supported marking!");
        }

        try (input) {
            Queue<JsonToken> result = new LinkedList<>();

            while (input.available() > 0) {
                char nextChar = readAsChar(input);

                if (WHITE_SPACE_PATTERN.asMatchPredicate().test(Character.toString(nextChar))) {
                    continue;
                }

                result.add(nextToken(nextChar, input).orElseThrow());
            }

            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<JsonToken> nextToken(char nextChar, InputStream input) throws IOException {
        Optional<JsonToken> nextOneLengthToken = oneLengthToken(nextChar);

        if (nextOneLengthToken.isPresent()) {
            return nextOneLengthToken;
        }

        return variableLengthToken(nextChar, input);
    }

    private Optional<JsonToken> oneLengthToken(char character) {
        final String charAsString = Character.toString(character);

        return JsonTokenType.match(charAsString).map(type -> new JsonToken(type, charAsString));
    }

    private Optional<JsonToken> variableLengthToken(char firstChar, InputStream input) throws IOException {
        Optional<JsonTokenType> type = Optional.empty();
        String value = null;

        if (firstChar == '"') {
            value = firstChar + readWhile(IS_QUOTE.negate(), input) + readAsChar(input);
            type = Optional.of(JsonTokenType.STRING_LITERAL);

        } else if (Character.isDigit(firstChar) || firstChar == '.') {
            value = firstChar + readWhile(Character::isDigit, input);
            type = Optional.of(JsonTokenType.NUMBER);

        } else if (Character.isAlphabetic(firstChar)) {
            value = firstChar + readWhile(Character::isAlphabetic, input);
            type = JsonTokenType.match(value);
        }

        final String returnValue = value;
        return type.map(t -> new JsonToken(t, returnValue));
    }

    private String readWhile(Predicate<Character> predicate, InputStream input) throws IOException {
        StringBuilder bldr = new StringBuilder();

        while (input.available() > 0) {
            input.mark(1);
            char character = readAsChar(input);

            if (!predicate.test(character)) {
                input.reset();
                break;
            }

            bldr.append(character);
        }

        return bldr.toString();
    }

    private char readAsChar(InputStream input) throws IOException {
        return (char) input.read();
    }

}