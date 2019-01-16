package com.github.loyada.jdollarx;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

import static java.util.logging.Level.ALL;
import static java.util.logging.Level.INFO;
import static org.openqa.selenium.logging.LogType.BROWSER;
import static org.openqa.selenium.logging.LogType.CLIENT;
import static org.openqa.selenium.logging.LogType.DRIVER;

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


    private DesiredCapabilities withLogSetup(DesiredCapabilities capabilities) {
        if (logEnabled) {
            capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());
        }
        return capabilities;
    }

    private DesiredCapabilities chromeCapabilities(boolean isHeadless) {
        final ChromeOptions options = new ChromeOptions();
        options.addArguments("---disable-extensions");
        if (isHeadless) {
            options.addArguments("--headless");
        }
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return withLogSetup(capabilities);
    }

    private WebDriver getCorrectDriver( String driverPath, boolean isHeadless) {
        System.setProperty("webdriver.chrome.driver", driverPath);
        return new ChromeDriver(chromeCapabilities(isHeadless));
    }

    public static WebDriver createStandardChromeDriver() {
       return getNew(false);
    }

    public static WebDriver createHeadlessChromeDriver() {
        return getNew(true);

    }

    private static WebDriver getNew(boolean isHeadless) {
        final String driverPath = DriverSetup.class.getClassLoader().getResource("chromedriver").getFile();
        WebDriver driver = new DriverSetup(true).getCorrectDriver(driverPath, isHeadless);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }
}
