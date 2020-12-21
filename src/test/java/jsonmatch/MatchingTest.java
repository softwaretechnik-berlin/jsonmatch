package jsonmatch;

import org.junit.Assert;
import org.junit.Test;

import static jsonmatch.JsonMatch.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatchingTest {

    @Test
    public void simpleObjectMatch() {

        var matcher = object()
                .with("a", eq("x"))
                .with("b", eq("y"))
                .with("z", eq(12))
                .build();

        Result result = matcher.match("{\"a\":\"x\",\"b\":\"y\", \"z\": 12}");

        assertEquals("{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": \u001B[32m\"y\"\u001B[0m,\n" +
            "    \"z\": \u001B[32m12\u001B[0m\n" +
            "}\n", result.visualize());

        assertTrue(result.isMatch());
    }

    @Test
    public void simpleObjectMatchIgnoreExtraData() {

        var matcher = object()
            .with("a", eq("x"))
            .with("b", eq("y"))
            .build();

        Result result = matcher.match("{\"a\":\"x\",\"b\":\"y\", \"z\": 12}");

        assertEquals("{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": \u001B[32m\"y\"\u001B[0m,\n" +
            "    \"z\": \u001B[90m12\u001B[0m\n" +
            "}\n", result.visualize());

        assertTrue(result.isMatch());
    }

    @Test
    public void simpleObjectMismatch() {
       var matcher = object()
            .with("a", eq("x"))
            .with("b", eq("o"))
           .build();

        Result result = matcher.match("{\"a\":\"x\",\"b\":\"y\"}");

        assertFalse(result.isMatch());
        assertEquals(
            "{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": \u001B[31m\"y\"\u001B[0m expected \"o\"\n" +
            "}\n",
            result.visualize()
        );
    }

    @Test
    public void simpleObjectMissingField() {
        var matcher = object()
            .with("a", eq("x"))
            .with("b", eq("o"))
            .build();

        Result result = matcher.match("{\"a\":\"x\"}");

        assertEquals(
            "{\n" +
                "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
                "    \u001B[31m\"b\": \u001B[0mis missing.\n" +
                "}\n",
            result.visualize()
        );

        assertFalse(result.isMatch());
    }

    @Test
    public void simpleArrayMatch() {
        var matcher = array()
            .with(eq("Hello"))
            .with(eq("World"))
            .build();

        Result result = matcher.match("[\"Hello\", \"World\"]");

        assertTrue(result.isMatch());
        assertEquals("[\n" +
            "    \u001B[32m\"Hello\"\u001B[0m,\n" +
            "    \u001B[32m\"World\"\u001B[0m\n" +
            "]\n", result.visualize());
    }

    @Test
    public void simpleArrayMismatch() {
        var matcher = array()
            .with(eq("Hello"))
            .with(eq("World"))
            .build();

        Result result = matcher.match("[\"Hello\", \"Boat\"]");

        assertFalse(result.isMatch());
        assertEquals("[\n" +
            "    \u001B[32m\"Hello\"\u001B[0m,\n" +
            "    \u001B[31m\"Boat\"\u001B[0m expected \"World\"\n" +
            "]\n", result.visualize());
    }


    @Test
    public void nestedObjectMatch() {
        var matcher = object()
            .with("a", eq("x"))
            .with("b", object()
                .with("c", eq("y"))
                .with("d", eq("z"))
                .build()
            )
            .build();

        Result result = matcher.match("{\"a\":\"x\",\"b\":{\"c\": \"y\", \"d\": \"z\"}}");

        assertTrue(result.isMatch());

        assertEquals(
            "{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": {\n" +
            "        \"c\": \u001B[32m\"y\"\u001B[0m,\n" +
            "        \"d\": \u001B[32m\"z\"\u001B[0m\n" +
            "    }\n" +
            "}\n", result.visualize()
        );
    }

    @Test
    public void nestedObjectMismatch() {

        var matcher = object()
            .with("a", eq("x"))
            .with("b", object()
                .with("c", eq("y"))
                .with("d", eq("z"))
                .build()
            )
            .build();

        Result result = matcher.match("{\"a\":\"x\",\"b\":{\"c\": \"y\", \"d\": \"42\"}}");

        assertFalse(result.isMatch());

        assertEquals(
            "{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": {\n" +
            "        \"c\": \u001B[32m\"y\"\u001B[0m,\n" +
            "        \"d\": \u001B[31m\"42\"\u001B[0m expected \"z\"\n" +
            "    }\n" +
            "}\n",
            result.visualize()
        );
    }

     void assertEquals(String expected, String actual) {
         System.out.println(actual);
        Assert.assertEquals(expected, actual);

     }
}
