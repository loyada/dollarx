package com.github.loyada.jdollarx.visual;

import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.HighLevelPathsIntegration;
import com.github.loyada.jdollarx.singlebrowser.SingltonBrowserImage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Dimension;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.github.loyada.jdollarx.BasicPath.anchor;
import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.element;
import static com.github.loyada.jdollarx.BasicPath.firstOccurrenceOf;
import static com.github.loyada.jdollarx.BasicPath.header;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextContaining;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scroll;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scrollElement;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scrollTo;
import static java.util.Objects.requireNonNull;

public class visualAssertSimilaritySnippetIntegration {
    private static InputStream referenceCodeSnippetImage;
    private static InputStream filterCodeSnippetImage;
    private static Path el;

    private static void load_html_file(String path) {
        URL url = HighLevelPathsIntegration.class.getClassLoader().getResource("html/" + path);
        assert url != null;
        driver.get(url.toString());
    }

    @BeforeClass
    public static void setupAll() {
    }

    @After
    public void teardown(){
        driver.quit();
    }


    @Before
    public void setup() throws FileNotFoundException {
        driver = DriverSetup.createStandardChromeDriver();
        ClassLoader classLoader = visualAssertSimilarityIntegration.class.getClassLoader();
        referenceCodeSnippetImage = new FileInputStream(requireNonNull(classLoader.getResource("snippet.png")).getFile());
        filterCodeSnippetImage = new FileInputStream(requireNonNull(classLoader.getResource("filter-for-snippet.jpg")).getFile());
        load_html_file("paths.html");
        driver.manage().window().setSize(new Dimension(1280,1200));
        scroll().to(header.that(hasAggregatedTextContaining("introduction")));
    }



    @Test(expected = AssertionError.class)
    public void checkSimilarityWithFilterCodeSnippetErr1() throws IOException {
        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div
                .withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        Path pre = firstOccurrenceOf(element.inside(div.inside(el)));
        scrollElement(pre).right(100);
        SingltonBrowserImage img =  new SingltonBrowserImage(el);
        img.assertImageIsSimilarToExpectedWithFilter(
                filterCodeSnippetImage,
                referenceCodeSnippetImage,
                5
        );


    }

    @Test
    public void checkSimilarityWithFilterCodeSnippetSuccess1() throws IOException {
        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div
                .withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        Path pre = firstOccurrenceOf(element.inside(div.inside(el)));
        scrollElement(pre).right(1);

        SingltonBrowserImage img =  new SingltonBrowserImage(el);
        img.assertImageIsSimilarToExpectedWithFilter(
                filterCodeSnippetImage,
                referenceCodeSnippetImage,
                5
        );
    }

    @Test
    public void checkSimilarityWithFilterCodeSnippetCapitalizedOSucess() throws IOException {
        // Note: Changing "o" to "O" changed a small number of pixels. Not enough
        // to make the image fail on similarity test.
        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div
                .withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        SingltonBrowserImage img =  new SingltonBrowserImage(el);
        img.assertImageIsSimilarToExpectedWithFilter(
                filterCodeSnippetImage,
                referenceCodeSnippetImage,
                1000
        );
    }

    @Test(expected = AssertionError.class)
    public void checkSimilarityWithFilterCodeSnippetCapitalizedOErr() throws IOException {
        // Note: Compared to the previous test case, the threshold here is stricter, and
        // causes the assertion to fail.
        // Compare this to checkSimilarityWithoutActiveAreaInCodeSnippetCapitalizedO() below.

        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div
                .withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        SingltonBrowserImage img =  new SingltonBrowserImage(el);

        img.assertImageIsSimilarToExpectedWithFilter(
                filterCodeSnippetImage,
                referenceCodeSnippetImage,
                2500
        );
    }

    @Test
    public void checkSimilarityWithoutActiveAreaFilteringInCodeSnippetCapitalizedO() throws IOException {
        // Note: If we don't filter for only the active area, checking for similarity
        // is more-forgiving/less-accurate, since it takes into account also areas that
        // have no information.
        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div
                .withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        SingltonBrowserImage img =  new SingltonBrowserImage(el);
            img.assertImageIsSimilarToExpectedWithShift(
                    referenceCodeSnippetImage,
                    2500,
                    2
            );
    }
}
