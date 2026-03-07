package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class ProductDetailPage extends BasePage {

    private static final By PRODUCT_NAME = By.cssSelector("h1.h1");
    private static final By PRODUCT_PRICE = By.cssSelector(".product-price span");
    // Actual DOM: <button data-button-action="add-to-cart" class="btn btn-primary add-to-cart">
    private static final By ADD_TO_CART_BUTTON = By.cssSelector("[data-button-action='add-to-cart']");
    // Modal that appears after AJAX add-to-cart: <div id="blockcart-modal">
    private static final By CART_BADGE = By.id("blockcart-modal");
    private static final By PRODUCT_CONTAINER = By.cssSelector("[data-button-action='add-to-cart']");

    private static final int WAIT_TIMEOUT = 15;

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return getText(PRODUCT_NAME);
    }

    public String getProductPrice() {
        return getText(PRODUCT_PRICE);
    }

    public void clickAddToCart() {
        click(ADD_TO_CART_BUTTON);
    }

    public void waitForCartBadge() {
        new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(CART_BADGE));
    }

    @Override
    public void assertLoaded() {
        waitForElement(PRODUCT_CONTAINER);
    }
}
