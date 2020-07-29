package jsonparser;

import java.util.Queue;

public class TestEntryPoint {

    public static void main(String[] args) {
        JsonTokenizer tokenizer = new JsonTokenizer("{\n" +
                "   \"book\": [\n" +
                "\n" +
                "      {\n" +
                "         \"id\": 01,\n" +
                "         \"language\": \"Java\",\n" +
                "         \"edition\": \"third\",\n" +
                "         \"author\": \"Herbert Schildt\",\n" +
                "         \"gay\": true\n" +
                "      },\n" +
                "\n" +
                "      {\n" +
                "         \"id\": 07,\n" +
                "         \"language\": \"C++\",\n" +
                "         \"edition\": \"second\",\n" +
                "         \"author\": \"E.Balagurusamy\",\n" +
                "         \"gay\": false\n" +
                "      }\n" +
                "\n" +
                "   ]\n" +
                "}");

        Queue<JsonToken> tokens = tokenizer.tokenize();
        //tokens.forEach(System.out::println);

        JsonParser parser = new JsonParser(tokens);
        System.out.println(parser.parseJsonObject());
    }

}