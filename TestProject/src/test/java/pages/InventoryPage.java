package pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import base.CommonMethods;

public class InventoryPage extends CommonMethods {

	public InventoryPage() throws Exception {
		super(); // Optional if not doing anything
	}

	By sortDropdown = By.className("product_sort_container");
	By productNames = By.className("inventory_item_name");
	By productPrices = By.className("inventory_item_price");
	By cartBadge = By.className("shopping_cart_badge");

	public void selectSortOption(By locatorKey, String visibleText) {
		selectDropdownByVisibleText(sortDropdown, visibleText);
	}

	public List<String> getProductNames() {
		return driver.findElements(productNames)
		             .stream()
		             .map(WebElement::getText)
		             .collect(Collectors.toList());
	}

	public List<Double> getProductPrices() {
		return driver.findElements(productPrices)
		             .stream()
		             .map(e -> Double.parseDouble(e.getText().replace("$", "")))
		             .collect(Collectors.toList());
	}

	public void addToCart(String itemName) {
		driver.findElement(By.xpath("//div[text()='" + itemName + "']/ancestor::div[@class='inventory_item']//button")).click();
	}

	public void removeFromCart(String itemName) {
		driver.findElement(By.xpath("//div[text()='" + itemName + "']/ancestor::div[@class='inventory_item']//button")).click();
	}

	public int getCartCount() {
		try {
			return Integer.parseInt(driver.findElement(cartBadge).getText());
		} catch (Exception e) {
			return 0;
		}
	}
}
