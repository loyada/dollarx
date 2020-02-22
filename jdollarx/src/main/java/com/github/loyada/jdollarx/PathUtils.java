package com.github.loyada.jdollarx;


import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;

/**
 * Internal implementation.
 */
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
        int indexOfAxis = relation.indexOf(":");
        if (indexOfAxis!=-1 ) {
            return oppositeRelationMap.get(relation.substring(0, indexOfAxis)) + relation.substring(indexOfAxis);
        }
        return oppositeRelationMap.get(relation);
    }

    private static final Pattern axisRegex = Pattern.compile("^(parent|child|ancestor|descendant|following|preceding|self|ancestor-or-self|descendant-or-self|following-sibling|preceding-sibling)::(.*)$");

    private static SimpleEntry<String, String> e(String k, String v) {
        return new SimpleEntry<>(k, v);
    }
    private static final Map<String, String> oppositeRelationMap = unmodifiableMap(Stream.of(
            e("parent", "child"),
            e("child", "parent"),
            e("ancestor", "descendant"),
            e("following", "preceding"),
            e("self", "self"),
            e("ancestor-or-self", "descendant-or-self"),
            e("descendant-or-self", "ancestor-or-self"),
            e("descendant", "ancestor"),
            e("following-sibling", "preceding-sibling"),
            e("preceding-sibling", "following-sibling"),
            e("preceding", "following")
            )
            .collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
}
