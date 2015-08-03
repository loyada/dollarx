package info.dollarx.custommatchers.hamcrest;

import info.dollarx.Path;

final class CustomMatchersUtil {


    static public String wrap(Path el) {
        String asString = el.toString();
        return (asString.contains(" ")) ? String.format("(%s)", asString) : asString;
    }
}
