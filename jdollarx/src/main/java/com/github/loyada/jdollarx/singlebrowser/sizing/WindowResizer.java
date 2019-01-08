package com.github.loyada.jdollarx.singlebrowser.sizing;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.util.Map;

import static com.github.loyada.jdollarx.BasicPath.html;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.*;
import static com.github.loyada.jdollarx.singlebrowser.sizing.SizingUtils.*;

public class WindowResizer implements AutoCloseable {
    private Map<String, String> originalStyling;
    private Dimension totalOriginalDimensions;

    public WindowResizer(int expectedHeight, int expectedWidth) {
        Map<String, Long> dimensions = getVisibleDimensions(html);
        Long originalHeight = dimensions.get(HEIGHT);
        Long originalWidth = dimensions.get(WIDTH);
        originalStyling = SizingUtils.getStylingDimensions(html);

        totalOriginalDimensions  = driver.manage().window().getSize();
        int heightOverhead = totalOriginalDimensions.getHeight() - originalHeight.intValue();
        int widthOverhead = totalOriginalDimensions.getWidth() - originalWidth.intValue();

        driver.manage().window().setPosition(new Point(1,1));
        driver.manage().window().setSize(new Dimension(expectedWidth + widthOverhead, expectedHeight + heightOverhead));
        SizingUtils.setDimensions(html, expectedWidth, expectedHeight);
    }

    public Long getVisibleHeight() {
        return getVisibleDimensions(html).get(HEIGHT);
    }

    public Long getVisibleWidth() {
        return getVisibleDimensions(html).get(WIDTH);
    }

    public Long getTotalHeight() {
        return getScrollableDimensions(html).get(HEIGHT);
    }

    public Long getTotalWidth() {
        return getScrollableDimensions(html).get(WIDTH);
    }

    @Override
    public void close()  {
        SizingUtils.setDimensions(html,
                    originalStyling.get(WIDTH), originalStyling.get(HEIGHT));
        driver.manage().window().setSize(totalOriginalDimensions);
    }


}
