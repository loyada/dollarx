package com.github.loyada.jdollarx;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LoggingPreferences;

import java.util.concurrent.TimeUnit;

import static java.util.logging.Level.ALL;
import static java.util.logging.Level.INFO;
import static org.openqa.selenium.logging.LogType.*;

public class DriverSetup {
    final String CHROME = "chrome";
    final String PHANTOM = "phantom";
    final Boolean logEnabled;

    public DriverSetup(Boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    public LoggingPreferences getLoggingPrefs() {
        LoggingPreferences prefs = new LoggingPreferences();
        prefs.enable(DRIVER, INFO);
        prefs.enable(CLIENT, INFO);
        prefs.enable(BROWSER, ALL);
        return prefs;
    }

    private ChromeOptions getChromeOptions(boolean isHeadless) {
        final ChromeOptions options = new ChromeOptions();
  //      options.addArguments("start-maximized"); // open Browser in maximized mode
        options.addArguments("disable-infobars"); // disabling infobars
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("--disable-gpu"); // applicable to windows os only
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--no-sandbox"); // Bypass OS security model
        if (isHeadless) options.addArguments("--headless");
        return options;
    }

    private WebDriver getCorrectDriver( String driverPath, boolean isHeadless) {
        System.setProperty("webdriver.chrome.driver", driverPath);
        return new ChromeDriver(getChromeOptions(isHeadless));
    }

    public static WebDriver createStandardChromeDriver() {
        boolean useHeadless = System.getenv("HEADLESS_TESTING")!=null;
       return getNew(useHeadless);
    }

    public static WebDriver createHeadlessChromeDriver() {
        return getNew(true);

    }

    private static WebDriver getNew(boolean isHeadless) {
        final String driverPath = System.getenv().get("CHROMEDRIVERPATH");
        WebDriver driver = new DriverSetup(true).getCorrectDriver(driverPath, isHeadless);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }
}
