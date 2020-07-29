package jsonparser.tree;

import java.util.Iterator;
import java.util.List;

public class JsonArray extends JsonElement {

    private final List<JsonElement> elements;

    public JsonArray(List<JsonElement> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder("[\n");

        Iterator<JsonElement> iter = elements.iterator();
        while (iter.hasNext()) {
            bldr.append(iter.next());

            if (iter.hasNext()) {
                bldr.append(",");
            }

            bldr.append("\n");
        }

        return bldr.append(']').toString();
    }

}