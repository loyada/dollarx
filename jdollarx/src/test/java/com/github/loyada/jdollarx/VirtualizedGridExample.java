package com.github.loyada.jdollarx;

import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;

import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.*;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.*;


public class VirtualizedGridExample {
      public static void main(String[] argc) throws Operations.OperationFailedException {
          InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver();
          InBrowserSinglton.driver.get("https://www.ag-grid.com/example.php");
          InBrowser browser =  new InBrowser(InBrowserSinglton.driver) ;
          browser.getDriver().manage().timeouts().implicitlyWait(1, TimeUnit.MILLISECONDS);
          Path table = div.withClass("ag-body-viewport");

          InBrowserSinglton.scrollElement(table).rightUntilElementIsPresent(div.that(hasAggregatedTextContaining("$86,416")));
          InBrowserSinglton.scrollElement(table).leftUntilElementIsPresent(div.that(hasAggregatedTextContaining("Tony Smith")));
          InBrowserSinglton.scrollElement(table).downUntilElementIsPresent(div.that(hasAggregatedTextContaining("isabella cage")));
          InBrowserSinglton.driver.quit();
    }
}
