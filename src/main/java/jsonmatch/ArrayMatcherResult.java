package jsonmatch;

import java.util.List;
import java.util.stream.Collectors;

import static jsonmatch.TextUtils.indent;

public class ArrayMatcherResult implements Result {
    private final List<Result> elementResults;

    public ArrayMatcherResult(List<Result> elementResults) {
        this.elementResults = elementResults;
    }

    @Override
    public boolean isMatch() {
        return elementResults.stream().allMatch(Result::isMatch);
    }

    @Override
    public String visualize() {
        return "[\n" +
            indent(elementResults.stream().map(Result::visualize).collect(Collectors.joining(",\n"))) + "\n"
            + "]\n";
    }
}
