package testcases;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import base.MasterPage;
import pages.InventoryPage;
import pages.LoginPage;

public class InventoryPageTest extends MasterPage {

	WebDriver driver;
	InventoryPage ip;
	LoginPage lp;

	@BeforeMethod
	public void setUp() throws Exception {
		initializeDriver();
		driver = getDriver();
		lp = new LoginPage();
		ip = new InventoryPage();
		lp.userLogin("standard_user", "secret_sauce"); // login to reach inventory
	}

	@Test(groups = "sanity")
	public void verify_SortingByNameAZ() {
		String visibleText = "Name (A to Z)";
		ip.selectSortOption(By.className("product_sort_container"), visibleText);
		List<String> names = ip.getProductNames();

		List<String> expected = names.stream().sorted().collect(Collectors.toList());

		Assert.assertEquals(names, expected, "❌ Product sorting failed: nameAZ");
	}

	@Test(groups = "sanity")
	public void verify_SortingByNameZA() {
		String visibleText = "Name (Z to A)";
		ip.selectSortOption(By.className("product_sort_container"), visibleText);
		List<String> names = ip.getProductNames();

		List<String> expected = names.stream()
		                             .sorted((a, b) -> b.compareTo(a))
		                             .collect(Collectors.toList());

		Assert.assertEquals(names, expected, "❌ Product sorting failed: nameZA");
	}

	@Test
	public void verify_SortingByPriceLowToHigh() {
		String visibleText = "Price (low to high)";
		ip.selectPriceOption(By.className("product_sort_container"), visibleText);
		List<Double> prices = ip.getProductPrices();

		List<Double> expected = prices.stream().sorted().collect(Collectors.toList());

		Assert.assertEquals(prices, expected, "❌ Price sorting failed: lowToHigh");
	}

	@Test
	public void verify_SortingByPriceHighToLow() {
		String visibleText = "Price (high to low)";
		ip.selectPriceOption(By.className("product_sort_container"), visibleText);
		List<Double> prices = ip.getProductPrices();

		List<Double> expected = prices.stream()
		                             .sorted((a, b) -> Double.compare(b, a))
		                             .collect(Collectors.toList());

		Assert.assertEquals(prices, expected, "❌ Price sorting failed: highToLow");
	}

	@Test(groups = "sanity")
	public void verify_TitleAndUrl() {
		Assert.assertEquals(driver.getTitle(), "Swag Labs");
		Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/v1/inventory.html");
	}

	//@Test
	public void verify_AddToCartAndRemove() throws Exception {
		String item = "Sauce Labs Backpack";
		ip.addToCart(item);
		Assert.assertEquals(ip.getCartCount(), 1);
		ip.removeFromCart(item);
		Assert.assertEquals(ip.getCartCount(), 0);
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
