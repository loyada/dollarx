package com.github.loyada.jdollarx;

import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import com.github.loyada.jdollarx.singlebrowser.SingltonBrowserImage;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.github.loyada.jdollarx.BasicPath.image;
import static com.github.loyada.jdollarx.BasicPath.occurrenceNumber;

public class ImageSourceCaptureExample {
    public static void main(String[] argc) throws IOException {
        InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver();
        InBrowserSinglton.driver.get("http://ynet.co.il");
        SingltonBrowserImage imageCapturer = new SingltonBrowserImage(occurrenceNumber(10).of(image));
        File outFile = new File("foo");

        // capture
        imageCapturer.captureImgSourceToFile(outFile);

        // validate
        final InputStream inputStream =
                new DataInputStream(new FileInputStream(new File("foo")));
        imageCapturer.assertImgSourceIsEqualToExpected(inputStream);
        InBrowserSinglton.driver.quit();

    }
}
