package org.lab49.jdollarx;

import java.util.Arrays;
import java.util.stream.Collectors;


public final class XpathUtils {
    private XpathUtils() {
    }

    public static String translateTextForPath(String txt) {
        return String.format("translate(%s, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')", txt);
    }

    public static String textContains(String text) {
        return String.format("contains(%s, '%s')", translateTextForPath("text()"), text.toLowerCase());
    }

    public static String textEquals(String text) {
        return String.format("%s = '%s'", translateTextForPath("text()"), text.toLowerCase());
    }

    public static String aggregatedTextEquals(String text) {
        return String.format("%s = '%s'", translateTextForPath("normalize-space(string(.))"), text.toLowerCase());
    }

    public static String aggregatedTextContains(String text) {
        return String.format("contains(%s, '%s')", translateTextForPath("normalize-space(string(.))"), text.toLowerCase());
    }

    public static final String hasSomeText = "string-length(text()) > 0";

    public static String hasClass(String className) {
        return String.format("contains(concat(' ', @class, ' '), ' %s ')", className);
    }

    public static String hasClasses(String... classNames) {
        return hasClassesInternal("and", classNames);
    }

    public static String hasAnyOfClasses(String... classNames) {
        return hasClassesInternal("or", classNames);
    }

    private static String hasClassesInternal(String logicOp, String... classNames) {
        return Arrays.stream(classNames).map(XpathUtils::hasClass).collect(Collectors.joining(" " + logicOp + " "));
    }

    public static String hasId(String id) {
        return hasAttribute("id", id);
    }

    public static String hasAttribute(String attribute, String value) {
        return String.format("@%s='%s'", attribute, value);
    }


    public static String doesNotExist(String path) {
        return String.format("not(%s)", path);
    }

    public static String doesNotExistInEntirePage(String path) {
        String processedPath = (path.startsWith("//")) ? path : ("//" + path);
        return String.format("/html[not(.%s)]", processedPath);
    }

    public static final String isHidden =  "contains(@style, 'display:none') or contains(normalize-space(@style), 'display: none')";
}
