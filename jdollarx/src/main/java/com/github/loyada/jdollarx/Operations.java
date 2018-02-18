package com.github.loyada.jdollarx;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.util.function.UnaryOperator;


public class Operations {

    public static class OperationFailedException extends IOException {
        public OperationFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class KeysSender {
        private final WebDriver driver;
        private final CharSequence[] charsToSend;

        public KeysSender(WebDriver driver, CharSequence... charsToSend) {
            this.driver = driver;
            this.charsToSend = charsToSend;
        }

        public void toBrowser() {
            preformActions(driver, e -> e.sendKeys(charsToSend));
        }

        public void to(Path path) throws OperationFailedException {
            try {
                preformActions(driver, e -> e.sendKeys(InBrowserFinder.find(driver, path), charsToSend));
            } catch (Exception e) {
                throw new OperationFailedException("could not send keys to " + path, e);
            }
        }
    }


    public static class KeysDown {
        private final WebDriver driver;
        private final CharSequence key;

        public KeysDown(WebDriver driver, CharSequence keysToSend) {
            this.driver = driver;
            this.key = keysToSend;
        }

        public void inBrowser() {
            preformActions(driver, a -> a.keyDown(key));
        }

        public void on(Path path) throws OperationFailedException {
            try {
                preformActions(driver, a -> a.keyDown(InBrowserFinder.find(driver, path), key));
            } catch (Exception e) {
                throw new OperationFailedException("could not key down on " + path, e);
            }
        }
    }

    public static class ReleaseKey {
        private final WebDriver driver;
        private final CharSequence key;

        public ReleaseKey(WebDriver driver, CharSequence keysToSend) {
            this.driver = driver;
            this.key = keysToSend;
        }

        public void inBrowser() {
            preformActions(driver, a -> a.keyUp(key));
        }

        public void on(BasicPath path) throws OperationFailedException {
            try {
                preformActions(driver, a -> a.keyUp(InBrowserFinder.find(driver, path), key));
            } catch (Exception e) {
                throw new OperationFailedException("could not release keys on " + path, e);
            }
        }
    }


    public static class Scroll {

        private final WebDriver driver;

        public Scroll(final WebDriver driver) {
            this.driver = driver;
        }

        public void to(Path path) {
            preformActions(driver, a -> a.moveToElement(InBrowserFinder.find(driver, path)));
        }

        private void scrollInternal(Integer x, Integer y) {
            ((JavascriptExecutor) driver).executeScript(String.format("scroll(%d,%d)", x, y));

        }

        public void left(Integer n) {
            scrollInternal(-1 * n, 0);
        }

        public void right(Integer n) {
            scrollInternal(n, 0);
        }

        public void up(Integer n) {
            scrollInternal(0, -1 * n);
        }

        public void down(Integer n) {
            scrollInternal(0, n);
        }

    }


    public static class DragAndDrop {
        private final WebDriver driver;
        private final Path path;

        public DragAndDrop(final WebDriver driver, final Path path) {
            this.driver = driver;
            this.path = path;
        }

        public void to(Path target) throws OperationFailedException {
            try {
                WebElement to = InBrowserFinder.find(driver, target);
                preformActions(driver, a -> a.clickAndHold(InBrowserFinder.find(driver, path)).moveToElement(to).release(to));
            } catch (Exception e) {
                throw new OperationFailedException("could not drag and drop " + path + " to " + target, e);
            }
        }

        public void to(Integer x, Integer y) throws OperationFailedException {
            try {
                preformActions(driver, a -> a.clickAndHold(InBrowserFinder.find(driver, path)).moveByOffset(x, y).release());
            } catch (Exception e) {
                throw new OperationFailedException("could not drag and drop " + path, e);
            }
        }
    }

    private static void preformActions(WebDriver driver, UnaryOperator<Actions> func) {
        final Actions actionBuilder = new Actions(driver);
        func.apply(actionBuilder).build().perform();
    }
}
