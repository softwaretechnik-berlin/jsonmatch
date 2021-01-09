package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Value;

@Value
public class AnnotatedMatcher implements Matcher {

    Matcher annotatedMatcher;
    String annotation;

    @Override
    public Result match(JsonNode parsed) {
        return new AnnotatedResult(annotatedMatcher.match(parsed), annotation);
    }

}
