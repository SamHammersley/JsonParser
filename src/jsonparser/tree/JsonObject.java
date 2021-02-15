package jsonparser.tree;

import jsonparser.exception.MissingNoArgsConstructorException;
import jsonparser.exception.NoSuchJsonFieldException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

public class JsonObject implements JsonElement<Set<JsonField>> {

    private final Set<JsonField> fields;

    public JsonObject(Set<JsonField> fields) {
        this.fields = fields;
    }

    public <T extends JsonElement<?>> T getValue(String key) {
        for (JsonField f : fields) {
            if (key.equals(f.getKey())) {
                return (T) f.getValue();
            }
        }

        throw new RuntimeException("No such value with key: " + key);
    }

    public <T> T as(Class<T> t) {
        try {
            Constructor<T> constructor = t.getConstructor();
            T v = constructor.newInstance();

            for (JsonField jsonField : fields) {
                Field field = t.getDeclaredField(jsonField.getKey());
                field.setAccessible(true);

                JsonElement<?> element = jsonField.getValue();

                if (element.getClass().equals(JsonObject.class)) {
                    JsonObject nested = (JsonObject) element;

                    field.set(v, nested.as(field.getType()));

                } else {
                    field.set(v, element.getData());
                }
            }

            return v;

        } catch (NoSuchMethodException e) {
            throw new MissingNoArgsConstructorException(e);

        } catch (NoSuchFieldException e) {
            throw new NoSuchJsonFieldException(e);

        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public Set<JsonField> getData() {
        return fields;
    }

    @Override
    public int hashCode() {
        return fields.hashCode();
    }
}