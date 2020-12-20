package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static jsonmatch.NodeType.ARRAY;

public class ArrayMatcher implements Matcher{
    private final List<Matcher> elementMatchers;

    public ArrayMatcher(List<Matcher> elementMatchers) {
        this.elementMatchers = elementMatchers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        List<Matcher> elementMatchers = new LinkedList<>();

        public ArrayMatcher.Builder with(Matcher elementMatcher) {
            elementMatchers.add(elementMatcher);
            return this;
        }

        public ArrayMatcher build() {
            return new ArrayMatcher(elementMatchers);
        }
    }

    @Override
    public Result match(JsonNode parsed) {
        if (!parsed.isArray()) {
            return new WrongTypeResult(ARRAY, NodeType.fromJackson(parsed.getNodeType()), parsed);
        }

        AtomicInteger index = new AtomicInteger(); // wow java why do you make me do this. need to move to totallylazy
        List<Result> results = elementMatchers.stream().map(matcher -> {
                final Result result = matcher.match(parsed.get(index.get()));
                index.getAndIncrement();
                return result;
            }
        ).collect(Collectors.toList());

        return new ArrayMatcherResult(results);
    }
}
