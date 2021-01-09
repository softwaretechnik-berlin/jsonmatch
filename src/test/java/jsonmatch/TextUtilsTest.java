package jsonmatch;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextUtilsTest {
    @Test
    public void annotation() {
        assertEquals("Hallo    ╮ annotation.\n" +
            "Was      │\n" +
            "Ist Das? ╯", TextUtils.annotate("Hallo\nWas\nIst Das?", "annotation."));
    }

}
