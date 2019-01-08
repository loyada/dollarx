package com.github.loyada.jdollarx.singlebrowser.sizing;

import com.github.loyada.jdollarx.Path;

import java.util.Map;

import static com.github.loyada.jdollarx.singlebrowser.sizing.SizingUtils.*;

public class ElementResizer implements AutoCloseable {
    private Path path;
    private Map<String, String> originalStylingDimensions;


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

    public Long getVisibleHeight() {
        return getVisibleDimensions(path).get(HEIGHT);
    }

    public Long getVisibleWidth() {
        return getVisibleDimensions(path).get(WIDTH);
    }

    public Long getTotalHeight() {
        return getScrollableDimensions(path).get(HEIGHT);
    }

    public Long getTotalWidth() {
        return getScrollableDimensions(path).get(WIDTH);
    }

    @Override
    public void close() {
        if (originalStylingDimensions!=null) {
            setDimensions(path, originalStylingDimensions.get(WIDTH), originalStylingDimensions.get(HEIGHT));
        }
    }
}
