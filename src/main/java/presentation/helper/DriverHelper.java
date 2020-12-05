package main.java.presentation.helper;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

public class DriverHelper {
   public static void setText(WebElement elm, String text) {
      elm.clear();
      elm.click();
      elm.sendKeys(text);
   }

   public static void selectDropdownByIndex(WebElement elm, int index) throws Exception {
      new Select(elm).selectByIndex(index);
   }

   public static void selectDropdownByValue(WebElement elm, String value) throws Exception {
      new Select(elm).selectByValue(value);
   }

   public static void selectDropdownByVisibleText(WebElement elm, String text) throws Exception {
      new Select(elm).selectByVisibleText(text);
   }

   public static void clickTo(WebElement element) throws Exception {
      element.click();
   }

   public static void closeBrowser(WebDriver driver) {
      driver.close();
   }

   public static byte[] takeScreenShot(WebDriver driver) throws Exception {
      if (driver instanceof TakesScreenshot) {
         return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      }
      else {
         throw new Exception("Driver does not support take screen shot");
      }
   }

   public static File takeScreenShotAsFile(WebDriver driver) throws Exception {
      if (driver instanceof TakesScreenshot) {
         return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      }
      else {
         throw new Exception("Driver does not support take screen shot");
      }
   }

   public static byte[] takeScreenShotAsBytes(WebDriver driver) throws Exception {
      if (driver instanceof TakesScreenshot) {
         return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      }
      else {
         throw new Exception("Driver does not support take screen shot");
      }
   }

   /**
    * @return size of browser (not web page).
    */
   public static Dimension getBrowserSize(WebDriver driver) {
      return driver.manage().window().getSize();
   }

   public static void clickCenterElementWithOffset(WebDriver driver, WebElement target, int xOffset, int yOffset) {
      Actions actions = new Actions(driver);
      actions.moveToElement(target).moveByOffset(xOffset, yOffset).click().perform();
   }

   public static WebElement waitElementUntilVisible(WebDriver webdriver, WebElement element, int maxWaitSeconds) {
      return new WebDriverWait(webdriver, maxWaitSeconds).until(ExpectedConditions.visibilityOf(element));
   }

   public static String getInputText(WebElement input) {
      return input.getAttribute("value");
   }

   //      driver.manage().window().fullscreen();
   //      driver.switchTo().alert().accept();
}
