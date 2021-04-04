package jsonparser.lexical;

import java.util.function.Function;

public final class JsonToken {

    private final JsonTokenType type;

    private final String value;

    public JsonToken(JsonTokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public JsonTokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public <T> T mapValue(Function<String, T> mapper) {
        return mapper.apply(value);
    }

    @Override
    public String toString() {
        return type.name() + ", " + value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof JsonToken)) {
            return false;
        }

        JsonToken other = (JsonToken) object;
        return type.equals(other.type) && value.equals(other.value);
    }
}