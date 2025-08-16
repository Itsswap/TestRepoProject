package testcases;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.MasterPage;
import pages.LoginPage;
import utilities.ReadingExcel;

public class LoginPageTest extends MasterPage {

	LoginPage lp;

	public LoginPageTest() throws Exception {
		super();
	}

	@BeforeMethod
	public void setUp() throws Exception {
		initializeDriver(); // from MasterPage
		driver = getDriver(); // Make sure this is not null
		lp = new LoginPage();
	}

	@DataProvider(name = "loginData")
	public Object[][] getLoginData() {

		String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\Testdata.xlsx";
		File file = new File(filePath);
		System.out.println("📂 Looking for Excel at: " + file.getAbsolutePath());

		if (!file.exists()) {
			throw new RuntimeException("❌ Excel file NOT FOUND at: " + filePath);
		}

		List<String[]> data = ReadingExcel.readLoginData(filePath);
		String[] selectedUser = data.get(0); // Selecting the first user, 0 will select the first user. // row (index 0)

		Object[][] testData = new Object[1][2]; // Create an Object array to hold test data for 1 user
		testData[0][0] = selectedUser[0]; // data[0][0] is the username from the first row. // (first column in the row)
		testData[0][1] = selectedUser[1]; // data[0][1] is the password from the first row. // (second column in the
											// row)

		return testData;
	}

	@Test(dataProvider = "loginData", priority = 0, groups = { "positive", "sanity" })
	public void Verify_userLoginSuccess(String username, String password) throws Exception {
		lp.userLogin(username, password);

		// Optionally add assertion here
		boolean isLoggedIn = driver.findElements(By.xpath("//div[@class='inventory_list']")).size() > 0;
		Assert.assertTrue(isLoggedIn, "Login failed for: " + username);
		lp.userLogout();
	}

	@Test(priority = 1, groups = "negative")
	public void Verify_loginWithCorrectUsernameIncorrectPassword() throws Exception {
		lp.userLogin("standard_user", "wrong_password");
		String error = lp.getErrorMessage();
		Assert.assertTrue(error.contains("Epic sadface: Username and password do not match any user in this service"),
				"Expected error message not found.");
	}

	@Test(priority = 2, groups = "negative")
	public void Verify_loginWithIncorrectUsernameCorrectPassword() throws Exception {
		lp.userLogin("wrong_user", "secret_sauce");
		String error = lp.getErrorMessage();
		Assert.assertTrue(error.contains("Epic sadface: Username and password do not match any user in this service"),
				"Expected error message not found.");
	}

	@Test(priority = 3, groups = "negative")
	public void Verify_loginWithEmptyUsername() throws Exception {
		lp.userLogin("", "secret_sauce");
		String error = lp.getErrorMessage();
		Assert.assertTrue(error.contains("Epic sadface: Username is required"),
				"Expected 'Username is required' message not shown.");
	}

	@Test(priority = 4, groups = "negative")
	public void Verify_loginWithEmptyPassword() throws Exception {
		lp.userLogin("standard_user", "");
		String error = lp.getErrorMessage();
		Assert.assertTrue(error.contains("Epic sadface: Password is required"),
				"Expected 'Password is required' message not shown.");
	}

	@Test(priority = 5, groups = "negative")
	public void Verify_loginWithEmptyUsernameAndPassword() throws Exception {
		lp.userLogin("", "");
		String error = lp.getErrorMessage();
		Assert.assertTrue(error.contains("Epic sadface: Username is required"),
				"Expected 'Username is required' message not shown.");
	}

	// 📋 GENERAL/SANITY TESTS

	@Test(groups = "sanity")
	public void Verify_pageUrl() throws Exception {
		lp.userLogin("standard_user", "secret_sauce");
		String actualUrl = lp.pageUrl();
		String expectedUrl = "https://www.saucedemo.com/v1/inventory.html";
		Assert.assertEquals(actualUrl, expectedUrl, "URL did not match after login..");
		lp.userLogout();
	}

	@Test(groups = "sanity")
	public void Verify_pageTitle() throws Exception {
		lp.userLogin("standard_user", "secret_sauce");
		String actualtitle = lp.pageTitle();
		String expectedtitle = "Swag Labs";
		Assert.assertEquals(actualtitle, expectedtitle, "Title did not match after login.");
		lp.userLogout();
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
