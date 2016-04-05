package com.github.loyada.jdollarx;


import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PathUtils {
    private PathUtils(){}

    public static String transformXpathToCorrectAxis(String sourceXpath) {
        Integer indexOfInsideMarker = sourceXpath.indexOf("//");
        if (indexOfInsideMarker >= 0) {
            String insidePart = sourceXpath.substring(indexOfInsideMarker + 2);
            String outsidePart = sourceXpath.substring(0, indexOfInsideMarker);
            String relation = findOppositeRelation(insidePart, "descendant");
            String correctedInsidePart = correctInsideClause(insidePart);
            String fixed = String.format("%s[%s::%s]", correctedInsidePart, relation, outsidePart);
            return transformXpathToCorrectAxis(fixed);
        } else {
            Integer indexOfChildMarker = sourceXpath.indexOf("/");
            if (indexOfChildMarker >= 0) {
                String insidePart = sourceXpath.substring(indexOfChildMarker + 1);
                String outsidePart = sourceXpath.substring(0, indexOfChildMarker);
                String relation = findOppositeRelation(insidePart, "child");
                String correctedInsidePart = correctInsideClause(insidePart);
                String fixed = String.format("%s[%s::%s]", correctedInsidePart, relation, outsidePart);
                return transformXpathToCorrectAxis(fixed);
            }
            else {
                return sourceXpath;
            }
        }
    }

    private static String findOppositeRelation(String insidePart, String defaultRelation) {
        Matcher matched = axisRegex.matcher(insidePart);
        String relation = (matched.matches()) ? matched.group(1) : defaultRelation;
        return oppositeRelation.get(relation);
    }

    private static String correctInsideClause(String insideClause) {
        Matcher matched = axisRegex.matcher(insideClause);
        return (matched.matches()) ? matched.group(2) : insideClause;
    }



    private static final Pattern axisRegex = Pattern.compile("^(parent|child|ancestor|descendant|following|preceding|self|ancestor-or-self|descendant-or-self|following-sibling|preceding-sibling)::(.*)$");
    private static final Map<String, String> oppositeRelation = Collections.unmodifiableMap(Stream.of(
            new SimpleEntry<>("parent", "child"),
            new SimpleEntry<>("child", "parent"),
            new SimpleEntry<>("ancestor", "descendant"),
            new SimpleEntry<>("following", "preceding"),
            new SimpleEntry<>("self", "self"),
            new SimpleEntry<>("ancestor-or-self", "descendant-or-self"),
            new SimpleEntry<>("descendant-or-self", "ancestor-or-self"),
            new SimpleEntry<>("descendant", "ancestor"),
            new SimpleEntry<>("following-sibling", "preceding-sibling"),
            new SimpleEntry<>("preceding-sibling", "following-sibling"),
            new SimpleEntry<>("preceding", "following")
            )
            .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));
}
