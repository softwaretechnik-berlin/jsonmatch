package jsonmatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.AssertionFailedError;
import org.buildobjects.doctest.runtime.junit4.DocufierRule;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jsonmatch.JsonMatch.*;
import static jsonmatch.util.Color.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * [DOC file=README.md]
 *
 * [![Build Status](https://travis-ci.com/softwaretechnik-berlin/jsonmatch.svg?branch=main)](https://travis-ci.com/softwaretechnik-berlin/jsonmatch)
 * [![Maven Central](https://img.shields.io/maven-central/v/berlin.softwaretechnik/jsonmatch.svg?maxAge=3600)](https://mvnrepository.com/artifact/berlin.softwaretechnik/jsonmatch)
 *
 * `jsonmatch` is a library that helps with verifying JSON results
 * in test cases. It provides a DSL to specify expectations,
 * which is much nicer to use in Java than providing, e.g.
 * JSON string literals. It also provides a visualisation of
 * the test result that provides a view of the actual result,
 * projected through the expectation that has been set up.
 * Currently the visualisation uses ANSI coloured output.
 *
 * How to start with Maven:
 *
 * ~~~.xml
 * <dependency>
 *     <groupId>berlin.softwaretechnik</groupId>
 *     <artifactId>jsonmatch</artifactId>
 *     <version>0.0.4</version>
 *     <scope>test</scope> <!-- If you only want to use jsonmatch in your tests. -->
 * </dependency>
 * ~~~
 * **Note**: This file is generated from the [acceptance test](src/test/java/jsonmatch/MatchingTest.java). To make
 * changes please edit the acceptance test.
 *
 *
 */
public class MatchingTest {

    @Rule
    public final DocufierRule doc = new DocufierRule();


    /**
     * This is how `jsonmatch` can be used in a test:
     */
    @Test
    @Ignore
    public void useInAssertion() {
        String result = "{\"a\":\"x\",\"b\":42,\"c\":\"X\",\"extra\":\"value\"}";

        assertMatches(result, object()
            .with("a", eq("x"))
            .with("b", eq(42))
            .with("c", eq("y"))
            .with("d", eq("z"))
        );
    }

    /**
     * If run in the IDE, the test failure will look like this:
     *
     * ![](assertion-failure-intellij.png)
     *
     * The `assertMatches` method is actually not part of `jsonmatch` (yet).
     * A possible implementation for JUnit4 could look like this:
     */
    private void assertMatches(String json, Matcher matcher) {
        Result result = matcher.match(json);
        if (result.isMatch()) {
            return;
        }
        throw new AssertionFailedError("\nJson didn't match expectation:\n" + result.visualize());
    }

    /**
     * [NO-DOC]
     */
    private void assertMatches(String json, MatcherBuilder matcherBuilder) {
        assertMatches(json, matcherBuilder.build());
    }

    /**
     * `jsonmatch` has a couple of static factory methods that serve as the
     * main interface, that can be imported as follows:
     *
     * ~~~.java
     * import jsonmatch.JsonMatch.*
     * ~~~
     *
     * The main concepts in jsonmatch are `Matcher`s that can try to match
     * `JsonNode`s or `String`s and `Result`s that know whether they
     * represent a successful match and that can visualize the match (attempt).
     *
     * More Examples
     * -------------
     * Let's look at some more cases. We are using a special
     * rendering of Java code, that allows us to have inline json literals
     * and also we have an inline rendering of ANSI-coloured Strings. To take
     * advantage of the full colouring make sure you look at this file
     * in [github pages](https://softwaretechnik-berlin.github.io/jsonmatch/).
     *
     * Matching Simple Objects
     * -----------------------
     *
     * The happy path case:
     */
    @Test
    public void simpleObjectMatch() {

        Matcher matcher = object()
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
        Matcher matcher = object()
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
     * We can also choose to elide the values of ignored fields:
     */
    @Test
    public void simpleObjectMatchElideIgnoredFields() {
        Matcher matcher = object()
            .elideIgnoredFieldValues(true)
            .with("a", eq("x"))
            .with("b", eq("y"))
            .build();

        Result result = matcher.match(doc.tap("{\"a\":\"x\",\"b\":\"y\", \"z\": 12}", this::prettyJson));

        assertEquals(doc.tap("{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": \u001B[32m\"y\"\u001B[0m,\n" +
            "    \u001B[90m\"z\": \u001B[0m\u001B[90m…\u001B[0m\n" +
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
        Matcher matcher = object()
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
        Matcher matcher = object()
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
        Matcher matcher = object()
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
     * Sometimes we want to give context on a particular expection:
     */
    @Test
    public void simpleObjectAnnotation() {
        Matcher matcher = object()
            .with("a", eq("x"))
            .with("b", annotate(eq(42), "This is the answer, obviously."))
            .build();

        Result result = matcher.match(doc.tap("{\"a\":\"x\",\"b\":42}", this::prettyJson));

        assertEquals(doc.tap("{\n" +
                "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
                "    \"b\": \u001B[32m42\u001B[0m ╶ This is the answer, obviously.\n" +
                "}\n", this::prettyAnsi),
            result.visualize()
        );

        assertTrue(result.isMatch());
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
        Matcher matcher = array()
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
        Matcher matcher = array()
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
        Matcher matcher = object()
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
     * Annotations makes sense especially for bigger longer results.
     * Here an example where a nested object is annotated.
     */
    @Test
    public void nestedObjectAnnotated() {
        Matcher matcher = object()
            .with("a", eq("x"))
            .with("b", annotate(
                object()
                    .with("c", eq("y"))
                    .with("d", eq("z"))
                    .build(),
                "This is a nested structure."
            ))
            .build();

        Result result = matcher.match(doc.tap("{\"a\":\"x\",\"b\":{\"c\": \"y\", \"d\": \"z\"}}", this::prettyJson));

        assertTrue(result.isMatch());

        assertEquals(doc.tap("{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": {        ╮\n" +
            "        \"c\": \u001B[32m\"y\"\u001B[0m, ├ This is a nested structure.\n" +
            "        \"d\": \u001B[32m\"z\"\u001B[0m  │\n" +
            "    }             ╯\n" +
            "}\n", this::prettyAnsi), result.visualize()
        );
    }

    /**
     * This is what a mismatch on a nested object looks like:
     */
    @Test
    public void nestedObjectMismatch() {
        Matcher matcher = object()
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

    /** [NO-DOC] */
    @Test
    public void dealsWithNull(){
            Matcher matcher = object()
                .with("a", eq("x"))
                .with("b", eq("y"))
                .build();

            Result result = matcher.match(doc.tap("{\"a\":\"x\",\"b\":null, \"z\": null}", this::prettyJson));

            assertEquals(doc.tap("{\n" +
                "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
                "    \"b\": \u001B[31mexpected <String>\u001B[0m but got <Null>,\n" +
                "    \u001B[90m\"z\": \u001B[0m\u001B[90mnull\u001B[0m\n" +
                "}\n", this::prettyAnsi), result.visualize());

            assertFalse(result.isMatch());

    }

    /** We can also match `null`: */
    @Test
    public void expectNull(){
        Matcher matcher = object()
            .with("a", eq("x"))
            .with("b", isNull())
            .build();

        Result result = matcher.match(doc.tap("{\"a\":\"x\",\"b\":null, \"z\": null}", this::prettyJson));

        assertEquals(doc.tap("{\n" +
            "    \"a\": \u001B[32m\"x\"\u001B[0m,\n" +
            "    \"b\": \u001B[32mnull\u001B[0m,\n" +
            "    \u001B[90m\"z\": \u001B[0m\u001B[90mnull\u001B[0m\n" +
            "}\n", this::prettyAnsi), result.visualize());

        assertTrue(result.isMatch());

    }



}
