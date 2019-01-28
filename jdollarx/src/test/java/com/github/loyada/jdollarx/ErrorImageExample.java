package com.github.loyada.jdollarx;

import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;

import java.io.File;
import java.io.IOException;

import static com.github.loyada.jdollarx.BasicPath.image;
import static com.github.loyada.jdollarx.BasicPath.occurrenceNumber;

public class ErrorImageExample {
  public static void main(String[] argc) throws IOException {
    InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver();
    InBrowserSinglton.driver.get("http://ynet.co.il");
    Images.captureImgSrcToFile(new InBrowser(InBrowserSinglton.driver), occurrenceNumber(10).of(image), new File("foo"));
       InBrowserSinglton.driver.quit();

  }
}
