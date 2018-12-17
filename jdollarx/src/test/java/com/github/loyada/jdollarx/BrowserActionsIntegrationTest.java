package com.github.loyada.jdollarx;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BrowserActionsIntegrationTest {
    WebDriver driverMock;
    @Captor
    private ArgumentCaptor<Collection<Sequence>> captor;
    RemoteWebElement webElement, webElement2;
    InBrowser browser;


    @Before
    public void setup() {
        driverMock = mock(ChromeDriver.class);
        webElement = mock(RemoteWebElement.class);
        webElement2 = mock(RemoteWebElement.class);
        when(driverMock.findElement(By.xpath("//" + BasicPath.div.getXPath().get()))).thenReturn(webElement);
        when(driverMock.findElement(By.xpath("//" + BasicPath.span.getXPath().get()))).thenReturn(webElement2);
        browser = new InBrowser(driverMock);
    }

    @Test
    public void scrollToElementTest() {
       browser.scroll().to(BasicPath.div);
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        assertThat(actions.get(0).toJson().get("type"), equalTo("pointer"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>) actions.get(0).toJson().get("actions");
        assertThat(ops.get(0).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(0).get("origin"), is(webElement));
    }

    @Test
    public void scrollDirection() {
        browser.scroll().down(100);
        verify(((JavascriptExecutor) driverMock)).executeScript("scroll(0,100)");
        browser.scroll().up(100);
        verify(((JavascriptExecutor) driverMock)).executeScript("scroll(0,-100)");
        browser.scroll().left(100);
        verify(((JavascriptExecutor) driverMock)).executeScript("scroll(-100,0)");
        browser.scroll().right(100);
        verify(((JavascriptExecutor) driverMock)).executeScript("scroll(100,0)");
    }

    @Test
    public void clickElement() {
        browser.clickAt(BasicPath.div);
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        assertThat(actions.get(0).toJson().get("type"), equalTo("pointer"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>) actions.get(0).toJson().get("actions");
        assertThat(ops.get(0).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(0).get("origin"), is(webElement));
        assertThat(ops.get(1).get("type"), equalTo("pointerDown"));
        assertThat(ops.get(2).get("type"), equalTo("pointerUp"));
    }

    @Test
    public void clickOn() {
        when(webElement.isEnabled()).thenReturn(true);
        when(webElement.isDisplayed()).thenReturn(true);

        browser.clickOn(BasicPath.div);
        verify(webElement).click();
    }

    @Test
    public void doubleclickElement() {
        when(webElement.isEnabled()).thenReturn(true);
        when(webElement.isDisplayed()).thenReturn(true);

        browser.doubleClickOn(BasicPath.div);
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        assertThat(actions.get(0).toJson().get("type"), equalTo("pointer"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>) actions.get(0).toJson().get("actions");
        assertThat(ops.get(0).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(0).get("origin"), is(webElement));
        assertThat(ops.get(1).get("type"), equalTo("pointerDown"));
        assertThat(ops.get(2).get("type"), equalTo("pointerUp"));
        assertThat(ops.get(3).get("type"), equalTo("pointerDown"));
        assertThat(ops.get(4).get("type"), equalTo("pointerUp"));
    }

    @Test
    public void sendKeysToElement() throws Operations.OperationFailedException {
        browser.sendKeys("x", "yz").to(BasicPath.div);
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);

        final int keyboardInd = actions.get(0).toJson().get("type").equals("key") ? 0 : 1;
        List<Map<String, Object>> ops = (List<Map<String, Object>>) actions.get(keyboardInd).toJson().get("actions");
        // first 3 actions are with pointer: move, clickdown, clickup
        List<Map<String, Object>> keysOps = ops.subList(3, ops.size());
        List<Object> keys = keysOps.stream().map(op -> op.get("value")).collect(Collectors.toList());
        assertEquals(keys, Arrays.asList("x", "x", "y", "y", "z","z" ));
        List<Object> opsTypesWithKeys = keysOps.stream().map(op -> op.get("type")).collect(Collectors.toList());
        assertEquals(opsTypesWithKeys, Arrays.asList("keyDown", "keyUp", "keyDown", "keyUp", "keyDown","keyUp" ));
    }

    @Test
    public void sendKeysToBrowser() {
        browser.sendKeys("x", "yz").toBrowser();
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        assertThat(actions.get(0).toJson().get("type"), equalTo("key"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>) actions.get(0).toJson().get("actions");
        List<Object> keys = ops.stream().map(op -> op.get("value")).collect(Collectors.toList());
        assertEquals(keys, Arrays.asList("x", "x", "y", "y", "z","z" ));
    }

    @Test
    public void DragAndDropToElement() throws Operations.OperationFailedException {
        browser.dragAndDrop(BasicPath.div).to(BasicPath.span);
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        Map<String, Object> keysActions = actions.get(0).toJson();
        assertThat(keysActions.get("type"), equalTo("pointer"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>)keysActions.get("actions");
        assertThat(ops.get(0).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(0).get("origin"), is(webElement));
        assertThat(ops.get(1).get("type"), equalTo("pointerDown"));
        assertThat(ops.get(2).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(2).get("origin"), is(webElement2));
        assertThat(ops.get(ops.size()-1).get("type"), equalTo("pointerUp"));
    }

    @Test
    public void DragAndDropToOffset() throws Operations.OperationFailedException {
        browser.dragAndDrop(BasicPath.div).to(10, 10);
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        Map<String, Object> keysActions = actions.get(0).toJson();
        assertThat(keysActions.get("type"), equalTo("pointer"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>)keysActions.get("actions");
        assertThat(ops.get(0).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(0).get("origin"), is(webElement));
        assertThat(ops.get(1).get("type"), equalTo("pointerDown"));
        assertThat(ops.get(2).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(2).get("x"),  is(10));
        assertThat(ops.get(2).get("y"),  is(10));
        assertThat(ops.get(ops.size()-1).get("type"), equalTo("pointerUp"));
    }

    @Test
    public void find() {
        assertThat(browser.find(BasicPath.div), is(webElement));
    }

    @Test
    public void findInsideElement() {
        WebElement el = browser.find(BasicPath.div);
        Path newPath = BasicPath.builder().withUnderlying(el).withXpath("span").build();
        assertThat(newPath.toString(), org.hamcrest.Matchers.startsWith("under reference element Mock for RemoteWebElement"));
        assertThat(newPath.toString(), org.hamcrest.Matchers.endsWith("xpath: \"span\""));
        RemoteWebElement el2 = mock(RemoteWebElement.class);
        when(webElement.findElement(By.xpath("span"))).thenReturn(el2);
        assertThat(browser.find(newPath), is(el2));
    }

    @Test
    public void findAll() {
        List<WebElement> result = Collections.singletonList(webElement);
        when(driverMock.findElements(By.xpath("//" + BasicPath.div.getXPath().get()))).thenReturn(result);

        assertThat(browser.findAll(BasicPath.div), is(equalTo(result)));
    }

    @Test
    public void scroll() {
        browser.scrollTo(BasicPath.div);
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        Map<String, Object> firstActions = actions.get(0).toJson();
        assertThat(firstActions.get("type"), equalTo("pointer"));
        Map<String, Object> op = ((List<Map<String, Object>>)firstActions.get("actions")).get(0);
        assertThat(op.get("type"), equalTo("pointerMove"));
        assertThat(op.get("origin"), is(webElement));
    }

    @Test
    public void hover() {
        browser.hoverOver(BasicPath.div);
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        Map<String, Object> keysActions = actions.get(0).toJson();
        assertThat(keysActions.get("type"), equalTo("pointer"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>)keysActions.get("actions");
        assertThat(ops.get(0).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(0).get("origin"), is(webElement));
    }

    @Test
    public void pressKeyDownInBrowser() {
        browser.pressKeyDown(Keys.SHIFT).inBrowser();
        browser.sendKeys("x").toBrowser();
        verify(((Interactive) driverMock), times(2)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        Map<String, Object> firstActions = actions.get(0).toJson();
        assertThat(firstActions.get("type"), equalTo("key"));
        Map<String, Object> op = ((List<Map<String, Object>>)firstActions.get("actions")).get(0);
        assertThat(op.get("type"), equalTo("keyDown"));
        assertThat(op.get("value"), equalTo(Keys.SHIFT.toString()));
    }

    @Test
    public void releaseKey() {
        browser.releaseKey(Keys.SHIFT).inBrowser();
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);

        assertThat(actions.get(0).toJson().get("type"), equalTo("key"));
        List<Map<String, Object>> keyOps = (List<Map<String, Object>>) actions.get(0).toJson().get("actions");
        Map<String, Object> keyDownOp = keyOps.get(keyOps.size()-1);
        assertThat(keyDownOp.get("type"), equalTo("keyUp"));
        assertThat(keyDownOp.get("value"), equalTo(Keys.SHIFT.toString()));
    }

    @Test
    public void pressKeyDownOnElement() throws Operations.OperationFailedException {
        browser.pressKeyDown(Keys.SHIFT).on(BasicPath.div);
        browser.sendKeys("x").toBrowser();

        verify(((Interactive) driverMock), times(2)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        final int keyBoardIndex =  actions.get(1).toJson().get("type").equals("key") ? 1 : 0 ;
        Map<String, Object> keysActions = actions.get(keyBoardIndex).toJson();
        assertThat(keysActions.get("type"), equalTo("key"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>)keysActions.get("actions");
        // first 3 operation is mouse move, click down, click up
        assertThat(ops.stream().map(op -> op.get("type") ).collect(Collectors.toList()), equalTo(Arrays.asList("pause", "pause", "pause", "keyDown" )));
        assertThat(ops.get(3).get("value"), equalTo(Keys.SHIFT.toString()));
    }

    @Test
    public void releaseKeyOnElement() throws Operations.OperationFailedException {
        browser.releaseKey(Keys.SHIFT).on(BasicPath.div);
        verify(((Interactive) driverMock)).perform(captor.capture());
        List<Sequence> actions = (List<Sequence>)captor.getAllValues().get(0);
        final int index;
        if (actions.get(1).toJson().get("type").equals("key")) {
            index = 1;
        }
        else index = 0;
        assertThat(actions.get(1-index).toJson().get("type"), equalTo("pointer"));
        List<Map<String, Object>> keyOps = (List<Map<String, Object>>) actions.get(index).toJson().get("actions");
        Map<String, Object> keyDownOp = keyOps.get(keyOps.size()-1);
        assertThat(keyDownOp.get("type"), equalTo("keyUp"));
        assertThat(keyDownOp.get("value"), equalTo(Keys.SHIFT.toString()));
    }

    @Test
    public void numberOfAppearances0() {
        when(driverMock.findElement(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.numberOfAppearances(BasicPath.listItem), is(0));
    }

    @Test
    public void numberOfAppearances2() {
        List<WebElement> result = Arrays.asList(webElement, webElement);
        when(driverMock.findElements(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenReturn(result);
        assertThat( browser.numberOfAppearances(BasicPath.listItem), is(2));
    }

    @Test
    public void isPresentFalse() {
        when(driverMock.findElement(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isPresent(BasicPath.listItem), is(false));
    }

    @Test
    public void isPresentTrue() {
        assertThat( browser.isPresent(BasicPath.div), is(true));
    }

    @Test
    public void isDisplayedYes() {
        when(webElement.isDisplayed()).thenReturn(true);
        assertThat( browser.isDisplayed(BasicPath.div), is(true));
    }

    @Test
    public void isDisplayedNo() {
        when(webElement.isDisplayed()).thenReturn(false);
        assertThat( browser.isDisplayed(BasicPath.div), is(false));
    }

    @Test
    public void isDisplayedDoesNotExist() {
        when(driverMock.findElement(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isDisplayed(BasicPath.listItem), is(false));
    }

    @Test
    public void isSelectedYes() {
        when(webElement.isSelected()).thenReturn(true);
        assertThat( browser.isSelected(BasicPath.div), is(true));
    }

    @Test
    public void isSelectedNo() {
        when(webElement.isSelected()).thenReturn(false);
        assertThat( browser.isSelected(BasicPath.div), is(false));
    }

    @Test
    public void isSelectedDoesNotExist() {
        when(driverMock.findElement(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isSelected(BasicPath.listItem), is(false));
    }

    @Test
    public void isEnabledYes() {
        when(webElement.isEnabled()).thenReturn(true);
        assertThat( browser.isEnabled(BasicPath.div), is(true));
    }

    @Test
    public void isEnabledNo() {
        when(webElement.isEnabled()).thenReturn(false);
        assertThat( browser.isEnabled(BasicPath.div), is(false));
    }

    @Test
    public void isEnableDoesNotExist() {
        when(driverMock.findElement(By.xpath("//" + BasicPath.listItem.getXPath().get()))).thenThrow(new NoSuchElementException(""));
        assertThat( browser.isEnabled(BasicPath.listItem), is(false));
    }

}
