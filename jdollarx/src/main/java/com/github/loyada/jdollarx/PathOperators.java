package com.github.loyada.jdollarx;


import java.util.Optional;


public final class PathOperators {
    private PathOperators(){}


    static public Path not(Path path) {
        if (!path.getXPath().isPresent()) throw new IllegalArgumentException();
        return BasicPath.builder().
                withXpath(String.format("*[not(self::%s)]", PathUtils.transformXpathToCorrectAxis(path).get())).
                withUnderlyingOptional(path.getUnderlyingSource()).
                withAlternateXpath(String.format("*[not(self::%s)]", path.getAlternateXPath().get())).
                withXpathExplanationOptional(Optional.of(String.format("anything except (%s)", path))).build();
    }
}
