package jsonparser.lexical;

import jsonparser.syntax.JsonParser;
import jsonparser.tree.JsonArray;
import jsonparser.tree.JsonObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static jsonparser.lexical.JsonTokenType.*;


import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

final class JsonTest {

    private static final Queue<JsonToken> TEST_TOKENS = new LinkedList<>() {{
        add(new JsonToken(LEFT_SQUARE_BRACKET, "["));
        add(new JsonToken(LEFT_CURLY_BRACE, "{"));
        add(new JsonToken(STRING_LITERAL, "\"id\""));
        add(new JsonToken(COLON, ":"));
        add(new JsonToken(NUMBER, "1"));
        add(new JsonToken(COMMA, ","));
        add(new JsonToken(STRING_LITERAL, "\"name\""));
        add(new JsonToken(COLON, ":"));
        add(new JsonToken(STRING_LITERAL, "\"Jeanette\""));
        add(new JsonToken(RIGHT_CURLY_BRACE, "}"));
        add(new JsonToken(COMMA, ","));
        add(new JsonToken(LEFT_CURLY_BRACE, "{"));
        add(new JsonToken(STRING_LITERAL, "\"id\""));
        add(new JsonToken(COLON, ":"));
        add(new JsonToken(NUMBER, "2"));
        add(new JsonToken(COMMA, ","));
        add(new JsonToken(STRING_LITERAL, "\"name\""));
        add(new JsonToken(COLON, ":"));
        add(new JsonToken(STRING_LITERAL, "\"Giavani\""));
        add(new JsonToken(RIGHT_CURLY_BRACE, "}"));
        add(new JsonToken(RIGHT_SQUARE_BRACKET, "]"));
    }};

    @Nested
    @DisplayName("Json tokenizer tests")
    class JsonTokenizerTests {

        @Test
        void testTokenize() {
            String testData = "[\n" +
                    "  {\n" +
                    "    \"id\": 1,\n" +
                    "    \"name\": \"Jeanette\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 2,\n" +
                    "    \"name\": \"Giavani\"\n" +
                    "  }\n" +
                    "]";

            JsonTokenizer tokenizer = new JsonTokenizer(testData);
            Queue<JsonToken> actual = tokenizer.tokenize();

            assertIterableEquals(TEST_TOKENS, actual);
        }
    }

    @Nested
    @DisplayName("Json parser tests")
    class JsonParserTests {

        @Test
        void testParse() {
            JsonParser parser = new JsonParser(TEST_TOKENS);
            JsonArray<JsonObject> jsonUsers = parser.parseJson();

            Collection<TestUser> users = jsonUsers.map(o -> o.as(TestUser.class));

            TestUser user_1 = new TestUser("\"Jeanette\"", 1);
            TestUser user_2 = new TestUser("\"Giavani\"", 2);

            Iterator<TestUser> iterator = users.iterator();
            assertEquals(user_1, iterator.next());
            assertEquals(user_2, iterator.next());
        }

    }
}