package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the Prestashop Cart page.
 *
 * Provides UI operations for cart interactions:
 * - Getting cart item count
 * - Removing products from cart
 * - Verifying empty cart
 */
public class CartPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);

    // Cart elements
    private static final By CART_ITEM = By.xpath("//div[@class='cart-item']");
    private static final By CART_ITEM_ALT = By.xpath("//tr[@class='cart-item']");

    // Remove button
    private static final By REMOVE_BUTTON = By.xpath("//button[contains(@class, 'remove')]");
    private static final By REMOVE_BUTTON_ALT = By.xpath("//a[contains(@class, 'delete')]");
    private static final By REMOVE_BUTTON_ALT2 = By.xpath("//i[@class='icon-trash']/..");

    // Empty cart message
    private static final By EMPTY_CART_MESSAGE = By.xpath("//p[contains(text(), 'empty')]");
    private static final By EMPTY_CART_MESSAGE_ALT = By.xpath("//div[@class='empty-cart']");

    // Cart counter
    private static final By CART_COUNTER = By.xpath("//span[@class='cart-products-count']");
    private static final By CART_COUNTER_ALT = By.xpath("//div[@id='cart-total']");

    // Page verification
    private static final By CART_PAGE_HEADER = By.xpath("//h1[contains(text(), 'Cart')]");
    private static final By CART_PAGE_HEADER_ALT = By.xpath("//div[@id='main']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Get the number of items in the cart.
     *
     * @return number of items
     */
    public int getCartItemCount() {
        logger.info("Getting cart item count");
        try {
            String countText = getText(CART_COUNTER).trim();
            countText = countText.replaceAll("[^0-9]", "");
            if (countText.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(countText);
        } catch (Exception e) {
            logger.debug("CART_COUNTER not found, trying alternative");
            try {
                String countText = getText(CART_COUNTER_ALT).trim();
                countText = countText.replaceAll("[^0-9]", "");
                if (countText.isEmpty()) {
                    return 0;
                }
                return Integer.parseInt(countText);
            } catch (Exception e2) {
                logger.error("Could not get cart count", e2);
                return 0;
            }
        }
    }

    /**
     * Remove a product from the cart.
     */
    public void removeProduct() {
        logger.info("Removing product from cart");
        try {
            click(REMOVE_BUTTON);
        } catch (Exception e) {
            logger.debug("REMOVE_BUTTON not found, trying alternative");
            try {
                click(REMOVE_BUTTON_ALT);
            } catch (Exception e2) {
                logger.debug("REMOVE_BUTTON_ALT not found, trying alternative");
                click(REMOVE_BUTTON_ALT2);
            }
        }
    }

    /**
     * Check if empty cart message is displayed.
     *
     * @return true if empty cart message is visible
     */
    public boolean isEmptyCartMessageVisible() {
        logger.info("Checking if empty cart message is visible");
        try {
            return isElementDisplayed(EMPTY_CART_MESSAGE);
        } catch (Exception e) {
            try {
                return isElementDisplayed(EMPTY_CART_MESSAGE_ALT);
            } catch (Exception e2) {
                logger.debug("Could not find empty cart message");
                return false;
            }
        }
    }

    /**
     * Verify that the Cart page is loaded.
     */
    @Override
    public void assertLoaded() {
        logger.info("Verifying Cart page is loaded");
        try {
            waitForElement(CART_PAGE_HEADER);
        } catch (Exception e) {
            logger.debug("CART_PAGE_HEADER not found, trying alternative");
            waitForElement(CART_PAGE_HEADER_ALT);
        }
        logger.info("Cart page has loaded successfully");
    }

    /**
     * Check if an element is displayed on the page.
     */
    private boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
