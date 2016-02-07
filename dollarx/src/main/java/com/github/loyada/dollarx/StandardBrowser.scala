package com.github.loyada.dollarx

import org.openqa.selenium.WebDriver

class StandardBrowser(val myDriver: WebDriver) extends Browser{
  override protected def driver: WebDriver = myDriver
}
