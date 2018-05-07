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


    private DesiredCapabilities withLogSetup(DesiredCapabilities capabilities) {
        if (logEnabled) {
            capabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());
        }
        return capabilities;
    }

    private DesiredCapabilities chromeCapabilities() {
        final ChromeOptions options = new ChromeOptions();
        options.addArguments("---disable-extensions");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return withLogSetup(capabilities);
    }



    private WebDriver getCorrectDriver( String driverPath) {
        System.setProperty("webdriver.chrome.driver", driverPath);
        return new ChromeDriver(chromeCapabilities());
    }

    public WebDriver createNewDriver(String driverPath) {
        WebDriver driver = getCorrectDriver(driverPath);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        return driver;
    }


    public static WebDriver createStandardChromeDriver() {
        final String driverPath = System.getenv().get("CHROMEDRIVERPATH");
        WebDriver driver = new DriverSetup(true).getCorrectDriver(driverPath);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }

}
