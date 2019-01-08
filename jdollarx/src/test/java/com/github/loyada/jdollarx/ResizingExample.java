package com.github.loyada.jdollarx;

import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import com.github.loyada.jdollarx.singlebrowser.sizing.ElementResizer;
import com.github.loyada.jdollarx.singlebrowser.sizing.WindowResizer;


import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.ElementProperties.hasClass;

public class ResizingExample {
    public static void main(String[] argc) {
        InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver();
        InBrowserSinglton.driver.get("https://www.google.com/maps/place/240+W+98th+St,+New+York,+NY+10025/@40.7959868,-73.9736719,17z/data=!4m5!3m4!1s0x89c2f6289ce044ab:0xb8a705c680aa8b62!8m2!3d40.7959868!4d-73.9714832");
        try (WindowResizer windowResizer = new WindowResizer(1000, 768)) {
            try (ElementResizer elementResizer = new ElementResizer(div.that(hasClass("widget-pane")), 600, 400)) {
                System.out.println(String.format("element total dimensions: %d, %d", elementResizer.getTotalWidth(), elementResizer.getTotalHeight()));
                System.out.println(String.format("element visible dimensions: %d, %d", elementResizer.getVisibleWidth(), elementResizer.getVisibleHeight()));

            }

            System.out.println(String.format("element total dimensions: %d, %d", windowResizer.getTotalWidth(), windowResizer.getTotalHeight()));

        }
        InBrowserSinglton.driver.quit();
    }
}
