package info.dollarx.custommatchers.scalatest;


import info.testtools.jdollarx.Operations;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import info.testtools.jdollarx.InBrowser;
import info.testtools.jdollarx.BasicPath;
import info.testtools.jdollarx.Path;
import org.openqa.selenium.WebDriver;

import static info.testtools.jdollarx.BasicPath.*;
import static info.testtools.jdollarx.ElementProperties.*;
import static info.testtools.jdollarx.custommatchers.CustomMatchers.*;

public class JdollarxExampleTest {

    private static final String driverPath = System.getenv().get("CHROMEDRIVERPATH");
    private static InBrowser browser;
    private static WebDriver driver;

    @BeforeClass
    public static void setup() {
        driver = new DriverSetup(true).createNewDriver("chrome", driverPath);
        browser = new InBrowser(driver);
    }

    @Before
    public void goToGoogle() {
        driver.get("http://www.google.com");
    }

    @Test
    public void googleForAmazonAndVerifyFirstResult() throws Operations.OperationFailedException {
        //Given
        Path searchFormWrapper = element.that(hasId("searchform")).contains(form);
        Path google = input.inside(searchFormWrapper);

        //When
        browser.sendKeys("amazon").to((BasicPath) google);

        //Then
        Path results = div.that(hasId("search"));
        Path resultsLink = anchor.inside(results);
        Path amazonResult = resultsLink.withIndex(0).that(hasTextContaining("amazon.com"));
        assertThat(amazonResult, isPresentIn(browser));
    }

    @Test
    public void showAUsefulExceptionForOperationError() throws Operations.OperationFailedException {
        //Given
        Path searchFormWrapper = element.that(hasId("searchform")).contains(form).describedBy("search form");
        Path warcraft = input.inside(searchFormWrapper).withText("for the horde!");
        try {
            // when
            browser.sendKeys("amazon").to((BasicPath) warcraft);
            //thnen
        } catch (Operations.OperationFailedException e) {
            assertThat(e.getMessage(), equalTo("could not send keys to input, inside (search form), and has the text \"for the horde!\""));
            assertThat(e.getCause().getMessage(), startsWith("could not find input, inside (search form), and has the text \"for the horde!\""));
        }
    }

    @Test
    public void googleForAmazonAndFeelingLucky() throws Operations.OperationFailedException {
        //Given
        Path searchFormWrapper = element.that(hasId("searchform")).contains(form);
        Path google = input.inside(searchFormWrapper);
        browser.sendKeys("amazon").to((BasicPath) google);

        //When
        Path firstSuggestion = listItem.withIndex(0).inside(form);
        browser.hoverOver(firstSuggestion);
        Path feelingLucky = anchor.inside(firstSuggestion).withTextContaining("feeling lucky");
        browser.clickAt(feelingLucky);

        //Then
        Path amazonMainTitle = title.that(hasTextContaining("amazon")).describedBy("amazon main title");
        assertThat(amazonMainTitle, isPresentIn(browser));
    }

    @Test
    public void googleForAmazonAssertionError1() throws Operations.OperationFailedException {
        //Given
        Path searchFormWrapper = element.that(hasId("searchform")).contains(form);
        Path google = input.inside(searchFormWrapper);

        //When
        browser.sendKeys("amazon").to((BasicPath) google);

        //Then
        Path results = div.that(hasId("search"));
        Path resultsLink = anchor.inside(results);
        Path amazonResult = resultsLink.withIndex(0).that(hasTextContaining("amazon.com"));
        assertThat(amazonResult, isPresentIn(browser));
        try {
            assertThat(amazonResult, isPresent(1000).timesIn(browser));
        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }

    @Test
    public void googleForAmazonAssertionError2() throws Operations.OperationFailedException {
        //Given
        Path searchFormWrapper = element.that(hasId("searchform")).contains(form);
        Path google = input.inside(searchFormWrapper);

        //When
        browser.sendKeys("amazon").to((BasicPath) google);

        //Then
        Path results = div.that(hasId("search"));
        Path resultsLink = anchor.inside(results);
        Path warcraftResult = resultsLink.withIndex(0).that(hasText("for the horde!"));
        try {
            assertThat(warcraftResult, isPresentIn(browser));
        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void teardown() {
        driver.quit();
    }

}
