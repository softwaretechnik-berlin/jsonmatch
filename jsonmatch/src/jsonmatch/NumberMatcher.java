package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.NumericNode;
import lombok.Value;

@Value
public class NumberMatcher implements Matcher {
    Number expectedValue;

    @Override
    public Result match(JsonNode parsed) {
        if (parsed.getNodeType() != JsonNodeType.NUMBER) {
            return new WrongTypeResult(NodeType.NUMBER, NodeType.fromJackson(parsed.getNodeType()), parsed);
        }
        return new NumberResult(expectedValue, (NumericNode) parsed);
    }
}
