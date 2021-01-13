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
import static jsonmatch.util.Pair.pair;

@Value
public class ObjectMatcher implements Matcher {
    LinkedHashMap<String, Matcher> fieldMatchers;
    boolean ignoreExtraFields;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Result match(JsonNode parsed) {
        if (!(parsed instanceof ObjectNode)) {
            return new WrongTypeResult(OBJECT, NodeType.fromJackson(parsed.getNodeType()), parsed);
        }
        ObjectNode obj = (ObjectNode) parsed;
        List<String> actualFieldNames = ImmutableList.copyOf(obj.fieldNames());
        List<String> matcherFields = new ArrayList<>(fieldMatchers.keySet());
        List<String> missingFieldNames = new ArrayList<>(matcherFields);
        missingFieldNames.removeAll(actualFieldNames); // I feel so dirty;

        List<Map.Entry<String, Result>> resultsForFields = actualFieldNames.stream().map(fieldName -> {
            if (fieldMatchers.containsKey(fieldName)) {
                return pair(fieldName, fieldMatchers.get(fieldName).match(parsed.get(fieldName)));
            } else {
                if (ignoreExtraFields) {
                    return pair(fieldName, (Result) new GrayResult(parsed.get(fieldName)));
                } else {
                    return pair(fieldName, (Result) new ExtraFieldResult(parsed.get(fieldName)));
                }
            }
        }).collect(Collectors.toList());

        List<Map.Entry<String, Result>> missingFieldResults = missingFieldNames.stream()
                .map(fieldName -> pair(fieldName, (Result) new MissingFieldResult(fieldName)))
                .collect(Collectors.toList());

        resultsForFields.addAll(missingFieldResults);
        return new ObjectResult(
                resultsForFields
        );
    }

    public static class Builder implements MatcherBuilder {
        private final LinkedHashMap<String, Matcher> fieldMatchers = new LinkedHashMap<>();

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
}
