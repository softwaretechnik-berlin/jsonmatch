package jsonmatch;

import jsonmatch.util.Color;

public class NullResult implements Result {
    @Override
    public boolean isMatch() {
        return true;
    }

    @Override
    public String visualize() {
        return Color.GREEN.render("null");
    }
}
