package com.github.loyada.jdollarx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * Internal implementation.
 */
public final class XpathUtils {
    private XpathUtils() {
    }

    public static String translateTextForPath(String txt) {
            return format("translate(%s, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')", txt);
    }

    public static String processTextForXpath(String txt) {
        String[] parts = txt.split("'");
        if (parts.length==1) {
            return String.format("'%s'", txt);
        }
        else {
            BiFunction<List<String>, String, List<String>> f1 = (accum, part) -> {
                if (!accum.isEmpty())
                    accum.add("\"'\"");
                accum.add(format("\"%s\"",part));
                return accum;

            };
            BinaryOperator<List<String>> f2 =  (list1, list2) -> Stream.concat(list1.stream(), list2.stream())
                    .collect(Collectors.toList());
            List<String> escaped = Stream.of(parts).reduce(new ArrayList<>(), f1, f2);
            return format("concat(%s)", String.join(", ", escaped));
        }
    }

    public static String textContains(String text) {
        return format("contains(%s, %s)", translateTextForPath("text()"), processTextForXpath(text.toLowerCase()));
    }

    public static String textEquals(final String text) {
        return format("%s = %s", translateTextForPath("text()"), processTextForXpath(text.toLowerCase()));
    }

    public static String aggregatedTextEquals(final String text) {
        return format("%s = %s", translateTextForPath("normalize-space(string(.))"), processTextForXpath(text.toLowerCase()));
    }

    public static String aggregatedTextContains(final String text) {
        return format("contains(%s, %s)", translateTextForPath("normalize-space(string(.))"), processTextForXpath(text.toLowerCase()));
    }

    public static final String hasSomeText = "string-length(text()) > 0";

    public static String hasClass(final String className) {
        return format("contains(concat(' ', normalize-space(@class), ' '), ' %s ')", className);
    }

    public static String hasClassContaining(final String className) {
        return format("contains(@class, '%s')", className);
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
        return format("@%s='%s'", attribute, value);
    }


    public static String doesNotExist(final String path) {
        return format("not(%s)", path);
    }

    public static String doesNotExistInEntirePage(final String path) {
        final String processedPath =  (path.startsWith("//")) ? format(".%s", path) :
                                         (path.startsWith("(/")) ? format("(./%s", path.substring(2)) :
                                                 ".//" + path;
        return format("/html[not(%s)]", processedPath);
    }

    public static final String isHidden =  "contains(@style, 'display:none') or contains(normalize-space(@style), 'display: none')";

    public static String nOccurances(final String xpath, int numberOfOccurrences, RelationOperator relationOperator){
        return format("[count(//%s)%s%d]", xpath, RelationOperator.opAsXpathString(relationOperator), numberOfOccurrences);
    }

    public static String insideTopLevel(String xpath) {
        boolean alreadyInsideTopLevel = xpath.matches("^[(]*[//]+.*");

        final String prefix =  (alreadyInsideTopLevel) ? "" :
                (xpath.startsWith("(")) ? "(//" :
                        "//";
        final int chopn = (xpath.startsWith("(") && !alreadyInsideTopLevel) ? 1 : 0;
        return prefix + xpath.substring(chopn);
    }

    public static String textEndsWith(String text)  {
        return format("substring(%s, string-length(text()) - string-length(%s) +1) = %s",
                translateTextForPath("text()"), processTextForXpath(text), processTextForXpath(text.toLowerCase()));
    }

    public static String textStartsWith(String text) {
        return format("starts-with(%s, %s)",
                translateTextForPath("text()"),  processTextForXpath(text.toLowerCase()));
    }

    public static String aggregatedTextEndsWith(final String text) {
        return format("substring(%s, string-length(normalize-space(string(.))) - string-length(%s) +1) = %s",
                translateTextForPath("normalize-space(string(.))"), processTextForXpath(text), processTextForXpath(text.toLowerCase()));
    }

    public static String aggregatedTextStartsWith(final String text) {
        return format("starts-with(%s, %s)",
                translateTextForPath("normalize-space(string(.))"),  processTextForXpath(text.toLowerCase()));
    }
}
