package jsonmatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.AssertionFailedError;
import org.buildobjects.doctest.runtime.junit4.DocufierRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jsonmatch.JsonMatch.*;
import static jsonmatch.console.Color.*;
import static jsonmatch.console.ConsoleUtils.ANSI_RESET;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * [DOC file=README.md]
 * <p>
 * `jsonmatch` is a library that helps verifying JSON results
 * in test cases. It provides a DSL to specify expectations,
 * which is much nicer to use in java than providing, e.g.
 * json string literals. It also provides a visualisation of
 * the test result, that provides a view at the actual result,
 * projected through the expectation that has been set up.
 * Currently the visualisation uses ansi coloured output.
 * <p>
 * **Note**: This file is generate from the [acceptance test](src/test/java/jsonmatch/MatchingTest.java), please
 * don't edit the README.md , but rather the test case.
 * <p>
 * Let's look at some examples.
 * <p>
 * Matching Simple Objects
 * -----------------------
 */
public class MatchingTest {

    @Rule
    public DocufierRule doc = new DocufierRule();

    /**
     * The happy path case:
     */
    @Test
    public void simpleObjectMatch() {

        var matcher = object()
            .with("a", eq("x"))
            .with("b", eq("y"))
            .with("z", eq(12))
            .with("d", eq(false))
            .build();

        Result result = matcher.match(doc.tap("{\"a\":\"x\", \"b\":\"y\", \"z\":12, \"d\": false}", this::prettyJson));

        assertEquals(doc.tap("{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": \u001B[32m\"y\"\u001B[0m,\n" +
            "    \"z\": \u001B[32m12\u001B[0m,\n" +
            "    \"d\": \u001B[32mfalse\u001B[0m\n" +
            "}\n", this::prettyAnsi), result.visualize());

        assertTrue(result.isMatch());
    }

    /**
     * By default the object matcher ignores extra fields.
     * However it displays the extra information in gray:
     */
    @Test
    public void simpleObjectMatchIgnoreExtraData() {
        var matcher = object()
            .with("a", eq("x"))
            .with("b", eq("y"))
            .build();

        Result result = matcher.match(doc.tap("{\"a\":\"x\",\"b\":\"y\", \"z\": 12}", this::prettyJson));

        assertEquals(doc.tap("{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": \u001B[32m\"y\"\u001B[0m,\n" +
            "    \u001B[90m\"z\": \u001B[0m\u001B[90m12\u001B[0m\n" +
            "}" +
            "\n", this::prettyAnsi), result.visualize());

        assertTrue(result.isMatch());
    }

    /**
     * If the matcher is configured not to ignore extra fields
     * it will fail as follows:
     */
    @Test
    public void simpleObjectMatchFailOnExtraData() {

        var matcher = object()
            .ignoreExtraFields(false)
            .with("a", eq("x"))
            .with("b", eq("y"))
            .build();

        Result result = matcher.match(doc.tap("{\"a\":\"x\",\"b\":\"y\", \"z\": 12}", this::prettyJson));

        assertEquals(doc.tap("{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": \u001B[32m\"y\"\u001B[0m,\n" +
            "    \u001B[31m\"z\": \u001B[0m\u001B[31m12\u001B[0m unexpected field\n" +
            "}\n", this::prettyAnsi), result.visualize());

        assertFalse(result.isMatch());
    }

    /**
     * Obviously field values need to match the expectation:
     */
    @Test
    public void simpleObjectMismatch() {
        var matcher = object()
            .with("a", eq("x"))
            .with("b", eq("o"))
            .build();

        Result result = matcher.match(doc.tap("{\"a\":\"x\",\"b\":\"y\"}", this::prettyJson));

        assertFalse(result.isMatch());
        assertEquals(doc.tap(
            "{\n" +
                "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
                "    \"b\": \u001B[31m\"y\"\u001B[0m expected \"o\"\n" +
                "}\n", this::prettyAnsi),
            result.visualize()
        );
    }

    /**
     * Missing fields will also fail to match:
     */
    @Test
    public void simpleObjectMissingField() {
        var matcher = object()
            .with("a", eq("x"))
            .with("b", eq("o"))
            .build();

        Result result = matcher.match(doc.tap("{\"a\":\"x\"}", this::prettyJson));

        assertEquals(doc.tap(
            "{\n" +
                "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
                "    \u001B[31m\"b\": \u001B[0mis missing\n" +
                "}\n", this::prettyAnsi),
            result.visualize()
        );

        assertFalse(result.isMatch());
    }

    /**
     * Matching Arrays
     * ---------------
     * <p>
     * We can also match elements of an array.
     * Currently only an exact match is implemented, however
     * more match modes, like all `ignoreExtraElements`,
     * `ignoreOrder` can easily be added. Here a basic
     * example:
     */
    @Test
    public void simpleArrayMatch() {
        var matcher = array()
            .with(eq("Hello"))
            .with(eq("World"))
            .build();

        Result result = matcher.match(doc.tap("[\"Hello\", \"World\"]", this::prettyJson));

        assertTrue(result.isMatch());
        assertEquals(doc.tap("[\n" +
            "    \u001B[32m\"Hello\"\u001B[0m,\n" +
            "    \u001B[32m\"World\"\u001B[0m\n" +
            "]\n", this::prettyAnsi), result.visualize());
    }

    /**
     * This is what a failed match on an array element looks like:
     */
    @Test
    public void simpleArrayMismatch() {
        var matcher = array()
            .with(eq("Hello"))
            .with(eq("World"))
            .build();

        Result result = matcher.match(doc.tap("[\"Hello\", \"Boat\"]", this::prettyJson));

        assertFalse(result.isMatch());
        assertEquals(doc.tap("[\n" +
            "    \u001B[32m\"Hello\"\u001B[0m,\n" +
            "    \u001B[31m\"Boat\"\u001B[0m expected \"World\"\n" +
            "]\n", this::prettyAnsi), result.visualize());
    }


    /**
     * Nested Objects
     * --------------
     * <p>
     * We can also match on nested structures. Actually
     * matchers support arbitrary nesting:
     */
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

        Result result = matcher.match(doc.tap("{\"a\":\"x\",\"b\":{\"c\": \"y\", \"d\": \"z\"}}", this::prettyJson));

        assertTrue(result.isMatch());

        assertEquals(doc.tap(
            "{\n" +
                "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
                "    \"b\": {\n" +
                "        \"c\": \u001B[32m\"y\"\u001B[0m,\n" +
                "        \"d\": \u001B[32m\"z\"\u001B[0m\n" +
                "    }\n" +
                "}\n", this::prettyAnsi), result.visualize()
        );
    }

    /**
     * This is what a mismatch on a nested object looks like:
     */
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

        Result result = matcher.match(doc.tap("{\"a\":\"x\",\"b\":{\"c\": \"y\", \"d\": \"42\"}}", this::prettyJson));

        assertFalse(result.isMatch());

        assertEquals(doc.tap(
            "{\n" +
                "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
                "    \"b\": {\n" +
                "        \"c\": \u001B[32m\"y\"\u001B[0m,\n" +
                "        \"d\": \u001B[31m\"42\"\u001B[0m expected \"z\"\n" +
                "    }\n" +
                "}\n", this::prettyAnsi),
            result.visualize()
        );
    }

    @Test
    public void useInAssertion() {

        assertMatches("{\"a\":\"x\"}", object()
            .with("a", eq("x"))
            .with("b", object()
                .with("c", eq("y"))
                .with("d", eq("z"))
            )
        );
    }

    private void assertMatches(String json, MatcherBuilder matcherBuilder) {
        assertMatches(json, matcherBuilder.build());
    }

    private void assertMatches(String json, Matcher matcher) {
        Result result = matcher.match(json);
        if (result.isMatch()) {
            return;
        }
        throw new AssertionFailedError("Mismatch:\n" + result.visualize());
    }


    /**
     * [NO-DOC]
     */
    void assertEquals(String expected, String actual) {
        System.out.println(actual);
        Assert.assertEquals(expected, actual);

    }

    /**
     * [NO-DOC]
     */
    private String prettyJson(String t) {
        try {
            return new ObjectMapper()
                .readTree(new ByteArrayInputStream(t.getBytes(UTF_8)))
                .toPrettyString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * [NO-DOC]
     */
    private String prettyAnsi(String t) {
        return
            t
                .replace(RED.getAnsi(), "<span style=\"color:red\">")
                .replace(GREEN.getAnsi(), "<span style=\"color:green\">")
                .replace(ANSI_RESET, "</span>")
                .replace(GRAY.getAnsi(), "<span style=\"color:gray\">")
            ;
    }

}
