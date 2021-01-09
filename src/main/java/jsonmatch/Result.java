package jsonmatch;

interface Result {
    boolean isMatch();

    default String visualize(VisualisationContext context) {
        return this.visualize();
    }

    default String visualize() {
        return this.visualize(null);
    }
}
