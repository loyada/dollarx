package info.testtools.jdollarx;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class InBrowserFinderTest {
    private WebDriver driverMock;
    private WebElement webElement, webElement2;
    InBrowser browser;


    @Before
    public void setup() {
        driverMock = mock(WebDriver.class);
        webElement = mock(WebElement.class);
        webElement2 = mock(WebElement.class);
        browser = new InBrowser(driverMock);
    }

    @Test
    public void findWithUnderlying() {
        when(webElement.findElement(By.xpath(BasicPath.div.getXPath().get()))).thenReturn(webElement2);
        Path path = BasicPath.builder().withUnderlying(webElement).withXpath("div").build();
        assertThat(InBrowserFinder.find(driverMock, path), is(webElement2));
    }

    @Test
    public void findAllUnderlying() {
        List<WebElement> expected = Collections.singletonList(webElement2);
        when(webElement.findElements(By.xpath(BasicPath.div.getXPath().get()))).thenReturn(expected);
        Path path = BasicPath.builder().withUnderlying(webElement).withXpath("div").build();
        assertThat(InBrowserFinder.findAll(driverMock, path), is(equalTo(expected)));
    }

    @Test
    public void findWithout() {
        when(driverMock.findElement(By.xpath("/html[not(.//div)]"))).thenReturn(webElement2);
        assertThat(InBrowserFinder.findPageWithout(driverMock, BasicPath.div), is(equalTo(webElement2)));
    }

    @Test
    public void findWithoutUnderlying() {
        when(webElement.findElement(By.xpath("//*[not(self::div)]"))).thenReturn(webElement2);
        Path path = BasicPath.builder().withUnderlying(webElement).withXpath("div").build();
        assertThat(InBrowserFinder.findPageWithout(driverMock, path), is(webElement2));
    }

    @Test
    public void findNTimes() {
        when(driverMock.findElement(By.xpath("/html[count(//div)=5]"))).thenReturn(webElement2);
        assertThat(InBrowserFinder.findPageWithNumberOfOccurrences(driverMock, BasicPath.div, 5), is(equalTo(webElement2)));
    }

    @Test
    public void findNTimesUnderlying() {
        when(webElement.findElement(By.xpath(".[count(//div)=5]"))).thenReturn(webElement2);
        Path path = BasicPath.builder().withUnderlying(webElement).withXpath("div").build();
        assertThat(InBrowserFinder.findPageWithNumberOfOccurrences(driverMock, path, 5), is(webElement2));
    }
}

