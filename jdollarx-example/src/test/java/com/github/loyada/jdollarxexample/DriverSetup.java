package com.github.loyada.jdollarxexample;

import org.openqa.selenium.logging.LoggingPreferences;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.openqa.selenium.phantomjs.PhantomJSDriverService.*;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.openqa.selenium.logging.LogType.*;
import static java.util.logging.Level.*;

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

    private DesiredCapabilities phantomCpabilities(String driverPath) {
        final DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(PHANTOMJS_EXECUTABLE_PATH_PROPERTY, driverPath);
        String[] args = {"--ignore-ssl-errors=true"};
        capabilities.setCapability(PHANTOMJS_CLI_ARGS, args);
        return withLogSetup(capabilities);
    }

    private WebDriver getCorrectDriver(String driverName, String driverPath) {
        switch (driverName) {
            case CHROME:
                System.setProperty("webdriver.chrome.driver", driverPath);
                return new ChromeDriver(chromeCapabilities());
            case PHANTOM:
                return new PhantomJSDriver(phantomCpabilities(driverPath));
        }
        throw new UnsupportedOperationException();
    }

    public WebDriver createNewDriver(String driverName, String driverPath) {
        WebDriver driver = getCorrectDriver(driverName, driverPath);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        return driver;
    }


}
