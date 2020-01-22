package com.github.loyada.jdollarx.singlebrowser.sizing;

import com.github.loyada.jdollarx.Path;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.Map;

import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;
import static java.lang.String.format;

class SizingUtils {
    static final String HEIGHT = "height";
    static final String WIDTH = "width";

    /*
      NOTE: do not refactor the assignment to js (i.e. JavascriptExecutor js = (JavascriptExecutor) driver;) to be static. This will break tests.
      The reason is that if there are 2 tests being ran serially, the first one quits the driver and the second one
      starts a new driver. But if the assignment to the "js" variable is done only once, it will keep referring to the dead driver, and the
      operations done on it will fail.
     */

    static void setDimensions(Path path, int width, int height) {
        setDimensions(path, format("%dpx", width), format("%dpx", height));
    }

    static private String correct(String s) {
        return format("'%s'", s);
    }
    static  void setDimensions(Path path, String width, String height) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement el = find(path);
        String script = format("arguments[0].style.width=%s;  arguments[0].style.height=%s;", correct(width), correct(height));
        js.executeScript(script, el);
    }

    static Map<String, Long> getVisibleDimensions(Path path) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement el = find(path);
        return castToMap(js.executeScript("return  { 'height':  arguments[0].clientHeight, 'width':   arguments[0].clientWidth};", el));
    }


    static Map<String, Long> getScrollableDimensions(Path path) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement el = find(path);
        return castToMap(js.executeScript("return  { 'height':  arguments[0].scrollHeight, 'width':   arguments[0].scrollWidth};", el));
    }

    static Map<String, String> getStylingDimensions(Path path) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement el = find(path);
        return castToMap(js.executeScript("return  { 'height':  arguments[0].style.height, 'width':   arguments[0].style.width};", el));
    }

    @SuppressWarnings("unchecked")
    private static <T> Map<String, T> castToMap(Object o) {
        return (Map<String, T>)o;
    }

}