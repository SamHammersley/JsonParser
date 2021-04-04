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

    private JsonToken poll() {
        return Objects.requireNonNull(tokens.poll());
    }

    private JsonToken getCurrent() {
        return Objects.requireNonNull(tokens.peek());
    }

    private JsonObject parseJsonObject() {
        Set<JsonField> fields = new HashSet<>();
        fields.add(parseField());

        while (getCurrent().getType().equals(COMMA)) {
            poll();
            fields.add(parseField());
        }

        expect(RIGHT_CURLY_BRACE).poll();
        return new JsonObject(fields);
    }

    private JsonArray parseJsonArray() {
        List<JsonElement> elements = new ArrayList<>();
        elements.add(parseJson());

        while (getCurrent().getType().equals(COMMA)) {
            poll();
            elements.add(parseJson());
        }

        expect(RIGHT_SQUARE_BRACKET).poll();
        return new JsonArray<>(elements);
    }

    private JsonField parseField() {
        expect(STRING_LITERAL);
        String key = getCurrent().mapValue(s -> s.replaceAll("\"", ""));

        poll();
        expect(COLON).poll();

        return new JsonField(key, parseJson());
    }

    @SuppressWarnings("unchecked")
    public <T extends JsonElement> T parseJson() {
        JsonToken token = poll();

        switch (token.getType()) {
            case NUMBER:
                return (T) new JsonAtom<>(token.mapValue(Integer::parseInt));

            case STRING_LITERAL:
                return (T) new JsonAtom<>(token.getValue());

            case NULL:
                return (T) new JsonNull();

            case BOOLEAN:
                return (T) new JsonAtom<>(token.mapValue(Boolean::parseBoolean));

            case LEFT_SQUARE_BRACKET:
                return (T) parseJsonArray();

            case LEFT_CURLY_BRACE:
                return (T) parseJsonObject();

            default:
                throw new RuntimeException("Unexpected token type for token " + token);
        }
    }

}