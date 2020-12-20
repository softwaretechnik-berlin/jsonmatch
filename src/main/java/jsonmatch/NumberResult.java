package jsonmatch;

import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import jsonmatch.console.Color;
import lombok.Value;

@Value
public class NumberResult implements Result {
    Number expectedValue;
    NumericNode actualValue;


    @Override
    public boolean isMatch() {
            return expectedValue.equals(actualValue.numberValue());

    }

    @Override
    public String visualize() {
        if (isMatch())
            return Color.GREEN.render(actualValue.toPrettyString());
        else
            return Color.RED.render(actualValue.toPrettyString()) + " expected \"" + expectedValue+"\"";
    }
}
