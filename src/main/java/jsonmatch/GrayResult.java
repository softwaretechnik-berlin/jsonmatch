package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;

import static jsonmatch.util.Color.GRAY;

public class GrayResult implements Result {
    private final JsonNode jsonNode;

    public GrayResult(JsonNode jsonNode) {

        this.jsonNode = jsonNode;
    }

    @Override
    public boolean isMatch() {
        return true;
    }

    @Override
    public String visualize() {
        return GRAY.render(jsonNode.toPrettyString());
    }
}
