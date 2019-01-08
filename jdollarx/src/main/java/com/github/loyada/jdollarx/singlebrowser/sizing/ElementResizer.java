package com.github.loyada.jdollarx.singlebrowser.sizing;

import com.github.loyada.jdollarx.Path;

import java.util.Map;

import static com.github.loyada.jdollarx.singlebrowser.sizing.SizingUtils.*;

/**
 * An AutoCloseable of a resizer for a Path element.
 * When closing, it reverts the the original state
 */
public class ElementResizer implements AutoCloseable {
    private Path path;
    private Map<String, String> originalStylingDimensions;


    /**
     * Resize an element in the browser
     * @param path The element to resize
     * @param expectedWidth expected width
     * @param expectedHeight expected height
     */
    public ElementResizer(Path path, int expectedWidth, int expectedHeight) {
        this.path = path;
        Map<String, Long> originalDimensions = getScrollableDimensions(path);
        Long originalHeight = originalDimensions.get(HEIGHT);
        Long originalWidth = originalDimensions.get(WIDTH);

        if (originalWidth != expectedWidth || originalHeight != expectedHeight) {
            originalStylingDimensions = getStylingDimensions(path);
            setDimensions(path, expectedWidth, expectedHeight);
        }
    }

    /**
     * get visible height of the element
     * @return height
     */
    public Long getVisibleHeight() {
        return getVisibleDimensions(path).get(HEIGHT);
    }

    /**
     * get visible width of the element
     * @return width
     */
    public Long getVisibleWidth() {
        return getVisibleDimensions(path).get(WIDTH);
    }

    /**
     * get total scrollable height of the element
     * @return height
     */
    public Long getTotalHeight() {
        return getScrollableDimensions(path).get(HEIGHT);
    }

    /**
     * get total scrollable width of the element
     * @return width
     */
    public Long getTotalWidth() {
        return getScrollableDimensions(path).get(WIDTH);
    }

    /**
     * Revert state
     */
    @Override
    public void close() {
        if (originalStylingDimensions!=null) {
            setDimensions(path, originalStylingDimensions.get(WIDTH), originalStylingDimensions.get(HEIGHT));
        }
    }
}
