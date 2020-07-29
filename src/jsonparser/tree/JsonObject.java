package jsonparser.tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonObject extends JsonElement {

    private final List<JsonField> fields;

    private final Map<String, JsonElement> lookup = new HashMap<>();

    public JsonObject(List<JsonField> fields) {
        this.fields = fields;
    }

    public JsonElement getField(String key) {
        if (lookup.isEmpty() && !fields.isEmpty()) {
            fields.forEach(f -> lookup.put(f.getKey(), f.getValue()));
        }

        return lookup.get(key);
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder("{\n");

        Iterator<JsonField> iter = fields.iterator();
        while (iter.hasNext()) {
            bldr.append(iter.next());

            if (iter.hasNext()) {
                bldr.append(",");
            }

            bldr.append("\n");
        }

        return bldr.append("}").toString();
    }

}