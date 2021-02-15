package jsonparser.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonArray<T extends JsonElement> implements JsonElement<Collection<T>>, Iterable<T> {

    private final Collection<T> elements;

    public JsonArray(Collection<T> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder("[\n");

        Iterator<T> iter = elements.iterator();
        while (iter.hasNext()) {
            bldr.append(iter.next());

            if (iter.hasNext()) {
                bldr.append(",");
            }

            bldr.append("\n");
        }

        return bldr.append(']').toString();
    }

    public <E> Collection<E> map(Function<T, E> mappingFunction) {
        return elements.stream().map(mappingFunction).collect(Collectors.toList());
    }

    @Override
    public Collection<T> getData() {
        return elements;
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }
}