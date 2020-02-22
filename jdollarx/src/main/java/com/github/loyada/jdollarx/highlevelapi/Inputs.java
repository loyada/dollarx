package com.github.loyada.jdollarx.highlevelapi;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.ElementProperties.hasId;
import static com.github.loyada.jdollarx.HighLevelPaths.hasType;
import static com.google.common.base.Strings.isNullOrEmpty;

public final class Inputs {
    private Inputs() {}

    public static Path inputForLabel(InBrowser browser, String labelText) {
        Path myLabel = label.that(hasAggregatedTextEqualTo(labelText.trim()));
        String theId = browser.find(myLabel).getAttribute("for");
        if (isNullOrEmpty(theId)) {
            return input.inside(myLabel);
        }
        return input.that(hasId(theId));
    }

    public static Path inputFollowedByUnlabeledText(String text) {
        return input.immediatelyBeforeSibling(textNode(text));
    }

    public static Path checkboxType(Path inp) {
        return inp.that(hasType("checkbox"));
    }

    public static Path radioType(Path inp) {
        return inp.that(hasType("radio"));
    }

}
