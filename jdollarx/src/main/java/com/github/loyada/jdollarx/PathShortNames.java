package com.github.loyada.jdollarx;

import static com.github.loyada.jdollarx.BasicPath.element;
import static com.github.loyada.jdollarx.ElementProperties.isSiblingOf;

/**
 * Aliases of relative paths
 */
public class PathShortNames {
    private PathShortNames() {}

    public static Path parentOf(Path el) {
        return element.parentOf(el);
    }

    public static Path childOf(Path el) {
        return element.childOf(el);
    }

    public static Path ancestorOf(Path el) {
        return element.ancestorOf(el);
    }

    public static Path descendentOf(Path el) {
        return element.descendantOf(el);
    }

    public static Path siblingOf(Path el) {
        return element.that(isSiblingOf(el));
    }
}
