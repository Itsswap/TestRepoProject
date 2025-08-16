package pageObjects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

	public static WebDriver lpdriver;

	public LoginPage(WebDriver rdriver) {
		lpdriver = rdriver;
		PageFactory.initElements(rdriver, this);
	}

	@FindBy(id = "Email")
	@CacheLookup
	WebElement txtEmail;

	@FindBy(id = "Password")
	@CacheLookup
	WebElement txtPassword;

	@FindBy(xpath = "//button[@class='button-1 login-button']")
	@CacheLookup
	WebElement btnLogin;

	@FindBy(linkText = "Logout")
	@CacheLookup
	WebElement lnkLogout;

	public void setUserName(String usname) {
		txtEmail.clear();
		txtEmail.sendKeys(usname);
	}

	public void setUserPassword(String uspwd) {
		txtPassword.clear();
		txtPassword.sendKeys(uspwd);
	}

	public void clickLogin() {
		btnLogin.click();
	}

	public void clickLogout() {
		WebDriverWait wait = new WebDriverWait(lpdriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("ajaxBusy")));
		lpdriver.findElement(By.linkText("Logout")).click();
		//lnkLogout.click();
	}
}
