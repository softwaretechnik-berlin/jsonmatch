package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.NumericNode;
import lombok.Value;

@Value
public class BooleanMatcher implements Matcher {
    Boolean expectedValue;

    @Override
    public Result match(JsonNode parsed) {
        if (parsed.getNodeType() != JsonNodeType.BOOLEAN) {
            return new WrongTypeResult(NodeType.BOOLEAN, NodeType.fromJackson(parsed.getNodeType()), parsed);
        }
        return new BooleanResult(expectedValue, (BooleanNode) parsed);
    }
}
