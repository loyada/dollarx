package com.github.loyada.jdollarx;

import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import com.github.loyada.jdollarx.singlebrowser.SingltonBrowserImage;

import java.io.IOException;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;

public class CaptureElementExample {
  public static void main(String[] argc) throws IOException, InterruptedException {
    InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver();
    InBrowserSinglton.driver.get("http://localhost/contacts/1");
    find(div.withClass("gmnoprint"));
    Thread.sleep(5000);
 //   File file = new File("sample1.png");
    Path map = div.withClass("gm-style");
    SingltonBrowserImage mapImage = new SingltonBrowserImage(map);
 //   Images.captureToFile(new InBrowser(InBrowserSinglton.driver), div.withClass("gm-style"), file);
    mapImage.show();
  //  mapImage.assertImageIsSimilarToExpected(new File("sample1.png"), 50);
 //   mapImage.assertImageIsEqualToExpected(new File("sample1.png"));

    //   Images.captureToFile(new InBrowser(InBrowserSinglton.driver), div.withClass("gm-style"), new File("sample1.png"));
   // Images.show(new InBrowser(InBrowserSinglton.driver), div.withClass("gm-style"));
  }
}