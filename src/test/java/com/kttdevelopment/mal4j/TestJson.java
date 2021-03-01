package com.kttdevelopment.mal4j;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.kttdevelopment.mal4j.Json.*;

@SuppressWarnings("SpellCheckingInspection")
public class TestJson {

    @Test
    public void testMap(){
        final String map = String.join("",
            "{",
                "\"double\": 1.0,",
                "\"doublen\": -1.0,",
                "\"doubles\":1.0,",
                "\"int\": 1,",
                "\"intn\": -1,",
                "\"ints\":1,",
                "\"bool\": true,",
                "\"bools\":false,",
                "\"null\":null,",
                "\"nulls\": null,",
                "\"string\": \"string\",",
                "\"strings\":\"string\",",
                "\"str\\\"ingx\":\"str\\\"ing\",",
                "\"slash\": \"\\/\\\\\",",
                "\"\\u4f55\": \"\\u4f55\",",
                "\"obj\": {" +
                    "\"k\": \"v\"" +
                "}," +
                "\"cobj\": {}," +
                "\"arr\": [" +
                    "\"str\"" +
                "]," +
                "\"carr\": []" +
            "}"
        );
        final JsonObject json = (JsonObject) parse(map);

        Assertions.assertEquals(1.0, json.getDouble("double"));
        Assertions.assertEquals(-1.0, json.getDouble("doublen"));
        Assertions.assertEquals(1.0, json.getDouble("doubles"));
        Assertions.assertEquals(1, json.getInt("int"));
        Assertions.assertEquals(-1,  json.getInt("intn"));
        Assertions.assertEquals(1, json.getInt("ints"));
        Assertions.assertTrue(json.getBoolean("bool"));
        Assertions.assertFalse(json.getBoolean("bools"));
        Assertions.assertNull(json.get("null"));
        Assertions.assertTrue(json.containsKey("null"));
        Assertions.assertNull(json.get("nulls"));
        Assertions.assertTrue(json.containsKey("nulls"));
        Assertions.assertEquals("string", json.getString("string"));
        Assertions.assertEquals("string", json.getString("strings"));
        Assertions.assertEquals("str\"ing", json.getString("str\"ingx"));
        Assertions.assertEquals("/\\", json.getString("slash"));
        Assertions.assertEquals("何", json.getString("何"));
        Assertions.assertEquals("v", json.getJsonObject("obj").getString("k"));
        Assertions.assertEquals(0, json.getJsonObject("cobj").size());
        Assertions.assertEquals("str", json.getStringArray("arr")[0]);
        Assertions.assertEquals(0, json.getJsonArray("carr").length);
    }

    @Test
    public void testArray(){
        final String arr = String.join("",
           "[",
                " 1.0,",
                " -1.0,",
                "2.0,",
                " 1,",
                " -1,",
                "2,",
                "true,",
                "false,",
                "null,",
                "\"string\",",
                " \"str\\\"ingx\",",
                "\"\\/\\\\\",",
                "\"\\u4f55\"",
                "{" +
                    "\"k\": \"v\"" +
                "}," +
                "{}," +
                "[" +
                    "\"str\"" +
                "]," +
                "[]" +
            "]"
        );

        // \/\\

        final List<?> json = (List<?>) parse(arr);

        Assertions.assertTrue(json.contains(1.0));
        Assertions.assertTrue(json.contains(-1.0));
        Assertions.assertTrue(json.contains(2.0));
        Assertions.assertTrue(json.contains(1));
        Assertions.assertTrue(json.contains(-1));
        Assertions.assertTrue(json.contains(2));
        Assertions.assertTrue(json.contains(true));
        Assertions.assertTrue(json.contains(false));
        Assertions.assertTrue(json.contains(null));
        Assertions.assertTrue(json.contains("string"));
        Assertions.assertTrue(json.contains("str\"ingx"));
        Assertions.assertTrue(json.contains("/\\"));
        Assertions.assertTrue(json.contains("何"));
        Assertions.assertEquals("v", ((JsonObject) json.get(12)).getString("k"));
        Assertions.assertEquals(0, ((JsonObject) json.get(13)).size());
        Assertions.assertTrue(json.contains(List.of("str")));
        Assertions.assertTrue(json.contains(List.of()));
    }

    @Test
    public void testMalformed(){
        Assertions.assertThrows(JsonSyntaxException.class, () -> parse(""));
        Assertions.assertThrows(JsonSyntaxException.class, () -> parse("?"));
        Assertions.assertThrows(JsonSyntaxException.class, () -> parse("{"));
        Assertions.assertThrows(JsonSyntaxException.class, () -> parse("["));
    }

    @Test
    public void testNewLine(){
        Assertions.assertEquals("v", ((JsonObject) parse("{\"k\":\n\"v\"\n}")).getString("k"));
    }

}
