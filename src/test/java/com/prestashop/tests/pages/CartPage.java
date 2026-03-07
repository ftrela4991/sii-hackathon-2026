package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class CartPage extends BasePage {

    // Actual DOM: <ul class="cart-items"><li class="cart-item">
    private static final By CART_ITEM = By.cssSelector("li.cart-item");
    // Actual DOM: <a class="remove-from-cart" data-link-action="delete-from-cart">
    private static final By REMOVE_BUTTON = By.cssSelector("a.remove-from-cart");
    // Actual DOM: <h1 class="h1">Shopping Cart</h1>
    private static final By CART_PAGE_HEADER = By.cssSelector("h1.h1");

    private static final int WAIT_TIMEOUT = 15;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        return driver.findElements(CART_ITEM).size();
    }

    public void removeProduct() {
        click(REMOVE_BUTTON);
    }

    public void waitForCartItemsToDisappear() {
        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT))
                .until(ExpectedConditions.invisibilityOfElementLocated(CART_ITEM));
    }

    @Override
    public void assertLoaded() {
        waitForElement(CART_PAGE_HEADER);
    }
}
