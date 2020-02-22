package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.ElementProperties;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.highlevelapi.CheckBox;
import com.github.loyada.jdollarx.highlevelapi.RadioInput;
import com.github.loyada.jdollarx.singlebrowser.highlevelapi.CheckBoxes;
import com.github.loyada.jdollarx.singlebrowser.highlevelapi.RadioInputs;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HighLevelPathsIntegration {
    @BeforeClass
    public static void setup() {
        driver = DriverSetup.createStandardChromeDriver();
    }


    @Before
    public void refresh() throws InterruptedException {
        driver.navigate().refresh();
    }

    @AfterClass
    public static void teardown(){
        driver.quit();
    }

    @Test
    public void testInputFor(){
        driver.get("https://www.w3schools.com/tags/tryit.asp?filename=tryhtml5_input_type_checkbox");
        driver.switchTo().frame("iframeResult");
        Path myInput = HighLevelPaths.inputFor(" I have a car");
        clickOn(myInput);
    }

    @Test
    public void testCheckbox(){
        driver.get("https://www.w3schools.com/tags/tryit.asp?filename=tryhtml5_input_type_checkbox");
        driver.switchTo().frame("iframeResult");
        CheckBox checkBox =  CheckBoxes.checkBoxWithLabel("I have a car");
        checkBox.check();
        assertTrue(checkBox.isChecked());
        assertThat(checkBox.toString(), equalTo("checkbox for 'I have a car'"));
    }

    @Test
    public void testCheckboxUnceck(){
        driver.get("https://www.w3schools.com/tags/tryit.asp?filename=tryhtml5_input_type_checkbox");
        driver.switchTo().frame("iframeResult");
        CheckBox checkBox =  CheckBoxes.checkBoxWithLabel("I have a car");
        checkBox.check();
        checkBox.uncheck();
        assertFalse(checkBox.isChecked());
    }

    @Test
    public void testInputFor2(){
        driver.get("https://blueprintjs.com/docs/#core/components/checkbox");
        CheckBox checkbox =  CheckBoxes.checkBoxWithLabel("Checkbox");
        checkbox.check();;
        assertTrue(checkbox.isChecked());
    }

    @Test
    public void inputWithProps(){
        driver.get("https://www.w3schools.com/tags/tryit.asp?filename=tryhtml5_input_type_checkbox");
        driver.switchTo().frame("iframeResult");
        CheckBox checkbox =  CheckBoxes.checkBoxWithProperties(ElementProperties.isNthSibling(3));
        checkbox.check();;
        assertTrue(checkbox.isChecked());
        assertThat(checkbox.toString(), equalTo("checkbox, that is in place 3 among its siblings"));
    }

    @Test
    public void testRadioUnlabeledText(){
        URL url = HighLevelPathsIntegration.class.getClassLoader().getResource("html/input-example1.html");
        driver.get(url.toString());
        RadioInput myInput = RadioInputs.withUnlabeledText("Female");
        myInput.select();
        assertTrue(myInput.isSelected());
    }

    @Test
    public void testRadioUnknown(){
        driver.get("https://www.w3schools.com/html/tryit.asp?filename=tryhtml_input_radio");
        driver.switchTo().frame("iframeResult");
        RadioInput myInput = RadioInputs.withTextUnknownDOM("Female", 5, TimeUnit.SECONDS);
        myInput.select();
        assertTrue(myInput.isSelected());
    }

    @Test
    public void testRadioUnknownLabel(){
        URL url = HighLevelPathsIntegration.class.getClassLoader().getResource("html/input-example2.html");
        driver.get(url.toString());
        RadioInput myInput = RadioInputs.withTextUnknownDOM("Female", 5, TimeUnit.SECONDS);
        myInput.select();
        assertTrue(myInput.isSelected());
    }

    @Test
    public void testRadioUnknownNoLabel(){
        URL url = HighLevelPathsIntegration.class.getClassLoader().getResource("html/input-example1.html");
        driver.get(url.toString());
        RadioInput myInput = RadioInputs.withTextUnknownDOM("Female", 5, TimeUnit.SECONDS);
        myInput.select();
        assertTrue(myInput.isSelected());
    }

    @Test
    public void testRadio2(){
        driver.get("https://blueprintjs.com/docs/#core/components/radio");
        RadioInput radio = RadioInputs.withLabeledText("Radio");
        radio.select();
        assertTrue(radio.isSelected());
    }

}
