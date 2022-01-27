package com.github.loyada.jdollarx.visual;

import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.HighLevelPathsIntegration;
import com.github.loyada.jdollarx.singlebrowser.SingltonBrowserImage;
import com.github.loyada.jdollarx.singlebrowser.sizing.ElementResizer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Dimension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.header;
import static com.github.loyada.jdollarx.BasicPath.paragraph;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextContaining;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scroll;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scrollTo;
import static java.util.Objects.requireNonNull;

public class visualAssertSimilarityIntegration {
    private static InputStream referenceChartImage;
    private static InputStream filterChartImage;

    private static Path el;

    private static void load_html_file(String path) {
        URL url = HighLevelPathsIntegration.class.getClassLoader().getResource("html/" + path);
        assert url != null;
        driver.get(url.toString());
    }

    @BeforeClass
    public static void setupAll() {
        driver = DriverSetup.createStandardChromeDriver();
    }

    @AfterClass
    public static void teardown(){
        driver.quit();
    }


    @Before
    public void setup() throws FileNotFoundException {
        ClassLoader classLoader = visualAssertSimilarityIntegration.class.getClassLoader();
    //    referenceChartImage = new FileInputStream(requireNonNull(classLoader.getResource("chart-ref.png")).getFile());
        referenceChartImage = new FileInputStream(requireNonNull(classLoader.getResource("chart-1-edited.png")).getFile());
        filterChartImage = new FileInputStream(requireNonNull(classLoader.getResource("filter-for-chart.jpg")).getFile());
        load_html_file("paths.html");
        driver.manage().window().setSize(new Dimension(1920,1400));
        scroll().to(header.that(hasAggregatedTextContaining("introduction")));
    }

    @Test
    public void checkSimilarityWithFilterRoundingErrShift() throws IOException {
        el = div.withClass("my-chart");
        scrollTo(paragraph.withTextContaining("Logical operations on properties"));

        try (ElementResizer elementResizer = new ElementResizer(el, 805, 409)) {
            SingltonBrowserImage img = new SingltonBrowserImage(el);
            img.captureToFile(new File("chart-ref.png"));
            img.assertImageIsSimilarToExpectedWithFilter(filterChartImage, referenceChartImage, 1000);
        }
    }

    @Test
    public void checkSimilarityWithFilterSuccess() throws IOException {
        el = div.withClass("my-chart");
        scrollTo(paragraph.withTextContaining("Properties related to text under"));

        try (ElementResizer elementResizer = new ElementResizer(el, 805, 409)) {
            SingltonBrowserImage img = new SingltonBrowserImage(el);
            img.assertImageIsEqualToExpectedWithShiftAndCrop(referenceChartImage, 1);
        }
    }


}
