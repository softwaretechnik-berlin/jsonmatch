package jsonmatch;

import jsonmatch.util.Color;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static jsonmatch.TextUtils.indent;


public class ObjectResult implements Result {


    private final List<Map.Entry<String, Result>> fieldResults;

    public ObjectResult(List<Map.Entry<String, Result>> fieldResults) {
        this.fieldResults = fieldResults;
    }

    @Override
    public boolean isMatch() {
        // the logic below will move to the matcher once we allow for mor fancy
        // matching modes.
        return fieldResults.stream().map(Map.Entry::getValue).allMatch(Result::isMatch);
    }

    public String visualize() {

        return
            "{\n" +
                indent(
                    fieldResults.stream()
                        .map(entry -> {
                            final String fieldPrefix = "\"" + entry.getKey() + "\": ";
                            final VisualisationContext context = new VisualisationContext(fieldPrefix.length());

                            if (entry.getValue() instanceof MissingFieldResult) {

                                return Color.RED.render(fieldPrefix) + entry.getValue().visualize(context);
                                } else if (entry.getValue() instanceof ExtraFieldResult) {
                                    return Color.RED.render(fieldPrefix) + entry.getValue().visualize(context);
                                } else if (entry.getValue() instanceof GrayResult) {
                                    return Color.GRAY.render(fieldPrefix) + entry.getValue().visualize(context);
                                } else {
                                    return
                                        "\"" + entry.getKey() + "\": " + entry.getValue().visualize(context);
                                }
                            }
                        )
                        .collect(Collectors.joining(",\n"))
                ) +
                "\n}\n";
    }

}
