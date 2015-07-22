package info.testtools.jdollarx.custommatchers;

import info.testtools.jdollarx.Path;

public class HasElements {
    private final Path path;

    public HasElements(final Path path){
        this.path = path;
    }

    public HasElementNTimes present(int nTimes) {
        return new HasElementNTimes(path, nTimes);
    }
}