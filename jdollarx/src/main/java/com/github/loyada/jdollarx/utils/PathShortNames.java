package com.github.loyada.jdollarx.utils;

import com.github.loyada.jdollarx.Path;

import static com.github.loyada.jdollarx.BasicPath.element;
import static com.github.loyada.jdollarx.ElementProperties.isSiblingOf;

public class PathShortNames {
    private PathShortNames() {}

    /**
     * Alias
     * @param path the element
     * @return the corresponding Path definition
     */
    public static Path parentOf(Path path){
        return element.parentOf(path);
    }

    /**
     * Alias
     * @param path the element
     * @return the corresponding Path definition
     */
    public static Path childOf(Path path){
        return element.childOf(path);
    }

    /**
     * Alias
     * @param path the element
     * @return the corresponding Path definition
     */
    public static Path ancestorOf(Path path){
        return element.containing(path);
    }

    /**
     * Alias
     * @param path the element
     * @return the corresponding Path definition
     */
    public static Path siblingOf(Path path){
        return element.that(isSiblingOf(path));
    }
}
