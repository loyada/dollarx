package com.github.loyada.jdollarx;


public final class NPath {
    public final Path path;
    public final int n;
    public final RelationOperator qualifier;

    public static NPathBuilder atLeast(int n) {
        return new NPathBuilder(n, RelationOperator.orMore);
    }

    public static NPathBuilder atMost(int n) {
        return new NPathBuilder(n, RelationOperator.orLess);
    }

    public static NPathBuilder exactly(int n) {
        return new NPathBuilder(n, RelationOperator.exactly);
    }

    static class NPathBuilder {
        final int n;
        final RelationOperator qualifier;

        NPathBuilder(int n, RelationOperator qualifier) {
            this.n = n;
            this.qualifier = qualifier;
        }

        public NPath occurrencesOf(Path path) {
            return new NPath(path, n, qualifier);
        }
    }

    public NPath(Path path, int n, RelationOperator qualifier ) {
        this.path = path;
        this.n = n;
        this.qualifier = qualifier;
    }
}
