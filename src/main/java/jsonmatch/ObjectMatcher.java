package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Value;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Value
public class ObjectMatcher implements Matcher {
    public static class Builder {
        private LinkedHashMap<String, Matcher> fieldMatchers = new LinkedHashMap<>();

        public Builder with(String fieldName, Matcher valueMatcher) {
            fieldMatchers.put(fieldName, valueMatcher);
            return this;
        }

        public ObjectMatcher build() {
            return new ObjectMatcher(fieldMatchers);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
    
    LinkedHashMap<String, Matcher> fieldMatchers;

    @Override
    public Result match(JsonNode parsed) {
        return new ObjectResult(fieldMatchers.entrySet().stream().map(
            entry -> Map.entry(entry.getKey(), entry.getValue().match(parsed.get(entry.getKey())))
        ).collect(Collectors.toList()));
    }
}
