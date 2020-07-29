package jsonparser.tree;

public class JsonAtom<T> extends JsonElement {

    private final T value;

    public JsonAtom(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}