package jsonmatch;

import com.fasterxml.jackson.databind.node.JsonNodeType;

public enum NodeType {

    ARRAY("Array"),
    OBJECT("Object"),
    STRING("String"),
    BOOLEAN("Boolean"),
    NUMBER("Number"),
    NULL("Null"),
    ;

    private final String printableString;

    NodeType(String printableString) {
        this.printableString = printableString;
    }

    public static NodeType fromJackson(JsonNodeType jacksonType) {
        switch (jacksonType) {
            case ARRAY: return ARRAY;
            case OBJECT: return OBJECT;
            case STRING: return STRING;
            case BOOLEAN: return BOOLEAN;
            case NUMBER: return NUMBER;
            case NULL: return NULL;
        }
        throw new RuntimeException("Could not map '" + jacksonType + "' to NodeType");
    }

    @Override
    public String toString() {
        return printableString;
    }
}
