package com.github.loyada.jdollarx;


import java.util.Optional;


public final class PathOperators {
    private PathOperators(){}


    /**
     * Any element that does NOT conform to the definition of the given path parameters
     * @param path - the path that represent what the element does NOT match
     * @return a new path that represents the negation of the given parameter
     */
    static public Path not(Path path) {
        if (!path.getXPath().isPresent()) throw new IllegalArgumentException();
        return BasicPath.builder().
                withXpath(String.format("*[not(self::%s)]", PathUtils.transformXpathToCorrectAxis(path).get())).
                withUnderlyingOptional(path.getUnderlyingSource()).
                withAlternateXpath(String.format("*[not(self::%s)]", path.getAlternateXPath().get())).
                withXpathExplanationOptional(Optional.of(String.format("anything except (%s)", path))).build();
    }
}
