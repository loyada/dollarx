package com.github.loyada.jdollarx.custommatchers;

import com.github.loyada.jdollarx.Path;

public class HasElements {
    private final Path path;

    public HasElements(final Path path){
        this.path = path;
    }

    public HasElementNTimes present(int nTimes) {
        return new HasElementNTimes(path, nTimes);
    }
}