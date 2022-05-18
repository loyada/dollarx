package com.github.loyada.jdollarx.visual;

import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.SingltonBrowserImage;
import com.github.loyada.jdollarx.singlebrowser.sizing.ElementResizer;
import com.github.loyada.jdollarx.singlebrowser.sizing.WindowResizer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.github.loyada.jdollarx.BasicPath.paragraph;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;
import static com.github.loyada.jdollarx.visual.VisualAssertSimilarityIntegration.load_html_file;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

import static org.hamcrest.MatcherAssert.assertThat;

public class ResizingIntegration {
    private static final Path myElement = paragraph;

    @BeforeClass
    public static void setup() {
        driver = DriverSetup.createStandardChromeDriver();
        load_html_file("search.html");
    }


    @Before
    public void refresh() throws InterruptedException {
        driver.navigate().refresh();
        find(myElement);
    }


    @Test
    public  void resizeWindowAndElementAndVerifyDimensions() throws IOException {
        try (WindowResizer windowResizer = new WindowResizer(1400, 1000)) {
            assertThat(windowResizer.getTotalHeight(), equalTo( 1000L));
            assertThat(windowResizer.getTotalWidth(), lessThanOrEqualTo(1420L));
            try (ElementResizer elementResizer = new ElementResizer(myElement, 600, 400)) {
                assertThat(elementResizer.getVisibleHeight(), equalTo(400L));
                assertThat(elementResizer.getVisibleWidth(), equalTo(600L));
                new SingltonBrowserImage(myElement).captureToFile(new File("tmpimge.png"));
            }
            assertThat(windowResizer.getTotalHeight(), equalTo( 1000L));
            assertThat(windowResizer.getTotalWidth(), lessThanOrEqualTo(1420L));
        }
    }

    @Test
    public  void resizeWindowAndElementSaveAndVerifyDimensions() throws IOException, InterruptedException {
        try (WindowResizer windowResizer = new WindowResizer(1400, 768)) {
            // The sleep in the next line is  a hack, since it takes time for the site to adjust to new layout.
            // I do it only because this is not a "real" test.
            Thread.sleep(1000);
            try (ElementResizer elementResizer = new ElementResizer(myElement, 700, 500)) {
                new SingltonBrowserImage(myElement).captureToFile(new File("tmpimge.png"));
            }
            BufferedImage img =  ImageIO.read(new FileInputStream(new File("tmpimge.png")));
            assertThat(img.getHeight(), equalTo(500));
            assertThat(img.getWidth(), equalTo(700));
        }
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();;
    }
}
