package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class NullMatcher implements Matcher {
    @Override
    public Result match(JsonNode parsed) {
        if (parsed.getNodeType() != JsonNodeType.NULL) {
            return new WrongTypeResult(NodeType.NULL, NodeType.fromJackson(parsed.getNodeType()), parsed);
        }
        return new NullResult();
    }
}
