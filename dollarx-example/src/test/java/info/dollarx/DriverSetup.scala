package info.dollarx

import java.util.concurrent.TimeUnit

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.logging.{LogType, LoggingPreferences}
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.{CapabilityType, DesiredCapabilities}


case class DriverSetup(logEnabled: Boolean) {

  val CHROME = "chrome"
  val PHANTOM = "phantom"

  private val loggingPrefs = {
    import java.util.logging.Level._

    import LogType._
    val prefs = new LoggingPreferences
    prefs.enable(DRIVER, INFO)
    prefs.enable(CLIENT, INFO)
    prefs.enable(BROWSER, ALL)
    prefs
  }

  private def withLogSetup(capabilities: DesiredCapabilities): DesiredCapabilities = {
    if (logEnabled) {
      capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPrefs)
    }
    capabilities
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
      case CHROME =>
        System.setProperty("webdriver.chrome.driver", driverPath)
        new ChromeDriver(chromeCapabilities)

      case PHANTOM =>
        new PhantomJSDriver(phantomCpabilities(driverPath))
    }

    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS)
    driver
  }


}
