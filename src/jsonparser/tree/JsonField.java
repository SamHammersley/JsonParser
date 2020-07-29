package jsonparser.tree;

public final class JsonField extends JsonElement {

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

}