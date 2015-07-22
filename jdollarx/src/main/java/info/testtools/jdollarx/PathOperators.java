package info.testtools.jdollarx;


import java.util.Optional;


public final class PathOperators {
    private PathOperators(){}


    static public Path not(Path path) {
        if (!path.getXPath().isPresent()) throw new IllegalArgumentException();
        String pathString = path.getXPath().get();
        if (pathString.contains("::") || pathString.contains("/")) throw new UnsupportedOperationException("operator \"not\" is not supported on a path with elements relation");
        return BasicPath.builder().withXpath(String.format("*[not(self::%s)]", (path.getXPath().get()))).
                withUnderlyingOptional(path.getUnderlyingSource()).
                withXpathExplanationOptional(Optional.of(String.format("anything except (%s)", path))).build();
    }
}
