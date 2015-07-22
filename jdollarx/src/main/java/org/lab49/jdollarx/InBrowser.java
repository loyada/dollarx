package org.lab49.jdollarx;


import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.function.UnaryOperator;
import static org.lab49.jdollarx.Operations.*;

public class InBrowser {
    private final WebDriver driver;


    public InBrowser(final WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebElement find(final Path el) {
        return InBrowserFinder.find(driver, el);
    }

    public List<WebElement> findAll(final Path el) {
        return InBrowserFinder.findAll(driver, el);

    }

    /////////////// predicates
    public Integer numberOfAppearances(Path el) {
        return InBrowserFinder.findAll(driver, el).size();
    }

    public boolean isPresent(Path el) {
        try {
            find(el);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isEnabled(Path el) {
        try {
            return find(el).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isSelected(Path el) {
        try {
            return find(el).isSelected();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isDisplayed(Path el) {
        try {
            return find(el).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    ////////////////////////////////////////////////////
    //// actions
    public WebElement clickOn(Path el) {
        WebElement found = find(el);
        found.click();
        return found;
    }

    public WebElement clickAt(Path el) {
        WebElement found = find(el);
        preformActions(e -> e.moveToElement(found).click());
        return found;
    }

    public WebElement hoverOver(Path el) {
        final WebElement found = find(el);
        preformActions(e -> e.moveToElement(found));
        return found;
    }

    public WebElement scrollTo(Path el) {
        return hoverOver(el);
    }

    private void preformActions(UnaryOperator<Actions> func) {
        final Actions actionBuilder = new Actions(driver);
        func.apply(actionBuilder).build().perform();
    }

    public void doubleClickOn(Path el) {
        WebElement found = find(el);
        preformActions(e -> e.doubleClick(found));
    }

    public KeysSender sendKeys(CharSequence... charsToSend) {
        return new KeysSender(driver, charsToSend);
    }

    public KeysDown pressKeyDown(Keys thekey) {
        return new KeysDown(driver, thekey);
    }

    public ReleaseKey releaseKey(Keys thekey) {
        return new ReleaseKey(driver, thekey);
    }

    public DragAndDrop dragAndDrop(BasicPath path) {
        return new Operations.DragAndDrop(driver, path);
    }
}
