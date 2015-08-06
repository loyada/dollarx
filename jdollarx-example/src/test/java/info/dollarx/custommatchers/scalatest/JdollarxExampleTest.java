package info.dollarx.custommatchers.scalatest;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
    public void goToGoogle(){
        driver.get("http://www.google.com");
    }

    @Test
     public void googleForAmazonAndVerifyFirstResult(){
        //Given
        Path searchFormWrapper = element.that(hasId("searchform")).contains(form);
        Path google = input.inside(searchFormWrapper);

        //When
        browser.sendKeys("amazon").to((BasicPath)google);

        //Then
        Path results = div.that(hasId("search"));
            Path resultsLink = anchor.inside(results);
            Path amazonResult = resultsLink.withIndex(0).that(hasTextContaining("amazon.com"));
            assertThat(amazonResult, isPresentIn(browser));
    }

    @Test
    public void googleForAmazonAndFeelingLucky(){
        //Given
        Path searchFormWrapper = element.that(hasId("searchform")).contains(form);
        Path google = input.inside(searchFormWrapper);
        browser.sendKeys("amazon").to((BasicPath)google);

        //When
        Path firstSuggestion = listItem.withIndex(0).inside(form);
        browser.hoverOver(firstSuggestion);
        Path feelingLucky = anchor.inside(firstSuggestion).withTextContaining("feeling lucky");
        browser.clickOn(feelingLucky);

        //Then
        Path amazonMainTitle = title.that(hasTextContaining("amazon")).describedBy("amazon main title");
        assertThat(amazonMainTitle, isPresentIn(browser));
    }

    @Test
    public void googleForAmazonAssertionError1(){
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
        try{
            assertThat(amazonResult, isPresent(1000).timesIn(browser));
        } catch (AssertionError e){
            System.out.println(e);
        }
    }

    @Test
    public void googleForAmazonAssertionError2(){
        //Given
        Path searchFormWrapper = element.that(hasId("searchform")).contains(form);
        Path google = input.inside(searchFormWrapper);

        //When
        browser.sendKeys("amazon").to((BasicPath)google);

        //Then
        Path results = div.that(hasId("search"));
        Path resultsLink = anchor.inside(results);
        Path warcraftResult = resultsLink.withIndex(0).that(hasText("for the hord!"));
        try{
            assertThat(warcraftResult, isPresentIn(browser));
        } catch (AssertionError e){
            System.out.println(e);
        }
    }

    @AfterClass
    public static void teardown(){
        driver.quit();
    }

}
