package org.lab49.jdollarx;


import java.util.Optional;


public final class PathOperators {
    private PathOperators(){}

    static public BasicPath not(Path path) {
        if (!path.getXPath().isPresent()) throw new IllegalArgumentException();
        return BasicPath.builder().withXpath(XpathUtils.doesNotExistInEntirePage(path.getXPath().get())).
                withUnderlyingOptional(path.getUnderlyingSource()).
                withXpathExplanationOptional(Optional.of(String.format("anything except (%s)", path))).build();
    }
}
