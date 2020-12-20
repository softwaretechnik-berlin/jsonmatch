package jsonmatch;

public class MissingFieldResult implements Result{
    private final Object fieldName;

    public MissingFieldResult(Object fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean isMatch() {
        return false;
    }

    @Override
    public String visualize() {
        return "is missing.";
    }
}
