package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Streams;
import lombok.Value;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static jsonmatch.NodeType.OBJECT;
import static jsonmatch.util.Pair.pair;

@Value
public class ObjectMatcher implements Matcher {
    LinkedHashMap<String, Matcher> fieldMatchers;
    boolean ignoreExtraFields;
    boolean elideIgnoredFieldValues;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Result match(JsonNode parsed) {
        if (!(parsed instanceof ObjectNode)) {
            return new WrongTypeResult(OBJECT, NodeType.fromJackson(parsed.getNodeType()), parsed);
        }
        ObjectNode obj = (ObjectNode) parsed;
        List<String> missingFieldNames = new ArrayList<>(fieldMatchers.keySet());
        obj.fieldNames().forEachRemaining(missingFieldNames::remove); // I feel so dirty;

        return new ObjectResult(Streams.concat(
                StreamSupport.stream(((Iterable<String>) obj::fieldNames).spliterator(), false).map(fieldName -> pair(fieldName,
                        fieldMatchers.containsKey(fieldName) ? fieldMatchers.get(fieldName).match(parsed.get(fieldName)) :
                        ignoreExtraFields ? new IgnoredFieldResult(parsed.get(fieldName), elideIgnoredFieldValues) :
                        new ExtraFieldResult(parsed.get(fieldName))
                )),
                missingFieldNames.stream().map(fieldName -> pair(fieldName,
                        (Result) new MissingFieldResult(fieldName)
                ))
        ).collect(Collectors.toList()));
    }

    public static class Builder implements MatcherBuilder {
        private final LinkedHashMap<String, Matcher> fieldMatchers = new LinkedHashMap<>();
        private boolean ignoreExtraFields = true;
        private boolean elideIgnoredFieldValues = false;

        public Builder with(String fieldName, MatcherBuilder valueMatcherBuilder) {
            return with(fieldName, valueMatcherBuilder.build());
        }

        public Builder with(String fieldName, Matcher valueMatcher) {
            fieldMatchers.put(fieldName, valueMatcher);
            return this;
        }

        public Builder ignoreExtraFields(boolean b) {
            this.ignoreExtraFields = b;
            return this;
        }

        public Builder elideIgnoredFieldValues(boolean b) {
            this.elideIgnoredFieldValues = b;
            return this;
        }

        public ObjectMatcher build() {
            return new ObjectMatcher(fieldMatchers, ignoreExtraFields, elideIgnoredFieldValues);
        }
    }
}
