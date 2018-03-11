package com.github.loyada.jdollarx;

/**
 * Internal implementation.
 */
public enum RelationOperator {
    exactly, orMore, orLess;

    public static String opAsXpathString(RelationOperator op) {
        switch (op) {
            case exactly:
                return "=";
            case orMore:
                return ">=";
            case orLess:
                return "<=";
            default:
                throw new IllegalArgumentException();
        }
    }

    public static String opAsEnglish(RelationOperator op) {
        switch (op) {
            case exactly:
                return " ";
            case orMore:
                return " at least ";
            case orLess:
                return " at most ";
            default:
                throw new IllegalArgumentException();
        }
    }

}
