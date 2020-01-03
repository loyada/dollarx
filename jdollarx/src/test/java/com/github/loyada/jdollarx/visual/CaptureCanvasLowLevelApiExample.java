package com.github.loyada.jdollarx.visual;

import com.github.loyada.jdollarx.BasicPath;
import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;

public class CaptureCanvasLowLevelApiExample {

  public static void main(String[] argc) throws IOException {
    final int startOfDataInDataURL = "data:image/png:base64,".length();
    InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver();
    InBrowserSinglton.driver.get("https://zenphoton.com/#AAQAAkACAAEgfwADAfgBhAI4AXz/AAACPAFwAMQA1v8AAAEZAIkCdACM/wAA");
    Path canvas = BasicPath.customElement("canvas");
    JavascriptExecutor js = (JavascriptExecutor) InBrowserSinglton.driver;
    WebElement canvasEl = find(canvas);

    String imageEncondedData = (String)js.executeScript("return arguments[0].toDataURL('image/png').substring(arguments[1]);", canvasEl, startOfDataInDataURL);

    byte[]  decodedImage = Base64.getDecoder().decode(imageEncondedData);
    ByteArrayInputStream bis = new ByteArrayInputStream(decodedImage);

    BufferedImage image = ImageIO.read(bis);
    bis.close();

    File outputfile = new File("saved.png");
    InBrowserSinglton.driver.quit();
    ImageIO.write(image, "png", outputfile);
    Icon icon = new ImageIcon(image);
    JLabel label = new JLabel(icon);

    final JFrame f = new JFrame("ImageIconExample");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(label);
    f.pack();
    SwingUtilities.invokeLater(new Runnable(){
      public void run() {
        f.setLocationRelativeTo(null);
        f.setVisible(true);
      }
    });
  }
}
