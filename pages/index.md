`jsonmatch` is a library that helps verifying JSON results
in test cases. It provides a DSL to specify expectations,
which is much nicer to use in java than providing, e.g.
json string literals. It also provides a visualisation of
the test result, that provides a view at the actual result,
projected through the expectation that has been set up.
Currently the visualisation uses ansi coloured output.

**Note**: This file is generate from the [acceptance test](src/test/java/jsonmatch/MatchingTest.java), please
don't edit the README.md , but rather the test case.

Let's look at some examples.

Matching Simple Objects
-----------------------

The happy path case:

<pre><code>var matcher = object()
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

<pre><code>var matcher = object()
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
    "z": <span style="color:gray">12</span>
}
», result.visualize());

assertTrue(result.isMatch());</code></pre>

If the matcher is configured not to ignore extra fields
it will fail as follows:

<pre><code>var matcher = object()
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

<pre><code>var matcher = object()
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

<pre><code>var matcher = object()
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

Matching Arrays
---------------

We can also match elements of an array.
Currently only an exact match is implemented, however
more match modes, like all `ignoreExtraElements`,
`ignoreOrder` can easily be added. Here a basic
example:

<pre><code>var matcher = array()
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

<pre><code>var matcher = array()
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

We can also match on nested structures. Actually
matchers support arbitrary nesting:

<pre><code>var matcher = object()
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

This is what a mismatch on a nested object looks like:

<pre><code>var matcher = object()
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
