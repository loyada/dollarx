package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.Operations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.loyada.jdollarx.singlebrowser.SingleBrowserPath.div;
import static com.github.loyada.jdollarx.singlebrowser.SingleBrowserPath.span;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SingleBrowserPathIntegrationTest {
    WebDriver driverMock;

    RemoteWebElement webElement, webElement2;

    @Captor
    private ArgumentCaptor<Collection<Sequence>> captor;

    @Before
    public void setup() {
        driverMock = mock(ChromeDriver.class);
        InBrowserSinglton.driver = driverMock;
        webElement = mock(RemoteWebElement.class);
        webElement2 = mock(RemoteWebElement.class);

        when(driverMock.findElement(By.xpath("//" + div.getXPath().get()))).thenReturn(webElement);
        when(driverMock.findElement(By.xpath("//" + span.getXPath().get()))).thenReturn(webElement2);
    }

    @Test
    public void clickElement() {
        div.click();
        verify(((Interactive) driverMock)).perform(captor.capture());
        Object[] actions = captor.getAllValues().get(0).toArray();
        Sequence firstSequence = (Sequence) actions[0];
        assertThat(firstSequence.toJson().get("type"), equalTo("pointer"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>) firstSequence.toJson().get("actions");
        assertThat(ops.get(0).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(0).get("origin"), is(webElement));
        assertThat(ops.get(1).get("type"), equalTo("pointerDown"));
        assertThat(ops.get(2).get("type"), equalTo("pointerUp"));
    }

    @Test
    public void doubleclickElement() {
        when(webElement.isDisplayed()).thenReturn(true);
        when(webElement.isEnabled()).thenReturn(true);

        div.doubleClick();
        verify(((Interactive) driverMock)).perform(captor.capture());
        Object[] actions = captor.getAllValues().get(0).toArray();
        Sequence firstSequence = (Sequence) actions[0];
        assertThat(firstSequence.toJson().get("type"), equalTo("pointer"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>) firstSequence.toJson().get("actions");
        assertThat(ops.get(0).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(0).get("origin"), is(webElement));
        assertThat(ops.get(1).get("type"), equalTo("pointerDown"));
        assertThat(ops.get(2).get("type"), equalTo("pointerUp"));
        assertThat(ops.get(3).get("type"), equalTo("pointerDown"));
        assertThat(ops.get(4).get("type"), equalTo("pointerUp"));
    }

    @Test
    public void sendKeysToElement() throws Operations.OperationFailedException {
        div.sendKeys("abc", "d");
        verify(((Interactive) driverMock)).perform(captor.capture());
        Object[] actions = captor.getAllValues().get(0).toArray();
        Sequence firstSequence = (Sequence) actions[0];
        final int keyboardInd = firstSequence.toJson().get("type").equals("key") ? 0 : 1;
        List<Map<String, Object>> ops = (List<Map<String, Object>>) ((Sequence)actions[keyboardInd]).toJson().get("actions");
        // first 3 actions are with pointer: move, clickdown, clickup
        List<Map<String, Object>> keysOps = ops.subList(3, ops.size());
        List<Object> keys = keysOps.stream().map(op -> op.get("value")).collect(Collectors.toList());
        assertEquals(keys, Arrays.asList("a", "a", "b", "b", "c","c", "d", "d" ));
        List<Object> opsTypesWithKeys = keysOps.stream().map(op -> op.get("type")).collect(Collectors.toList());
        assertEquals(opsTypesWithKeys, Arrays.asList("keyDown", "keyUp", "keyDown", "keyUp", "keyDown","keyUp", "keyDown","keyUp"  ));

    }

    @Test
    public void DragAndDropToElement() throws Operations.OperationFailedException {
        div.dragAndDrop().to(span);
        verify(((Interactive) driverMock)).perform(captor.capture());
        Object[] actions = captor.getAllValues().get(0).toArray();
        Sequence firstSequence = (Sequence) actions[0];
        Map<String, Object> keysActions = firstSequence.toJson();
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
        div.dragAndDrop().to(10, 10);
        verify(((Interactive) driverMock)).perform(captor.capture());
        Object[] actions = captor.getAllValues().get(0).toArray();
        Sequence firstSequence = (Sequence) actions[0];
        Map<String, Object> keysActions = firstSequence.toJson();
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
        assertThat(div.find(), is(webElement));
    }

    @Test
    public void findAll() {
        List<WebElement> result =  Arrays.asList(webElement);
        when(driverMock.findElements(By.xpath("//" + div.getXPath().get()))).thenReturn(result);

        assertThat(div.findAll(), is(equalTo(result)));
    }

    @Test
    public void scroll() {
        div.scrollTo();
        verify(((Interactive) driverMock)).perform(captor.capture());
        Object[] actions = captor.getAllValues().get(0).toArray();
        Sequence firstSequence = (Sequence) actions[0];
        Map<String, Object> firstActions = firstSequence.toJson();
        assertThat(firstActions.get("type"), equalTo("pointer"));
        Map<String, Object> op = ((List<Map<String, Object>>)firstActions.get("actions")).get(0);
        assertThat(op.get("type"), equalTo("pointerMove"));
        assertThat(op.get("origin"), is(webElement));
    }

    @Test
    public void hover() {
        div.hover();
        verify(((Interactive) driverMock)).perform(captor.capture());
        Object[] actions = captor.getAllValues().get(0).toArray();
        Sequence firstSequence = (Sequence) actions[0];
        Map<String, Object> keysActions = firstSequence.toJson();
        assertThat(keysActions.get("type"), equalTo("pointer"));
        List<Map<String, Object>> ops = (List<Map<String, Object>>)keysActions.get("actions");
        assertThat(ops.get(0).get("type"), equalTo("pointerMove"));
        assertThat(ops.get(0).get("origin"), is(webElement));
    }
}
