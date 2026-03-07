package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the Prestashop Product Detail page.
 *
 * Provides UI operations for product viewing and cart operations:
 * - Getting product name and price
 * - Clicking "Add to cart" button
 * - Handling product options/variants
 */
public class ProductDetailPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(ProductDetailPage.class);

    // Locators for product information
    private static final By PRODUCT_NAME = By.xpath("//h1[@class='h1']");
    private static final By PRODUCT_NAME_ALT = By.xpath("//h1");
    private static final By PRODUCT_PRICE = By.xpath("//span[@class='price']");
    private static final By PRODUCT_PRICE_ALT = By.xpath("//div[@class='product-price']//span");

    // Add to cart button
    private static final By ADD_TO_CART_BUTTON = By.id("add-to-cart");
    private static final By ADD_TO_CART_BUTTON_ALT = By.xpath("//button[contains(text(), 'Add to cart')]");
    private static final By ADD_TO_CART_BUTTON_ALT2 = By.xpath("//button[contains(@class, 'add-to-cart')]");

    // Page verification element - look for add to cart button or product name
    private static final By PRODUCT_CONTAINER = By.xpath("//button[contains(@class, 'add-to-cart')]");
    private static final By PRODUCT_CONTAINER_ALT = By.xpath("//h1");
    private static final By PRODUCT_CONTAINER_ALT2 = By.xpath("//div[@id='product']");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Get the product name from the page.
     *
     * @return product name text
     */
    public String getProductName() {
        logger.info("Getting product name");
        try {
            return getText(PRODUCT_NAME);
        } catch (Exception e) {
            logger.debug("PRODUCT_NAME not found, trying alternative");
            return getText(PRODUCT_NAME_ALT);
        }
    }

    /**
     * Get the product price from the page.
     *
     * @return product price text
     */
    public String getProductPrice() {
        logger.info("Getting product price");
        try {
            return getText(PRODUCT_PRICE);
        } catch (Exception e) {
            logger.debug("PRODUCT_PRICE not found, trying alternative");
            return getText(PRODUCT_PRICE_ALT);
        }
    }

    /**
     * Click the "Add to cart" button to add product to cart.
     */
    public void clickAddToCart() {
        logger.info("Clicking 'Add to cart' button");
        try {
            click(ADD_TO_CART_BUTTON);
        } catch (Exception e) {
            logger.debug("ADD_TO_CART_BUTTON not found, trying alternative");
            try {
                click(ADD_TO_CART_BUTTON_ALT);
            } catch (Exception e2) {
                logger.debug("ADD_TO_CART_BUTTON_ALT not found, trying alternative");
                click(ADD_TO_CART_BUTTON_ALT2);
            }
        }
    }

    /**
     * Verify that the Product Detail page is loaded.
     */
    @Override
    public void assertLoaded() {
        logger.info("Verifying Product Detail page is loaded");
        try {
            waitForElement(PRODUCT_CONTAINER);
        } catch (Exception e) {
            logger.debug("PRODUCT_CONTAINER not found, trying alternative");
            try {
                waitForElement(PRODUCT_CONTAINER_ALT);
            } catch (Exception e2) {
                logger.debug("PRODUCT_CONTAINER_ALT not found, trying alternative");
                waitForElement(PRODUCT_CONTAINER_ALT2);
            }
        }
        logger.info("Product Detail page has loaded successfully");
    }
}
