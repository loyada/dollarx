package com.github.loyada.jdollarx;

import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import com.github.loyada.jdollarx.singlebrowser.SingltonBrowserImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.github.loyada.jdollarx.BasicPath.div;

public class ErrorImageExample {
  public static void main(String[] argc) throws IOException {
    InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver();
    InBrowserSinglton.driver.get("http://ynet.co.il");
    SingltonBrowserImage image = new SingltonBrowserImage(div.withClass("top-story-media"));
    // image.captureToFile(new File("test.png"));
    image.getErrorImage(new FileInputStream(new File("test.png"))).ifPresent(Images::showImage);
       InBrowserSinglton.driver.quit();
  }
}
