package org.lab49.jdollarx;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.function.UnaryOperator;

/**
 * Created by danny on 6/17/2015.
 */
public class Operations {

    public static class KeysSender{
        private final WebDriver driver;
        private final  CharSequence[] charsToSend;

        public KeysSender (WebDriver driver, CharSequence... charsToSend) {
            this.driver = driver;
            this.charsToSend = charsToSend;
        }

        public void toBrowser() {
            preformActions(driver, e -> e.sendKeys(charsToSend));
        }

        public void to(BasicPath path) {
            preformActions(driver, e -> e.sendKeys(InBrowserFinder.find(driver, path), charsToSend));
        }
    };


    public static class KeysDown{
        private final WebDriver driver;
        private final Keys key;

        public KeysDown(WebDriver driver, Keys keysToSend) {
            this.driver = driver;
            this.key = keysToSend;
        }

        public void inBrowser() {
            preformActions(driver, a -> a.keyDown(key));
        }

        public void on(BasicPath path) {
            preformActions(driver, a -> a.keyDown(InBrowserFinder.find(driver, path), key));
        }
    };

    public static class ReleaseKey{
        private final WebDriver driver;
        private final  Keys key;

        public ReleaseKey(WebDriver driver, Keys keysToSend) {
            this.driver = driver;
            this.key = keysToSend;
        }

        public void inBrowser() {
            preformActions(driver, a -> a.keyUp(key));
        }

        public void on(BasicPath path) {
            preformActions(driver, a -> a.keyUp(InBrowserFinder.find(driver, path), key));
        }
    };


    public static class DragAndDrop{
        private final WebDriver driver;
        private final BasicPath path;

        public DragAndDrop(final WebDriver driver, final BasicPath path) {
            this.driver = driver;
            this.path = path;
        }

        public void to(Path target) {
            WebElement to = InBrowserFinder.find(driver, target);
            preformActions(driver, a -> a.clickAndHold(InBrowserFinder.find(driver, path)).moveToElement(to).release(to));
        }

        public void to(Integer x, Integer y) {
            preformActions(driver, a -> a.clickAndHold(InBrowserFinder.find(driver, path)).moveByOffset(x, y).release());
        }
    };

    private static void preformActions(WebDriver driver, UnaryOperator<Actions> func)  {
        final Actions actionBuilder = new Actions(driver);
        func.apply(actionBuilder).build().perform();
    }
}
