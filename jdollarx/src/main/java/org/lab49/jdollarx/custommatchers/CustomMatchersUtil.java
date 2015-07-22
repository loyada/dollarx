package org.lab49.jdollarx.custommatchers;

import org.lab49.jdollarx.Path;

final class CustomMatchersUtil {


    static public String wrap(Path el) {
        String asString = el.toString();
        return (asString.contains(" ")) ? String.format("(%s)", asString) : asString;
    }
}
