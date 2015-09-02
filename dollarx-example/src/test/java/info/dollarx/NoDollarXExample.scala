package info.dollarx

import org.openqa.selenium.By
import org.openqa.selenium.interactions.Actions
import org.scalatest.exceptions.TestFailedException
import org.scalatest.mock.MockitoSugar
import org.scalatest.{MustMatchers, BeforeAndAfterAll, BeforeAndAfter, FunSpec}
import scala.collection.JavaConverters._


class NoDollarXExample extends FunSpec with BeforeAndAfter with BeforeAndAfterAll with MustMatchers with MockitoSugar {


  val driverPath = System.getenv.get("CHROMEDRIVERPATH")
  val driver = DriverSetup(true).createNewDriver("chrome", driverPath)
  driver.get("http://www.google.com")

  describe("Googling for amazon") {
    val google = driver.findElement(By.xpath("//*[(@id='searchform' and descendant::form)]//input"))
    val actionBuilder: Actions = new Actions(driver)
    actionBuilder.sendKeys("amazon").build().perform()
    //google.sendKeys("amazon") will not work...

    it("amazon.com should appear as the first result link => sometimes fails because of race condition") {
      //can fails because resultLink is empty
      val resultsLink = driver.findElements(By.xpath("//div[@id='search']//a")).asScala
      resultsLink(0).findElement(By.xpath("//*[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') ='amazon']"))
    }

    it("shows an exception of limited value in case of a failure to perform an operation") {
      val results = driver.findElement(By.xpath("//*[@id='search']"))
        results.findElement(By.xpath("//*[contains(concat(' ', @class, ' '), 'foobar')]")).click()
    }

    it("creates a clear assertion error #1") {
      val results = driver.findElements(By.xpath("//div[@id='search']//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'amazon.com')]"))
      results.size must be(1000)
    }

    it("creates a clear assertion error #2") {
      val warcraftResult = driver.findElement(By.xpath("//*[@id='search']//a[1][translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') ='for the horde!']"))
    }

  }

  override def afterAll()  {
    driver.close()
    driver.quit()
  }
}