package com.github.loyada.jdollarx;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.loyada.jdollarx.XpathUtils.nOccurances;

/**
 * Internal implementation.
 */
public class InBrowserFinder {

    static WebElement find(WebDriver driver, final Path el) {
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


    static WebElement findPageWithout(WebDriver driver, final Path el) {
        if (!el.getXPath().isPresent()) {
            throw new UnsupportedOperationException("findPageWithout requires a path");
        }
        final String path = el.getXPath().get();

        try {
            if (el.getUnderlyingSource().isPresent()) {
                final WebElement underlying = el.getUnderlyingSource().get();
                return underlying.findElement(By.xpath("//" + PathOperators.not(el).getXPath().get()));
            } else {
                String processedPath = XpathUtils.doesNotExistInEntirePage(path);
                return driver.findElement(By.xpath(processedPath));
            }
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            throw new NoSuchElementException("could not find page without " + el, ex);
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

    public static int countAll(WebDriver driver, final Path el) {
        final Optional<String> path = el.getXPath();
        if (el.getUnderlyingSource().isPresent()) {
            throw new UnsupportedOperationException();
        } else {
            if (path.isPresent()) {
                String processedPath = processedPathForFind(path.get());
                String script = getScriptForCounting(processedPath);
                try {
                    Object res = ((JavascriptExecutor) driver).executeScript(script);
                    return Long.valueOf((long) res).intValue();
                }
                catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw e;
                    }
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    private static String getScriptForCounting(String processedPath) {
        return String.format(
                "return document.evaluate(\"count(%s)\", document, null, XPathResult.NUMBER_TYPE, null).numberValue",
                processedPath);
    }

    /**
     * Extract an attribute from all elements that match the given element.
     * The implementation is optimized.
     *
     * @param driver  the driver
     * @param el the definition of the elements to match
     * @param attribute the attribute name
     * @return List of int/string with all the values of the attribute
     */
    public static List<?> getAttributeOfAll(WebDriver driver, final Path el, String attribute) {
        final Optional<String> path = el.getXPath();
        if (el.getUnderlyingSource().isPresent()) {
            WebElement underlying = el.getUnderlyingSource().get();
            if (path.isPresent()) {
                String pathForAttribute = String.format("%s/@%s", path.get(), attribute);
                String script = getScriptToExtractAttributes(pathForAttribute);
                Object res = ((JavascriptExecutor) driver).executeScript(script);
                return (List<?>)res;
            } else {
                String value = underlying.getAttribute(attribute);
                return Collections.singletonList(value);
            }
        } else {
            if (path.isPresent()) {
                String processedPath = processedPathForFind(path.get());
                String pathForAttribute = String.format("%s/@%s", processedPath, attribute);
                String script = getScriptToExtractAttributes(pathForAttribute);
                Object res = ((JavascriptExecutor) driver).executeScript(script);
                return (List<?>)res;

            } else {
                throw new IllegalArgumentException("webel is empty"); // should never happen
            }
        }
    }

    private static String getScriptToExtractAttributes(String pathForAttribute) {
       return String.format(
               "values = document.evaluate(\"%s\", document, null, XPathResult.ANY_TYPE, null);" +
               "const res = [];" +
               "var latest = values.iterateNext();" +
               "while (latest) {" +
                       "     res.push(latest.value);" +
                       "     latest = values.iterateNext();" +
               "}" +
               "return res;", pathForAttribute);
    }


    public static WebElement findPageWithNumberOfOccurrences(WebDriver driver, final Path el, int numberOfOccurrences) {
        return findPageWithNumberOfOccurrences(driver, el, numberOfOccurrences, RelationOperator.exactly);
    }

    public static WebElement findPageWithNumberOfOccurrences(WebDriver driver, final Path el, int numberOfOccurrences, RelationOperator relationOperator) {
        final Optional<String> path = el.getXPath();
        if (!path.isPresent()) {
            throw new UnsupportedOperationException("findPageWithNumberOfOccurrences requires a path");
        }
        String pathWithNOccurrences =  nOccurances(path.get(), numberOfOccurrences, relationOperator);
        if (el.getUnderlyingSource().isPresent()) {
            WebElement underlying = el.getUnderlyingSource().get();
            return underlying.findElement(By.xpath("." + pathWithNOccurrences));
        } else {
            return driver.findElement(By.xpath("/html" + pathWithNOccurrences));
        }
    }

    private static String processedPathForFind(final String path) {
        if (path.startsWith("not(.//")) {
            return String.format("/html[.%s]", path);
        } else if (path.startsWith("not")) {
            String processedPath = path.replaceFirst("not[(]", "not(.//");
            return String.format("/html[%s]", processedPath);
        } else if (path.startsWith("html")) {
            return String.format(("/%s"), path);
        } else if (path.startsWith("body")) {
            return String.format(("/html/%s"), path);
        } else {
                final String prefix =  (path.startsWith("/") || path.startsWith("(/")) ? "" :
                        (path.startsWith("(")) ? "(//" :
                                "//";
                final int chopn = (path.startsWith("(") && !path.startsWith("(/")) ? 1 : 0;
                return prefix + path.substring(chopn);
        }
    }
}
