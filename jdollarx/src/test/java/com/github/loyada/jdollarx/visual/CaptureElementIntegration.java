package com.github.loyada.jdollarx.visual;

import com.github.loyada.jdollarx.BasicPath;
import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.Images;
import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.singlebrowser.SingltonBrowserImage;
import com.github.loyada.jdollarx.singlebrowser.sizing.WindowResizer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static com.github.loyada.jdollarx.BasicPath.canvas;
import static com.github.loyada.jdollarx.BasicPath.firstOccurrenceOf;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;

public class CaptureElementIntegration {

    @BeforeClass
    public static void setup() {
        driver = DriverSetup.createStandardChromeDriver();
    }


    @Before
    public void refresh() throws InterruptedException {
        driver.navigate().refresh();
    }

    @AfterClass
    public static void teardown(){
        driver.quit();
    }

    @Test
  public  void captureToFileAsCanvasAndStandardRusterAndCompare() throws IOException, InterruptedException, URISyntaxException {
      driver.get("https://zenphoton.com/#AAQAAkACAAEgfwADAfgBhAI4AXz/AAACPAFwAMQA1v8AAAEZAIkCdACM/wAA");
      // wait for the image to stabilize
      Thread.sleep(5000);
      try (WindowResizer windowResizer = new WindowResizer(1600, 1200)) {

          File fileCanvas = Files.createTempFile(Path.of("/tmp"), "canvas-", ".png").toFile();
          SingltonBrowserImage image = new SingltonBrowserImage(canvas);
          image.captureCanvasToFile(fileCanvas);
          System.out.println("captured as canvas");

          File fileRuster = Files.createTempFile(Path.of("/tmp"), "image-", ".png").toFile();
          image.captureToFile(fileRuster);
          System.out.println("captured as standard ruster");

          BufferedImage image1 = ImageIO.read(fileCanvas);
          BufferedImage image2 = ImageIO.read(fileRuster);
          Images.ImageComparator.verifyImagesAreSimilar(image1, image2, 50);
          System.out.println("verified similarity");

          Optional<BufferedImage> errImage = Images.ImageComparator.getErrorImage(image1, image2);
          if (errImage.isEmpty()) {
              System.out.print("no difference found");
          } else {
              File errFile = Files.createTempFile(Path.of("/tmp"), "diff-", ".png").toFile();
              ImageIO.write(errImage.get(), "png", new FileOutputStream(errFile));
              System.out.println("captured as error file");
          }
      }
    }

    @Test
    public  void captureImageSource() throws IOException {
        String url = Objects.requireNonNull(CaptureElementIntegration.class.getClassLoader().getResource("html/index.html")).toString();

        driver.get(url);

        File imageFile = Files.createTempFile(Path.of("/tmp"), "image-", ".png").toFile();
        SingltonBrowserImage image = new SingltonBrowserImage(firstOccurrenceOf(BasicPath.image));
        image.captureImgSourceToFile(imageFile);
        System.out.println("captured image source");

        Images.assertHTMLImgSoureIsEqualToExpected(new InBrowser(driver), firstOccurrenceOf(BasicPath.image),new FileInputStream(imageFile));
        System.out.println("verified similarity");
    }
}