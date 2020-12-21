package jsonmatch;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import jsonmatch.console.Color;
import lombok.Value;

@Value
public class BooleanResult implements Result {
    Boolean expectedValue;
    BooleanNode actualValue;

    @Override
    public boolean isMatch() {
            return expectedValue.equals(actualValue.booleanValue());

    }

    @Override
    public String visualize() {
        if (isMatch())
            return Color.GREEN.render(actualValue.toPrettyString());
        else
            return Color.RED.render(actualValue.toPrettyString()) + " expected \"" + expectedValue+"\"";
    }
}
