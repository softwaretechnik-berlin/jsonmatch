package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;

import static jsonmatch.util.Color.RED;

public class ExtraFieldResult implements Result {
    private final JsonNode jsonNode;

    public ExtraFieldResult(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    @Override
    public boolean isMatch() {
        return false;
    }

    @Override
    public String visualize() {
        return RED.render(jsonNode.toPrettyString()) + " unexpected field";
    }
}
