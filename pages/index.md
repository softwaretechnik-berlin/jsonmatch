[![Build Status](https://travis-ci.com/softwaretechnik-berlin/jsonmatch.svg?branch=main)](https://travis-ci.com/softwaretechnik-berlin/jsonmatch)
[![Maven Central](https://img.shields.io/maven-central/v/berlin.softwaretechnik/jsonmatch.svg?maxAge=3600)](https://mvnrepository.com/artifact/berlin.softwaretechnik/jsonmatch)

`jsonmatch` is a library that helps with verifying JSON results
in test cases. It provides a DSL to specify expectations,
which is much nicer to use in Java than providing, e.g.
JSON string literals. It also provides a visualisation of
the test result that provides a view of the actual result,
projected through the expectation that has been set up.
Currently the visualisation uses ANSI coloured output.

How to start with Maven:

~~~.xml
<dependency>
    <groupId>berlin.softwaretechnik</groupId>
    <artifactId>jsonmatch</artifactId>
    <version>0.0.6</version>
    <scope>test</scope> <!-- If you only want to use jsonmatch in your tests. -->
</dependency>
~~~
**Note**: This file is generated from the [acceptance test](https://github.com/softwaretechnik-berlin/jsonmatch/blob/main/src/test/java/jsonmatch/MatchingTest.java). To make
changes please edit the acceptance test.

This is how `jsonmatch` can be used in a test:

<pre><code>String result = <span style="color:green">"{\"a\":\"x\",\"b\":42,\"c\":\"X\",\"extra\":\"value\"}"</span>;

assertMatches(result, object()
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, eq(<span style="color:blue">42</span>))
    .with(<span style="color:green">"c"</span>, eq(<span style="color:green">"y"</span>))
    .with(<span style="color:green">"d"</span>, eq(<span style="color:green">"z"</span>))
);</code></pre>

If run in the IDE, the test failure will look like this:

![](assertion-failure-intellij.png)

The `assertMatches` method is actually not part of `jsonmatch` (yet).
A possible implementation for JUnit4 could look like this:

<pre><code>void assertMatches(String json, Matcher matcher) {
    Result result = matcher.match(json);
    if (result.isMatch()) {
        return;
    }
    throw new AssertionFailedError("\nJson didn't match expectation:\n" + result.visualize());
}</code></pre>

`jsonmatch` has a couple of static factory methods that serve as the
main interface, that can be imported as follows:

~~~.java
import jsonmatch.JsonMatch.*
~~~

The main concepts in jsonmatch are `Matcher`s that can try to match
`JsonNode`s or `String`s and `Result`s that know whether they
represent a successful match and that can visualize the match (attempt).

More Examples
-------------
Let's look at some more cases. We are using a special
rendering of Java code, that allows us to have inline json literals
and also we have an inline rendering of ANSI-coloured Strings. To take
advantage of the full colouring make sure you look at this file
in [github pages](https://softwaretechnik-berlin.github.io/jsonmatch/).

Matching Simple Objects
-----------------------

The happy path case:

<pre><code>Matcher matcher = object()
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, eq(<span style="color:green">"y"</span>))
    .with(<span style="color:green">"z"</span>, eq(<span style="color:blue">12</span>))
    .with(<span style="color:green">"d"</span>, eq(<span style="color:blue">false</span>))
    .build();

Result result = matcher.match(«{
  "a" : "x",
  "b" : "y",
  "z" : 12,
  "d" : false
}»);

assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    "b": <span style="color:green">"y"</span>,
    "z": <span style="color:green">12</span>,
    "d": <span style="color:green">false</span>
}
», result.visualize());

assertTrue(result.isMatch());</code></pre>

By default the object matcher ignores extra fields.
However it displays the extra information in gray:

<pre><code>Matcher matcher = object()
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, eq(<span style="color:green">"y"</span>))
    .build();

Result result = matcher.match(«{
  "a" : "x",
  "b" : "y",
  "z" : 12
}»);

assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    "b": <span style="color:green">"y"</span>,
    <span style="color:gray">"z": </span><span style="color:gray">12</span>
}
», result.visualize());

assertTrue(result.isMatch());</code></pre>

We can also choose to elide the values of ignored fields:

<pre><code>Matcher matcher = object()
    .elideIgnoredFieldValues(<span style="color:blue">true</span>)
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, eq(<span style="color:green">"y"</span>))
    .build();

Result result = matcher.match(«{
  "a" : "x",
  "b" : "y",
  "z" : 12
}»);

assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    "b": <span style="color:green">"y"</span>,
    <span style="color:gray">"z": </span><span style="color:gray">…</span>
}
», result.visualize());

assertTrue(result.isMatch());</code></pre>

If the matcher is configured not to ignore extra fields
it will fail as follows:

<pre><code>Matcher matcher = object()
    .ignoreExtraFields(<span style="color:blue">false</span>)
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, eq(<span style="color:green">"y"</span>))
    .build();

Result result = matcher.match(«{
  "a" : "x",
  "b" : "y",
  "z" : 12
}»);

assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    "b": <span style="color:green">"y"</span>,
    <span style="color:red">"z": </span><span style="color:red">12</span> unexpected field
}
», result.visualize());

assertFalse(result.isMatch());</code></pre>

Obviously field values need to match the expectation:

<pre><code>Matcher matcher = object()
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, eq(<span style="color:green">"o"</span>))
    .build();

Result result = matcher.match(«{
  "a" : "x",
  "b" : "y"
}»);

assertFalse(result.isMatch());
assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    "b": <span style="color:red">"y"</span> expected "o"
}
»,
    result.visualize()
);</code></pre>

Missing fields will also fail to match:

<pre><code>Matcher matcher = object()
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, eq(<span style="color:green">"o"</span>))
    .build();

Result result = matcher.match(«{
  "a" : "x"
}»);

assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    <span style="color:red">"b": </span>is missing
}
»,
    result.visualize()
);

assertFalse(result.isMatch());</code></pre>

Sometimes we want to give context on a particular expection:

<pre><code>Matcher matcher = object()
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, annotate(eq(<span style="color:blue">42</span>), <span style="color:green">"This is the answer, obviously."</span>))
    .build();

Result result = matcher.match(«{
  "a" : "x",
  "b" : 42
}»);

assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    "b": <span style="color:green">42</span> ╶ This is the answer, obviously.
}
»,
    result.visualize()
);

assertTrue(result.isMatch());</code></pre>

Matching Arrays
---------------
<p>
We can also match elements of an array.
Currently only an exact match is implemented, however
more match modes, like all `ignoreExtraElements`,
`ignoreOrder` can easily be added. Here a basic
example:

<pre><code>Matcher matcher = array()
    .with(eq(<span style="color:green">"Hello"</span>))
    .with(eq(<span style="color:green">"World"</span>))
    .build();

Result result = matcher.match(«[ "Hello", "World" ]»);

assertTrue(result.isMatch());
assertEquals(«[
    <span style="color:green">"Hello"</span>,
    <span style="color:green">"World"</span>
]
», result.visualize());</code></pre>

This is what a failed match on an array element looks like:

<pre><code>Matcher matcher = array()
    .with(eq(<span style="color:green">"Hello"</span>))
    .with(eq(<span style="color:green">"World"</span>))
    .build();

Result result = matcher.match(«[ "Hello", "Boat" ]»);

assertFalse(result.isMatch());
assertEquals(«[
    <span style="color:green">"Hello"</span>,
    <span style="color:red">"Boat"</span> expected "World"
]
», result.visualize());</code></pre>

Nested Objects
--------------
<p>
We can also match on nested structures. Actually
matchers support arbitrary nesting:

<pre><code>Matcher matcher = object()
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, object()
        .with(<span style="color:green">"c"</span>, eq(<span style="color:green">"y"</span>))
        .with(<span style="color:green">"d"</span>, eq(<span style="color:green">"z"</span>))
        .build()
    )
    .build();

Result result = matcher.match(«{
  "a" : "x",
  "b" : {
    "c" : "y",
    "d" : "z"
  }
}»);

assertTrue(result.isMatch());

assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    "b": {
        "c": <span style="color:green">"y"</span>,
        "d": <span style="color:green">"z"</span>
    }
}
», result.visualize()
);</code></pre>

Annotations makes sense especially for bigger longer results.
Here an example where a nested object is annotated.

<pre><code>Matcher matcher = object()
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, annotate(
        object()
            .with(<span style="color:green">"c"</span>, eq(<span style="color:green">"y"</span>))
            .with(<span style="color:green">"d"</span>, eq(<span style="color:green">"z"</span>))
            .build(),
        <span style="color:green">"This is a nested structure."</span>
    ))
    .build();

Result result = matcher.match(«{
  "a" : "x",
  "b" : {
    "c" : "y",
    "d" : "z"
  }
}»);

assertTrue(result.isMatch());

assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    "b": {        ╮
        "c": <span style="color:green">"y"</span>, ├ This is a nested structure.
        "d": <span style="color:green">"z"</span>  │
    }             ╯
}
», result.visualize()
);</code></pre>

This is what a mismatch on a nested object looks like:

<pre><code>Matcher matcher = object()
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, object()
        .with(<span style="color:green">"c"</span>, eq(<span style="color:green">"y"</span>))
        .with(<span style="color:green">"d"</span>, eq(<span style="color:green">"z"</span>))
        .build()
    )
    .build();

Result result = matcher.match(«{
  "a" : "x",
  "b" : {
    "c" : "y",
    "d" : "42"
  }
}»);

assertFalse(result.isMatch());

assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    "b": {
        "c": <span style="color:green">"y"</span>,
        "d": <span style="color:red">"42"</span> expected "z"
    }
}
»,
    result.visualize()
);</code></pre>

We can also match `null`:

<pre><code>Matcher matcher = object()
    .with(<span style="color:green">"a"</span>, eq(<span style="color:green">"x"</span>))
    .with(<span style="color:green">"b"</span>, isNull())
    .build();

Result result = matcher.match(«{
  "a" : "x",
  "b" : null,
  "z" : null
}»);

assertEquals(«{
    "a": <span style="color:green">"x"</span>,
    "b": <span style="color:green">null</span>,
    <span style="color:gray">"z": </span><span style="color:gray">null</span>
}
», result.visualize());

assertTrue(result.isMatch());</code></pre>
