package com.github.loyada.jdollarxexample;

import com.github.loyada.jdollarx.Operations;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class NoDollarxExampleTest {
    private static final String driverPath = System.getenv().get("CHROMEDRIVERPATH");
    private static WebDriver driver;

    @BeforeClass
    public static void setup() {
        driver = new DriverSetup(true).createNewDriver("chrome", driverPath);
    }

    @Before
    public void goToGoogle() {
        driver.get("http://www.google.com");
    }

    @Test
    public void googleForAmazonAndVerifyFirstResult() throws Operations.OperationFailedException {
        //Given
        WebElement google = driver.findElement(By.xpath("//*[(@id='searchform' and descendant::form)]//input"));

        //When
        Actions actionBuilder = new Actions(driver);
        actionBuilder.sendKeys(google, "amazon").build().perform();

        //Then
        List<WebElement> resultsLinks = driver.findElements(By.xpath("//div[@id='search']//a"));
        WebElement firstSearchResult = resultsLinks.get(0);
        firstSearchResult.findElement(By.xpath("//*[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') ='amazon']"));
    }

    @AfterClass
    public static void teardown() {
        driver.quit();
    }
}
