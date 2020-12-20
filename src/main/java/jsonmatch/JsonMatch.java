package jsonmatch;

public class JsonMatch {
    public static ObjectMatcher.Builder object() {
        return ObjectMatcher.builder();
    }

    public static ArrayMatcher.Builder array() {
        return ArrayMatcher.builder();
    }

    public static Matcher eq(String str) {
        return new StringMatcher(str);
    }

    public static Matcher eq(Number number) {
        return new NumberMatcher(number);
    }

}
