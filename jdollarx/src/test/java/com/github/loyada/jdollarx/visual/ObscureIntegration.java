package com.github.loyada.jdollarx.visual;

import com.github.loyada.jdollarx.DriverSetup;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.singlebrowser.Obscure;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isDisplayed;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isNotDisplayed;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ObscureIntegration {
  private static URL url;


  @BeforeClass
  public static void setup() {
    driver = DriverSetup.createStandardChromeDriver();
    driver.manage().timeouts().implicitlyWait(1, SECONDS);
    url = ObscureIntegration.class.getClassLoader().getResource("html/recipes.html");
  }

  @Before
  public void reload() {
    driver.get(url.toString());
  }

  @AfterClass
  public static void teardown(){
    driver.quit();
  }

  @Test
  public void obscureSingleElement() throws Exception {
    Path firstJavaSnippet = firstOccurrenceOf(div.withClass("highlight-java"));
    assertThat(firstJavaSnippet, isDisplayed());

    try (Obscure obscure = new Obscure(firstJavaSnippet)) {
      assertThat(firstJavaSnippet, isNotDisplayed());
      assertThat(obscure.getObscuredElements().size(), is(1));

    }

    assertThat(firstJavaSnippet, isDisplayed());
  }

  @Test
  public void obscureSingleElementNotFound() throws Exception {
    Path firstJavaSnippet = firstOccurrenceOf(div.withClass("xyz"));
    try (Obscure obscure = new Obscure(firstJavaSnippet)) {
      assertThat(obscure.getObscuredElements(), Matchers.empty());
    }
  }

  @Test
  public void obscureMultipleElementsNonStrict() throws Exception {
    Path firstJavaSnippet = firstOccurrenceOf(div.withClass("highlight-java"));
    Path secondJavaSnippet = occurrenceNumber(2).of(div.withClass("highlight-java"));
    Path nonExistingElement = div.withClass("snafu");
    assertThat(firstJavaSnippet, isDisplayed());
    assertThat(secondJavaSnippet, isDisplayed());

    List<Path> els = Arrays.asList(firstJavaSnippet, secondJavaSnippet, nonExistingElement);

    try (Obscure obscure = new Obscure(els)) {
      assertThat(firstJavaSnippet, isNotDisplayed());
      assertThat(secondJavaSnippet, isNotDisplayed());

      assertThat(obscure.getObscuredElements().size(), is(2));
    }

    assertThat(firstJavaSnippet, isDisplayed());
    assertThat(secondJavaSnippet, isDisplayed());
  }


  @Test(expected= NoSuchElementException.class)
  public void obscureMultipleElementsStrict() throws Exception {
    Path firstJavaSnippet = firstOccurrenceOf(div.withClass("highlight-java"));
    Path nonExistingElement = div.withClass("snafu");

    try (Obscure obscure = new Obscure(Arrays.asList(firstJavaSnippet, nonExistingElement), true)) {
    }
  }

}
