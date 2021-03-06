package jsonmatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface Matcher {
    ObjectMapper mapper = new ObjectMapper();

    Result match(JsonNode parsed);

    default Result match(String matchee) {
        try {
            return this.match(mapper.readValue(matchee, JsonNode.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    };
}
