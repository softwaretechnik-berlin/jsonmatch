package jsonmatch;

public class AnnotatedResult implements Result {
    private final Result annotatedResult;
    private final String annotation;

    public AnnotatedResult(Result annotatedResult, String annotation) {
        this.annotatedResult = annotatedResult;
        this.annotation = annotation;
    }

    @Override
    public boolean isMatch() {
        return annotatedResult.isMatch();
    }

    @Override
    public String visualize(VisualisationContext context) {
        if (context != null) {
            return TextUtils.annotate(annotatedResult.visualize(), annotation, context.getExtraIndent()) ;
        }
        return TextUtils.annotate(annotatedResult.visualize(), annotation) ;
    }
}
