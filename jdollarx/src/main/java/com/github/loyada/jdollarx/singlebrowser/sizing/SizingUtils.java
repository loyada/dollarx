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

    private static JavascriptExecutor js = (JavascriptExecutor) driver;

    static void setDimensions(Path path, int width, int height) {
        setDimensions(path, format("%dpx", width), format("%dpx", height));
    }

    static private String correct(String s) {
        return format("'%s'", s);
    }
    static  void setDimensions(Path path, String width, String height) {

        WebElement el = find(path);
        String script = format("arguments[0].style.width=%s;  arguments[0].style.height=%s;", correct(width), correct(height));
        js.executeScript(script, el);
    }

    static Map<String, Long> getVisibleDimensions(Path path) {
        WebElement el = find(path);
        return (Map<String, Long>)js.executeScript("return  { 'height':  arguments[0].clientHeight, 'width':   arguments[0].clientWidth};", el);
    }

    static Map<String, Long> getScrollableDimensions(Path path) {
        WebElement el = find(path);
        return (Map<String, Long>)js.executeScript("return  { 'height':  arguments[0].scrollHeight, 'width':   arguments[0].scrollWidth};", el);
    }

    static Map<String, String> getStylingDimensions(Path path) {
        WebElement el = find(path);
        return (Map<String, String>)js.executeScript("return  { 'height':  arguments[0].style.height, 'width':   arguments[0].style.width};", el);
    }

}