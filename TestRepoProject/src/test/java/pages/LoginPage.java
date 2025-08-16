package pages;

import org.openqa.selenium.By;

import base.CommonMethods;

public class LoginPage extends CommonMethods {

	public LoginPage() throws Exception {
		super();
	}

	public void userLogin(String username, String password) throws Exception {
		enterWebElementData("inputname", username);
		enterWebElementData("inputpwd", password);
		clickWebElement("submitBtn");
	}

	public void userLogout() throws Exception {
		clickWebElement("openmenu");
		clickWebElement("logoutbtn");
	}

	public String pageUrl() {
		return driver.getCurrentUrl();
	}

	public String pageTitle() {
		return driver.getTitle();
	}

	public String getErrorMessage() {
		return driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
	}
}
