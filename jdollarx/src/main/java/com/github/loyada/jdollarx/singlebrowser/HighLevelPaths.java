package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.BasicPath;
import com.github.loyada.jdollarx.ElementProperties;
import com.github.loyada.jdollarx.Path;

public class HighLevelPaths {

    public static Path inputFor(String labelText) {
        String name = InBrowserSinglton.find(BasicPath.label.withText(labelText)).getAttribute("for");
        return BasicPath.input.that(ElementProperties.hasAttribute("name", name));
    }
}
