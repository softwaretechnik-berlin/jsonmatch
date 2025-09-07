package jsonmatch;

public class AnnotatedResult implements Result {
    private final Result result;
    private final String annotation;

    public AnnotatedResult(Result result, String annotation) {
        this.result = result;
        this.annotation = annotation;
    }

    @Override
    public boolean isMatch() {
        return result.isMatch();
    }

    @Override
    public String visualize(VisualisationContext context) {
        if (context != null) {
            return TextUtils.annotate(result.visualize(), annotation, context.getExtraIndent());
        }
        return TextUtils.annotate(result.visualize(), annotation);
    }
}
