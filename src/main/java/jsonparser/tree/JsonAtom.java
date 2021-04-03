package jsonparser.tree;

public class JsonAtom<T> implements JsonElement<T> {

    private final T value;

    public JsonAtom(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public T getData() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}