package com.github.loyada.jdollarx.singlebrowser.sizing;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.util.Map;

import static com.github.loyada.jdollarx.BasicPath.html;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.*;
import static com.github.loyada.jdollarx.singlebrowser.sizing.SizingUtils.*;

/**
 * An AutoCloseable resizer for the browser.
 * When closing, it reverts the the original state
 */
public class WindowResizer implements AutoCloseable {
    private Map<String, String> originalStyling;
    private Dimension totalOriginalDimensions;

    /**
     * Resize a browser to the requested dimensions.
     * First it changes the window size, and then it updates the size of the html inside it.
     * @param expectedWidth  expected width
     * @param expectedHeight  expected height
     */
    public WindowResizer(int expectedWidth, int expectedHeight) {
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

    /**
     * get visible height of the browser
     * @return height
     */
    public Long getVisibleHeight() {
        return getVisibleDimensions(html).get(HEIGHT);
    }

    /**
     * get visible width of the browser
     * @return width
     */
    public Long getVisibleWidth() {
        return getVisibleDimensions(html).get(WIDTH);
    }

    /**
     * get total scrollable height of the browser
     * @return height
     */
    public Long getTotalHeight() {
        return getScrollableDimensions(html).get(HEIGHT);
    }

    /**
     * get total scrollable width of the browser
     * @return width
     */
    public Long getTotalWidth() {
        return getScrollableDimensions(html).get(WIDTH);
    }

    /**
     * Revert state
     */
    @Override
    public void close()  {
        SizingUtils.setDimensions(html,
                    originalStyling.get(WIDTH), originalStyling.get(HEIGHT));
        driver.manage().window().setSize(totalOriginalDimensions);
    }


}
