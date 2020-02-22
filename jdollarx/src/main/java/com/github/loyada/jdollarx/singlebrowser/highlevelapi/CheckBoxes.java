package com.github.loyada.jdollarx.singlebrowser.highlevelapi;

import com.github.loyada.jdollarx.ElementProperty;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.highlevelapi.CheckBox;

import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;

public final class CheckBoxes {
    private CheckBoxes(){}

    /**
     * input of type "checkbox" with a label
     * @param labelText - the text in the label
     */
    public static CheckBox checkBoxWithLabel(String labelText) {
        return new CheckBox(new InBrowser(driver), labelText);
    }

    /**
     * input of type "checkbox" with the given properties
     * @param props
     */
    public static CheckBox checkBoxWithProperties(ElementProperty... props) {
        return new CheckBox(new InBrowser(driver), props);
    }


}
