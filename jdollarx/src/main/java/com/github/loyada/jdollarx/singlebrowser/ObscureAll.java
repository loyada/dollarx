package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.visual.AllElementsObscure;
import com.github.loyada.jdollarx.visual.ElementObscure;

import java.util.List;

/**
 * class that allows to hide elements temporarily.
 * This is useful when doing visual testing, while ignoring elements that are not interesting for the test.
 * For example - testing a chart while ignoring certain labels.
 * This is an Autocloseable: it reverts to the original state when leaving the try{}  block.
 *
 */
public class ObscureAll extends AllElementsObscure implements AutoCloseable {

    /**
     * Make the first element matching the given path temporarily hidden.
     * If the element is not found, it ignores it.
     * @param element the path of the element to obscure
     */
    public ObscureAll(Path element) {
        super(new InBrowser(InBrowserSinglton.driver), element);
    }

    /**
     * Make the elements matching the given paths temporarily hidden.
     * In case there are multiple matches, it will hide the first one.
     * If the element is not found, it ignores it.
     * @param elements the elements to obscure
     */
    public ObscureAll(List<Path> elements) {
        super(new InBrowser(InBrowserSinglton.driver), elements);
    }

    /**
     * Make the elements matching the given paths temporarily hidden.
     * In case there are multiple matches, it will hide the first one.
     * @param elements the elements to obscure
     * @param strict in strict mode, if the element is not found, it throws am exception and stops
     */
    public ObscureAll(List<Path> elements, boolean strict) {
        super(new InBrowser(InBrowserSinglton.driver), elements, strict);
    }
}
