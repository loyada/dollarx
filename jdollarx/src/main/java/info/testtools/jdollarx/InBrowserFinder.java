package info.testtools.jdollarx;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InBrowserFinder {
    static WebElement find( WebDriver driver, final Path el) {
        final Optional<String> path = el.getXPath();
        try {
            if (el.getUnderlyingSource().isPresent()) {
                WebElement underlying = el.getUnderlyingSource().get();
                return (path.isPresent()) ? underlying.findElement(By.xpath(path.get())) : underlying;
            } else {
                if (el.getXPath().isPresent()) {
                    String processedPath = processedPathForFind(path.get());
                    return driver.findElement(By.xpath(processedPath));
                } else {
                    throw new IllegalArgumentException("path is empty"); // should never happen
                }
            }
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            throw new NoSuchElementException("could not find " + el, ex);
        }
    }



    public static List<WebElement> findAll(WebDriver driver, final Path el) {
        final Optional<String> path = el.getXPath();
        if (el.getUnderlyingSource().isPresent()) {
            WebElement underlying = el.getUnderlyingSource().get();
            if (path.isPresent()) {
                return underlying.findElements(By.xpath(path.get()));
            } else {
                return Collections.singletonList(underlying);
            }
        } else {
            if (path.isPresent()) {
                String processedPath = processedPathForFind(path.get());
                return driver.findElements(By.xpath(processedPath));

            } else {
                throw new IllegalArgumentException("webel is empty"); // should never happen
            }
        }
    }

    private static String processedPathForFind(final String path) {
        if (path.startsWith("not(.//")) {
            return String.format("/html[.%s]", path);
        } else if (path.startsWith("not")) {
            String processedPath = path.replaceFirst("not[(]", "not(.//");
            return String.format("/html[%s]", processedPath);
        } else {
            String prefix = (path.startsWith("/")) ? "" : "//";
            return prefix + path;
        }
    }
}
