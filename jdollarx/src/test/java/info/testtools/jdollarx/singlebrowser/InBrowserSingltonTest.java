package info.testtools.jdollarx.singlebrowser;

import info.testtools.jdollarx.BasicPath;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class InBrowserSingltonTest {
    WebDriver driverMock;
    WebElement webElement;

    @Before
    public void setup() {
        driverMock = mock(WebDriver.class);
        InBrowserSinglton.driver = driverMock;
        webElement = mock(WebElement.class);

        when(driverMock.findElement(By.xpath("//div"))).thenReturn(webElement);
        when(driverMock.findElements(By.xpath("//div"))).thenReturn(Arrays.asList(webElement, webElement));

    }

    @Test
    public void basicFindElement() {
        assertThat(InBrowserSinglton.find(BasicPath.div), is(equalTo(webElement)));
    }

    @Test
    public void basicFindElements() {
        assertThat(InBrowserSinglton.findAll(BasicPath.div), is(equalTo(Arrays.asList(webElement, webElement))));
    }

    @Test
    public void numberOfAppearancesTest() {
        assertThat(InBrowserSinglton.numberOfAppearances(BasicPath.div), is(2));
    }

    @Test
    public void isPresentTest() {
        assertThat(InBrowserSinglton.isPresent(BasicPath.div), is(equalTo(true)));
    }

    @Test
    public void clickTest() {
        InBrowserSinglton.clickOn(BasicPath.div);
        verify(webElement).click();
    }

    @Test
    public void clickAtTest() {
        InBrowserSinglton.clickOn(BasicPath.div);
        verify(webElement).click();
    }

    @Test
    public void isSelectedTest() {
        when(webElement.isSelected()).thenReturn(true);
        assertThat(InBrowserSinglton.isSelected(BasicPath.div), is(equalTo(true)));
        when(webElement.isSelected()).thenReturn(false);
        assertThat(InBrowserSinglton.isSelected(BasicPath.div), is(equalTo(false)));
    }

    @Test
    public void isDisplayedTest() {
        when(webElement.isDisplayed()).thenReturn(true);
        assertThat(InBrowserSinglton.isDisplayed(BasicPath.div), is(equalTo(true)));
        when(webElement.isDisplayed()).thenReturn(false);
        assertThat(InBrowserSinglton.isDisplayed(BasicPath.div), is(equalTo(false)));
    }

    @Test
    public void isEnabledTest() {
        when(webElement.isEnabled()).thenReturn(true);
        assertThat(InBrowserSinglton.isEnabled(BasicPath.div), is(equalTo(true)));
        when(webElement.isEnabled()).thenReturn(false);
        assertThat(InBrowserSinglton.isEnabled(BasicPath.div), is(equalTo(false)));
    }
}
