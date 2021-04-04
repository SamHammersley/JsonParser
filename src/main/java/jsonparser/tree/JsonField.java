package jsonparser.tree;

import java.util.Objects;

public final class JsonField implements JsonElement {

    private final String key;

    private final JsonElement value;

    public JsonField(String key, JsonElement value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + key + "\":" + value.toString();
    }

    @Override
    public Object[] getData() {
        return new Object[] { key, value };
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}