package base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class CommonMethods extends MasterPage {

	public CommonMethods() throws Exception {
		super();
	}

	// GetText WebElement
	public void getWebElementText(String xpathkey) {
		String getTextOfWebElement = driver.findElement(By.xpath(xpathkey)).getText();
		System.out.println(getTextOfWebElement);
	}

	// Click WebElement
	public void clickWebElement(String xpathkey) {
		driver.findElement(By.xpath(prop.getProperty(xpathkey))).click();
	}

	// Clear WebElement
	public void clearWebElement(String xpathkey) {
		driver.findElement(By.xpath(prop.getProperty(xpathkey))).clear();
	}

	// Enter WebElement Data
	public void enterWebElementData(String xpathkey, String value) {
		driver.findElement(By.xpath(prop.getProperty(xpathkey))).sendKeys(value);
	}

	// Select drop-down by visible text
	public void selectDropdownByVisibleText(By locatorKey, String visibleText) {
		try {
			//By locator = getLocator(locatorKey);
			Select select = new Select(driver.findElement(locatorKey));
			select.selectByVisibleText(visibleText);
			System.out.println("✅ Selected from dropdown '" + locatorKey + "': " + visibleText);
		} catch (Exception e) {
			System.out.println("❌ Failed to select from dropdown '" + locatorKey + "': " + e.getMessage());
		}
	}

	// Add this helper method (if not already added)
	public By getLocator(String key) {
		String locator = prop.getProperty(key);
		String[] parts = locator.split("=", 2);
		String type = parts[0].trim();
		String value = parts[1].trim();

		switch (type.toLowerCase()) {
		case "id":
			return By.id(value);
		case "name":
			return By.name(value);
		case "xpath":
			return By.xpath(value);
		case "css":
			return By.cssSelector(value);
		case "class":
			return By.className(value);
		case "tag":
			return By.tagName(value);
		case "linktext":
			return By.linkText(value);
		case "partiallinktext":
			return By.partialLinkText(value);
		default:
			throw new IllegalArgumentException("Unsupported locator type: " + type);
		}
	}

	// Alert Message
	public void acceptAlert() {
		try {
			Alert alert = driver.switchTo().alert();
			System.out.println("🔔 Alert Text: " + alert.getText());
			alert.accept();
		} catch (NoAlertPresentException e) {
			System.out.println("⚠️ No alert to accept: " + e.getMessage());
		}
	}

	public void dismissAlert() {
		try {
			Alert alert = driver.switchTo().alert();
			System.out.println("🔔 Alert Text: " + alert.getText());
			alert.dismiss();
		} catch (NoAlertPresentException e) {
			System.out.println("⚠️ No alert to dismiss: " + e.getMessage());
		}
	}

	public String getAlertText() {
		try {
			Alert alert = driver.switchTo().alert();
			return alert.getText();
		} catch (NoAlertPresentException e) {
			System.out.println("⚠️ No alert to get text from: " + e.getMessage());
			return null;
		}
	}

	// Handle Alert
	public void handleAlert(String action, String inputText) {
		try {
			Alert alert = driver.switchTo().alert();
			System.out.println("Alert Text: " + alert.getText());

			if (inputText != null) {
				alert.sendKeys(inputText);
			}

			if ("accept".equalsIgnoreCase(action)) {
				alert.accept();
			} else {
				alert.dismiss();
			}
		} catch (NoAlertPresentException e) {
			System.out.println("No alert found: " + e.getMessage());
		}
	}

	// Scroll Down
	public void scrollPageDown() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, 200);");
	}

	// Screenshot
	public String captureScreenshot(WebDriver driver, String methodName) {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String destPath = System.getProperty("user.dir") + "./screenshots/screenshots" + methodName + ".png";
		try {
			Files.createDirectories(Paths.get(System.getProperty("user.dir") + "./screenshots"));
			Files.copy(src.toPath(), Paths.get(destPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destPath;
	}

	// Get Driver Instance
	public WebDriver getDriverFromTestInstance(Object testInstance) {
		try {
			return (WebDriver) testInstance.getClass().getField("driver").get(testInstance);
		} catch (Exception e) {
			e.printStackTrace(); // helpful for debugging
			return null;
		}
	}

}
