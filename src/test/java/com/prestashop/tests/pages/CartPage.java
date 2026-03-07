package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    // Actual DOM: <ul class="cart-items"><li class="cart-item">
    private static final By CART_ITEM = By.cssSelector("li.cart-item");
    // Actual DOM: <a class="remove-from-cart" data-link-action="delete-from-cart">
    private static final By REMOVE_BUTTON = By.cssSelector("a.remove-from-cart");
    // Actual DOM: <h1 class="h1">Shopping Cart</h1>
    private static final By CART_PAGE_HEADER = By.cssSelector("h1.h1");
    // Quantity controls: <input class="js-cart-line-product-quantity">
    private static final By QUANTITY_INPUT = By.cssSelector("input.js-cart-line-product-quantity");
    // <button class="... js-increase-product-quantity ...">
    private static final By INCREASE_QTY_BUTTON = By.cssSelector("button.js-increase-product-quantity");
    // <button class="... js-decrease-product-quantity ...">
    private static final By DECREASE_QTY_BUTTON = By.cssSelector("button.js-decrease-product-quantity");
    // Line item total: <strong>£22.94</strong> inside .product-line-grid-right
    private static final By LINE_TOTAL = By.cssSelector(".product-line-grid-right strong");
    // Products subtotal (before shipping): <div id="cart-subtotal-products"><span class="value">
    private static final By PRODUCTS_SUBTOTAL = By.cssSelector("#cart-subtotal-products .value");
    // Grand total: <span class="value">£31.34</span> inside .cart-total
    private static final By GRAND_TOTAL = By.cssSelector(".cart-total .value");
    // Header cart counter: <span class="cart-products-count">(1)</span>
    private static final By HEADER_CART_COUNT = By.cssSelector(".cart-products-count");

    private static final int WAIT_TIMEOUT = 15;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        return driver.findElements(CART_ITEM).size();
    }

    public int getQuantity() {
        return Integer.parseInt(waitForElement(QUANTITY_INPUT).getAttribute("value").trim());
    }

    public void increaseQuantity() {
        click(INCREASE_QTY_BUTTON);
    }

    public void decreaseQuantity() {
        click(DECREASE_QTY_BUTTON);
    }

    public void removeProduct() {
        click(REMOVE_BUTTON);
    }

    public String getLineTotal() {
        return getText(LINE_TOTAL).trim();
    }

    public List<String> getLineTotals() {
        return driver.findElements(LINE_TOTAL).stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    public String getProductsSubtotal() {
        return getText(PRODUCTS_SUBTOTAL).trim();
    }

    public String getGrandTotal() {
        return getText(GRAND_TOTAL).trim();
    }

    public String getHeaderCartCount() {
        return getText(HEADER_CART_COUNT).trim();
    }

    public void waitForLineTotalsCount(int count) {
        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT))
                .until(d -> d.findElements(LINE_TOTAL).size() == count);
    }

    public void waitForQuantityToBe(int expected) {
        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT))
                .until(d -> String.valueOf(expected).equals(
                        d.findElement(QUANTITY_INPUT).getAttribute("value")));
    }

    public void waitForGrandTotalToChangFrom(String previousTotal) {
        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT))
                .until(d -> !previousTotal.equals(d.findElement(GRAND_TOTAL).getText().trim()));
    }

    public void waitForCartItemsToDisappear() {
        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT))
                .until(d -> d.findElements(CART_ITEM).isEmpty());
    }

    public boolean isEmptyCartDisplayed() {
        return driver.findElements(CART_ITEM).isEmpty();
    }

    @Override
    public void assertLoaded() {
        waitForElement(CART_PAGE_HEADER);
    }
}
