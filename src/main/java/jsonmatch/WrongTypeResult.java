package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;
import jsonmatch.util.Color;


public class WrongTypeResult implements Result {
    private final NodeType expectedType;
    private final NodeType actualType;
    private final JsonNode actualValue;

    public WrongTypeResult(NodeType expectedType, NodeType actualType, JsonNode actualValue) {
        this.expectedType = expectedType;
        this.actualType = actualType;
        this.actualValue = actualValue;
    }

    @Override
    public boolean isMatch() {
        return false;
    }

    @Override
    public String visualize() {
        return Color.RED.render("expected <" + expectedType + ">") + " but got <" + actualType + ">";
    }
}
