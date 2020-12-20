package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.Value;

@Value
public class StringMatcher implements Matcher {
    String expectedValue;

    @Override
    public Result match(JsonNode parsed) {
        if (parsed.getNodeType() != JsonNodeType.STRING) {
            return new WrongTypeResult(NodeType.STRING, NodeType.fromJackson(parsed.getNodeType()), parsed);
        }
        return new StringResult(expectedValue, (TextNode) parsed);
    }
}
