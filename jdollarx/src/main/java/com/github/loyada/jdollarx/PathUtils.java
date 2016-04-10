package com.github.loyada.jdollarx;


import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PathUtils {
    private PathUtils(){}

    static boolean hasHeirarchy(String xpath) {
        return xpath.contains("/");
    }

    static Optional<String> transformXpathToCorrectAxis(Path sourcePath) {
        if (sourcePath.getXPath().isPresent() && hasHeirarchy(sourcePath.getXPath().get())) {
            return sourcePath.getAlternateXPath();
        } else {
            return sourcePath.getXPath();
        }
    }

    static String oppositeRelation(String relation) {
        return oppositeRelationMap.get(relation);
    }

    private static final Pattern axisRegex = Pattern.compile("^(parent|child|ancestor|descendant|following|preceding|self|ancestor-or-self|descendant-or-self|following-sibling|preceding-sibling)::(.*)$");
    private static final Map<String, String> oppositeRelationMap = Collections.unmodifiableMap(Stream.of(
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
