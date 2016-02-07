package com.github.loyada.jdollarx.custommatchers;

import com.github.loyada.jdollarx.Path;

final class CustomMatchersUtil {


    static public String wrap(Path el) {
        String asString = el.toString();
        return (asString.contains(" ")) ? String.format("(%s)", asString) : asString;
    }
}
