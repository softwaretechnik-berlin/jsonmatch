package jsonmatch;

public class JsonMatch {

    /** Match an Object. */
    public static ObjectMatcher.Builder object() {
        return ObjectMatcher.builder();
    }

    /** Match an Array. */
    public static ArrayMatcher.Builder array() {
        return ArrayMatcher.builder();
    }

    /** Match a String. */
    public static Matcher eq(String str) {
        return new StringMatcher(str);
    }

    /** Match a Number. */
    public static Matcher eq(Number number) {
        return new NumberMatcher(number);
    }

    /** Match a boolean. */
    public static Matcher eq(boolean b) {
        return new BooleanMatcher(b);
    }

    /**
     * Add an annotation to the given matcher that is rendered next to
     * the matched value.
     */
    public static Matcher annotate(Matcher matcher, String annotation) {
        return new AnnotatedMatcher(matcher, annotation);
    }
}
