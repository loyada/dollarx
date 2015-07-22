package testsetup

import java.util.concurrent.TimeUnit

import org.openqa.selenium.{Dimension, WebDriver}
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.logging.{LogType, LoggingPreferences}
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.{CapabilityType, DesiredCapabilities}


case class DriverSetup(logEnabled: Boolean) {

  private val loggingPrefs = {
    import LogType._
    import java.util.logging.Level._
    val prefs = new LoggingPreferences
    prefs.enable(DRIVER, INFO)
    prefs.enable(CLIENT, INFO)
    prefs.enable(PERFORMANCE, INFO)
    prefs.enable(BROWSER, ALL)
    prefs
  }

  private val withLogSetup: (DesiredCapabilities => DesiredCapabilities) = {
    desireCapabilities =>
      if (logEnabled) {
        desireCapabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPrefs)
      }
      desireCapabilities
  }

  private val chromeCapabilities: DesiredCapabilities = {
    val options = new ChromeOptions
    options.addArguments("---disable-extensions")
    val capabilities = DesiredCapabilities.chrome
    capabilities.setCapability(ChromeOptions.CAPABILITY, options)
    withLogSetup(capabilities)
  }

  private def phantomCpabilities(driverPath: String): DesiredCapabilities = {
    import org.openqa.selenium.phantomjs.PhantomJSDriverService._
    val capabilities = DesiredCapabilities.chrome
    capabilities.setCapability(PHANTOMJS_EXECUTABLE_PATH_PROPERTY, driverPath)
    capabilities.setCapability(PHANTOMJS_CLI_ARGS, Array("--ignore-ssl-errors=true"))
    withLogSetup(capabilities)
  }

  def createNewDriver(driverName: String, driverPath: String): WebDriver = {
    val driver = driverName match {
      case "chrome" => {
        System.setProperty("webdriver.chrome.driver", driverPath)

        new ChromeDriver(chromeCapabilities)
      }
      case "phantom" => {
        val caps = new DesiredCapabilities
        new PhantomJSDriver(phantomCpabilities(driverPath))
      }
    }

    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)
    driver
  }


}
