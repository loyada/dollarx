package com.github.loyada.jdollarx.singlebrowser.highlevelapi;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;

import static com.github.loyada.jdollarx.BasicPath.*;

public final class Inputs {
    private Inputs() {}

    public static Path inputForLabel(String labelText) {
        return com.github.loyada.jdollarx.highlevelapi.Inputs.inputForLabel(new InBrowser(InBrowserSinglton.driver), labelText);
    }

    public static Path inputFollowedByUnlabeledText(String text) {
        return input.immediatelyBeforeSibling(textNode(text));
    }
}
