package jsonmatch;

import com.fasterxml.jackson.databind.JsonNode;

import static jsonmatch.util.Color.GRAY;

public class IgnoredFieldResult implements Result {
    private final JsonNode jsonNode;
    private final boolean elideValue;

    public IgnoredFieldResult(JsonNode jsonNode, boolean elideValue) {
        this.jsonNode = jsonNode;
        this.elideValue = elideValue;
    }

    @Override
    public boolean isMatch() {
        return true;
    }

    @Override
    public String visualize() {
        return GRAY.render(elideValue?  "…" : jsonNode.toPrettyString());
    }
}
