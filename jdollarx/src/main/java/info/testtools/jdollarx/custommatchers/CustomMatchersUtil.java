package info.testtools.jdollarx.custommatchers;

import info.testtools.jdollarx.Path;

final class CustomMatchersUtil {


    static public String wrap(Path el) {
        String asString = el.toString();
        return (asString.contains(" ")) ? String.format("(%s)", asString) : asString;
    }
}
