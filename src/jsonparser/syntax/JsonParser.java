package jsonparser.syntax;

import jsonparser.lexical.JsonToken;
import jsonparser.lexical.JsonTokenType;
import jsonparser.tree.*;

import java.util.*;

import static jsonparser.lexical.JsonTokenType.*;

public final class JsonParser {

    private final Queue<JsonToken> tokens;

    public JsonParser(Queue<JsonToken> tokens) {
        this.tokens = tokens;
    }

    private JsonParser expect(JsonTokenType... expectedTypes) {
        JsonToken token = getCurrent();

        boolean typeFound = false;

        for (JsonTokenType expectedType : expectedTypes) {
            typeFound = token.getType().equals(expectedType);
        }

        if (!typeFound) {
            throw new RuntimeException("Expected one of " + Arrays.toString(expectedTypes) + " and had " + token.getType());
        }

        return this;
    }

    private JsonParser consume() {
        tokens.poll();

        return this;
    }

    private JsonToken getCurrent() {
        return Objects.requireNonNull(tokens.peek());
    }

    private JsonObject parseJsonObject() {
        expect(LEFT_CURLY_BRACE).consume();

        Set<JsonField> fields = new HashSet<>();
        fields.add(parseField());

        while (getCurrent().getType().equals(COMMA)) {
            consume();
            fields.add(parseField());
        }

        expect(RIGHT_CURLY_BRACE);
        return new JsonObject(fields);
    }

    private JsonArray<?> parseJsonArray() {
        consume(); // consume left bracket.

        List<JsonElement> elements = new ArrayList<>();
        elements.add(parseValue());

        while (getCurrent().getType().equals(COMMA)) {
            consume();
            elements.add(parseValue());
        }

        expect(RIGHT_SQUARE_BRACKET);
        return new JsonArray<>(elements);
    }

    private JsonField parseField() {
        expect(STRING_LITERAL);
        String key = getCurrent().mapValue(s -> s.replaceAll("\"", ""));

        consume();
        expect(COLON).consume();

        return new JsonField(key, parseValue());
    }

    public <T extends JsonElement<?>> T parseValue() {
        JsonToken token = getCurrent();
        JsonElement<?> element;

        switch(token.getType()) {
            case NUMBER:
                element = new JsonAtom<>(token.mapValue(Integer::parseInt));
                break;

            case STRING_LITERAL:
                element = new JsonAtom<>(token.getValue());
                break;

            case NULL:
                element = new JsonNull();
                break;

            case BOOLEAN_VALUE:
                element = new JsonAtom<>(token.mapValue(Boolean::parseBoolean));
                break;

            case LEFT_SQUARE_BRACKET:
                element = parseJsonArray();
                break;

            case LEFT_CURLY_BRACE:
                element = parseJsonObject();
                break;

            default:
                throw new RuntimeException();
        }

        consume();
        return (T) element;
    }

}