package info.testtools.jdollarx;

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

    public static String textEquals(final String text) {
        return String.format("%s = '%s'", translateTextForPath("text()"), text.toLowerCase());
    }

    public static String aggregatedTextEquals(final String text) {
        return String.format("%s = '%s'", translateTextForPath("normalize-space(string(.))"), text.toLowerCase());
    }

    public static String aggregatedTextContains(final String text) {
        return String.format("contains(%s, '%s')", translateTextForPath("normalize-space(string(.))"), text.toLowerCase());
    }

    public static final String hasSomeText = "string-length(text()) > 0";

    public static String hasClass(final String className) {
        return String.format("contains(concat(' ', normalize-space(@class), ' '), ' %s ')", className);
    }

    public static String hasClasses(final String... classNames) {
        return hasClassesInternal("and", classNames);
    }

    public static String hasAnyOfClasses(final String... classNames) {
        return hasClassesInternal("or", classNames);
    }

    private static String hasClassesInternal(final String logicOp, String... classNames) {
        return Arrays.stream(classNames).map(XpathUtils::hasClass).collect(Collectors.joining(" " + logicOp + " "));
    }

    public static String hasId(final String id) {
        return hasAttribute("id", id);
    }

    public static String hasAttribute(final String attribute, final String value) {
        return String.format("@%s='%s'", attribute, value);
    }


    public static String doesNotExist(final String path) {
        return String.format("not(%s)", path);
    }

    public static String doesNotExistInEntirePage(final String path) {
        final String processedPath =  (path.startsWith("//")) ? String.format(".%s", path) :
                                         (path.startsWith("(/")) ? String.format("(./%s", path.substring(2)) :
                                                 ".//" + path;
        return String.format("/html[not(%s)]", processedPath);
    }

    public static final String isHidden =  "contains(@style, 'display:none') or contains(normalize-space(@style), 'display: none')";
}
