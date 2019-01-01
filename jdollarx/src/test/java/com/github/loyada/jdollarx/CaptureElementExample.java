package com.github.loyada.jdollarx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CaptureElementExample {
  public static void main(String[] argc) throws IOException, InterruptedException {
  //  InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver();
  //  InBrowserSinglton.driver.get("https://zenphoton.com/#AAQAAkACAAEgfwADAfgBhAI4AXz/AAACPAFwAMQA1v8AAAEZAIkCdACM/wAA");
  //  Thread.sleep(5000);
    File file = new File("sample1.png");
  //  SingltonBrowserImage image = new SingltonBrowserImage(canvas);
  //  image.captureCanvasToFile(file);
    BufferedImage expectedImage =  ImageIO.read(file);
    BufferedImage shiftedImage = expectedImage.getSubimage(20,30,
            expectedImage.getWidth()-30, expectedImage.getHeight() -50);
    Images.ImageComparator.verifyImagesAreShifted(shiftedImage, expectedImage, 50);


    //   Images.captureToFile(new InBrowser(InBrowserSinglton.driver), div.withClass("gm-style"), file);
  //  mapImage.assertImageIsSimilarToExpected(new File("sample1.png"), 50);
 //   mapImage.assertImageIsEqualToExpected(new File("sample1.png"));

    //   Images.captureToFile(new InBrowser(InBrowserSinglton.driver), div.withClass("gm-style"), new File("sample1.png"));
   // Images.show(new InBrowser(InBrowserSinglton.driver), div.withClass("gm-style"));
  }
}