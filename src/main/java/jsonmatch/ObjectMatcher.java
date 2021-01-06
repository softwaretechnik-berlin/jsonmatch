package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import lombok.Value;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static jsonmatch.NodeType.OBJECT;

@Value
public class ObjectMatcher implements Matcher {
    public static class Builder implements MatcherBuilder{
        private LinkedHashMap<String, Matcher> fieldMatchers = new LinkedHashMap<>();

        private boolean ignoreExtraFields = true;

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
        public ObjectMatcher build() {
            return new ObjectMatcher(fieldMatchers, ignoreExtraFields);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    LinkedHashMap<String, Matcher> fieldMatchers;
    boolean ignoreExtraFields;

    @Override
    public Result match(JsonNode parsed) {
        if (! (parsed instanceof ObjectNode)) {
            return new WrongTypeResult(OBJECT, NodeType.fromJackson(parsed.getNodeType()), parsed);
        }
        var obj = (ObjectNode) parsed;
        var actualFieldNames = ImmutableList.copyOf(obj.fieldNames());
        var matcherFields = new ArrayList<>(fieldMatchers.keySet());
        var missingFieldNames = new ArrayList<String>(matcherFields);
        missingFieldNames.removeAll(actualFieldNames); // I feel so dirty;

        List<Map.Entry<String, Result>> resultsForFields = actualFieldNames.stream().map(fieldName -> {
            if (fieldMatchers.containsKey(fieldName)) {
                return Map.entry(fieldName, fieldMatchers.get(fieldName).match(parsed.get(fieldName)));
            } else {
                if (ignoreExtraFields) {
                    return Map.entry(fieldName, (Result) new GrayResult(parsed.get(fieldName)));
                } else {
                    return Map.entry(fieldName, (Result) new ExtraFieldResult(parsed.get(fieldName)));
                }
            }
        }).collect(Collectors.toList());

        List<Map.Entry<String, Result>> missingFieldResults = missingFieldNames.stream()
            .map(fieldName -> Map.entry(fieldName, (Result) new MissingFieldResult(fieldName)))
            .collect(Collectors.toList());

        resultsForFields.addAll(missingFieldResults);
        return new ObjectResult(
            resultsForFields
        );
    }
}
