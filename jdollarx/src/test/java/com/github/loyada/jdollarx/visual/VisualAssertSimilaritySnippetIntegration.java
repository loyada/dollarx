package com.github.loyada.jdollarx.visual;

import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.SingltonBrowserImage;
import com.github.loyada.jdollarx.singlebrowser.sizing.ElementResizer;
import com.github.loyada.jdollarx.singlebrowser.sizing.WindowResizer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Dimension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.logging.Logger;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.BasicPath.anchor;
import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.element;
import static com.github.loyada.jdollarx.BasicPath.header;
import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextContaining;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.captureWindowToFile;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scroll;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scrollElement;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.scrollTo;
import static java.util.Objects.requireNonNull;

public class VisualAssertSimilaritySnippetIntegration {
    static Logger logger = Logger.getLogger(Images.class.getName());
    private static InputStream referenceCodeSnippetImage;
    private static InputStream filterCodeSnippetImage;
    private static Path el;

    private static void load_html_file(String path) {
        URL url = VisualAssertSimilaritySnippetIntegration.class.getClassLoader().getResource("html/" + path);
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
        ClassLoader classLoader = VisualAssertSimilarityIntegration.class.getClassLoader();
    //    referenceCodeSnippetImage = new FileInputStream(requireNonNull(classLoader.getResource("snippet_headless.png")).getFile());
        referenceCodeSnippetImage = new FileInputStream(requireNonNull(classLoader.getResource("snippet_docker_capture_reference_with_small_err.png")).getFile());
        filterCodeSnippetImage = new FileInputStream(requireNonNull(classLoader.getResource("filter-for-snippet.jpg")).getFile());
        load_html_file("paths.html");
        driver.manage().window().setSize(new Dimension(1280,1200));
        scroll().to(header.that(hasAggregatedTextContaining("introduction")));
    }



    @Test(expected = AssertionError.class)
    public void checkSimilarityWithFilterCodeSnippetLargeShiftErr1() throws IOException {
        logger.info("checkSimilarityWithFilterCodeSnippetLargeShiftErr1 - when we have a large shift between the images" +
                "the similarity test does not find it and consequently fails");
        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div
                .withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        Path pre = firstOccurrenceOf(element.inside(div.inside(el)));
        scrollElement(pre).right(50);
        SingltonBrowserImage img =  new SingltonBrowserImage(el);
        img.assertImageIsSimilarToExpectedWithFilter(
                filterCodeSnippetImage,
                referenceCodeSnippetImage,
                10
        );


    }

    @Test
    public void checkSimilarityWithFilterCodeSnippetTinyShift() throws IOException {
        logger.info("checkSimilarityWithFilterCodeSnippetTinyShift - when we have a shift of one pixel, the similarity" +
                "test will finds the right offset");

        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div
                .withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        Path pre = firstOccurrenceOf(element.inside(div.inside(el)));
        scrollElement(pre).right(1);

        SingltonBrowserImage img =  new SingltonBrowserImage(el);
        try (ElementResizer elementResizer = new ElementResizer(el, 660, 248)) {

            img.assertImageIsSimilarToExpectedWithFilter(
                    filterCodeSnippetImage,
                    referenceCodeSnippetImage,
                    50
            );
        }
    }

    @Test
    public void checkSimilarityWithFilterCodeSnippetRemovedCharsSucess() throws IOException {
        // deleted two chars changed a small number of pixels. Not enough
        // to make the image fail on similarity test, when using a permissive threshold of 200
        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div
                .withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        SingltonBrowserImage img =  new SingltonBrowserImage(el);
//        File fileRuster = Files.createTempFile(java.nio.file.Path.of("/tmp"), "image-", ".png").toFile();
//        img.captureToFile(fileRuster);
//        ImageIO.write(img.getFuzzyErrorImage(referenceCodeSnippetImage).get(), "png", fileRuster);
        try (ElementResizer elementResizer = new ElementResizer(el, 660, 248)) {
            img.assertImageIsSimilarToExpectedWithFilter(
                    filterCodeSnippetImage,
                    referenceCodeSnippetImage,
                    50
            );
        }
    }

    @Test(expected = AssertionError.class)
    public void checkSimilarityWithFilterCodeSnippetRemovedCharsErr() throws IOException {
        // Note: Compared to the previous test case, the threshold here is stricter, and
        // causes the assertion to fail.
        // Compare this to checkSimilarityWithoutActiveAreaInCodeSnippetCapitalizedO() below.

        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div
                .withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        SingltonBrowserImage img =  new SingltonBrowserImage(el);

        try (ElementResizer elementResizer = new ElementResizer(el, 660, 248)) {
            img.assertImageIsSimilarToExpectedWithFilter(
                    filterCodeSnippetImage,
                    referenceCodeSnippetImage,
                    1200
            );
        }
    }

    @Test
    public void checkSimilarityWithoutActiveAreaFilteringInCodeSnippetRemovedChars() throws IOException {
        // Note: If we don't filter for only the active area, checking for similarity
        // is more-forgiving/less-accurate, since it takes into account also areas that
        // have no information.
        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div.withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        SingltonBrowserImage img =  new SingltonBrowserImage(el);
      //  File fileRuster = Files.createTempFile(java.nio.file.Path.of("/tmp"), "image-", ".png").toFile();
      //  img.captureToFile(fileRuster);
      //  ImageIO.write(img.getFuzzyErrorImage(referenceCodeSnippetImage).get(), "png", fileRuster);
        try (ElementResizer elementResizer = new ElementResizer(el, 660, 248)) {
            img.assertImageIsSimilarToExpectedWithShift(
                    referenceCodeSnippetImage,
                    80,
                    1
            );
        }
    }

    @Test
    public void checkSimilarityWithoutActiveAreaFilteringInCodeSnippetRemovedCharsErrImage() throws IOException {
        // Note: If we don't filter for only the active area, checking for similarity
        // is more-forgiving/less-accurate, since it takes into account also areas that
        // have no information.
        scrollTo(header.containing(anchor.withText("predefined elements")));
        el = div.withClass("highlight-java")
                .that(hasAggregatedTextContaining("thePasswordInput"));
        SingltonBrowserImage img =  new SingltonBrowserImage(el);
        File fileRuster = Files.createTempFile(java.nio.file.Path.of("/tmp"), "image-", ".png").toFile();
        img.captureToFile(fileRuster);
        ImageIO.write(img.getFuzzyErrorImage(referenceCodeSnippetImage).get(), "png", fileRuster);
    }

    @Test
    public void captureFullWindowScreenshot() throws IOException {
        try (WindowResizer windowResizer = new WindowResizer(1400, 768)) {
            scrollTo(header.containing(anchor.withText("predefined elements")));
            File fileRuster = Files.createTempFile(java.nio.file.Path.of("/tmp"), "image-", ".png").toFile();
            captureWindowToFile(fileRuster);
        }
    }

    @Test
    public void assertFullWindowScreenshot() throws IOException {
        ClassLoader classLoader = VisualAssertSimilarityIntegration.class.getClassLoader();
        InputStream refImage = new FileInputStream(requireNonNull(classLoader.getResource("full-window-screenshot.png")).getFile());

        try (WindowResizer windowResizer = new WindowResizer(1400, 768)) {
            scrollTo(header.containing(anchor.withText("predefined elements")));
            Images.assertScreenIsSimilarToExpected(new InBrowser(driver), refImage, 50);
        }
    }

    @Test
    public void assertFullWindowScreenshotWithFilter() throws IOException {
        ClassLoader classLoader = VisualAssertSimilarityIntegration.class.getClassLoader();
        InputStream refImage = new FileInputStream(requireNonNull(classLoader.getResource("full-window-screenshot.png")).getFile());
        filterCodeSnippetImage = new FileInputStream(requireNonNull(classLoader.getResource("full-window-filter.jpg")).getFile());;

        try (WindowResizer windowResizer = new WindowResizer(1400, 768)) {
            scrollTo(header.containing(anchor.withText("predefined elements")));
            Images.assertWindowIsSimilarToExpectedWithFilter(
                    new InBrowser(driver),
                    filterCodeSnippetImage,
                    refImage,
                    100);
        }
    }

//    @Test
//    public void experiment() throws IOException {
//        ClassLoader classLoader = VisualAssertSimilarityIntegration.class.getClassLoader();
//        InputStream reference = new FileInputStream(requireNonNull(classLoader.getResource("qp-business-errs.png")).getFile());
//        InputStream actual = new FileInputStream(requireNonNull(classLoader.getResource("qp-business-errs-actual.png")).getFile());
//        //BufferedImage referenceImage =  ImageIO.read(reference);
//        //BufferedImage actualImage =  ImageIO.read(actual);
//
//       // ImageComparator.verifyImagesAreSimilar(actualImage, referenceImage, 60);
//        File fileRuster = Files.createTempFile(java.nio.file.Path.of("/tmp"), "image-", ".png").toFile();
//        //  img.captureToFile(fileRuster);
//        ImageIO.write(Images.getFuzzyErrorsImage(reference, actual).get(), "png", fileRuster);
//        System.out.println(fileRuster.getName());
//    }
}
