package jsonmatch;

import com.fasterxml.jackson.databind.node.TextNode;
import jsonmatch.util.Color;
import lombok.Value;

@Value
public class StringResult implements Result {
    String expectedValue;
    TextNode actualValue;


    @Override
    public boolean isMatch() {
            return expectedValue.equals(actualValue.asText());

    }

    @Override
    public String visualize() {
        if (isMatch())
            return Color.GREEN.render(actualValue.toPrettyString());
        else
            return Color.RED.render(actualValue.toPrettyString()) + " expected \"" + expectedValue+"\"";
    }
}
