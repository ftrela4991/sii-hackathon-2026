package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the Prestashop Product Detail page.
 *
 * Provides UI operations for reading product data and interacting
 * with the add-to-cart flow on a single product page.
 *
 * Selectors verified against live app: http://145.239.29.235
 */
public class ProductPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(ProductPage.class);

    // Product details
    // h1.h1 — product title heading
    private static final By PRODUCT_NAME = By.cssSelector("h1.h1");
    // .current-price-value — price span, content attribute holds numeric value, text has currency
    private static final By PRODUCT_PRICE = By.cssSelector(".current-price-value");

    // Add to cart button — data-button-action="add-to-cart"
    private static final By ADD_TO_CART_BUTTON = By.cssSelector("button[data-button-action='add-to-cart']");

    // Cart counter in header — .cart-products-count, e.g. "(0)", "(1)"
    private static final By CART_COUNT_HEADER = By.cssSelector(".cart-products-count");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Get the product name displayed on the detail page.
     *
     * @return product name text
     */
    public String getProductName() {
        String name = waitForElement(PRODUCT_NAME).getText().trim();
        logger.info("Product name: {}", name);
        return name;
    }

    /**
     * Get the product price text as displayed (e.g. "£22.94").
     *
     * @return price with currency symbol
     */
    public String getProductPrice() {
        String price = waitForElement(PRODUCT_PRICE).getText().trim();
        logger.info("Product price: {}", price);
        return price;
    }

    /**
     * Click the "Add to cart" button.
     * The modal (#blockcart-modal) will appear asynchronously after click.
     */
    public void clickAddToCart() {
        logger.info("Clicking Add to cart button");
        click(ADD_TO_CART_BUTTON);
    }

    /**
     * Get the cart counter text shown in the page header (e.g. "(1)").
     *
     * @return cart counter text
     */
    public String getCartHeaderCount() {
        String count = waitForElement(CART_COUNT_HEADER).getText().trim();
        logger.info("Cart header count: {}", count);
        return count;
    }

    /**
     * Check whether the product detail page contains variant/combination selectors
     * (e.g. colour swatches, size dropdowns).
     *
     * A standard product without combinations must NOT have these selectors.
     * Uses findElements() so no exception is thrown when the section is absent.
     *
     * Selector: .product-variants — the container rendered only when a product
     * has at least one combination defined in the back-office.
     *
     * @return {@code true} if at least one variant selector element is present,
     *         {@code false} for a simple product with no combinations
     */
    public boolean hasVariantSelectors() {
        boolean found = !driver.findElements(By.cssSelector(".product-variants")).isEmpty();
        logger.info("Variant selectors present on product page: {}", found);
        return found;
    }

    /**
     * Verify product detail page is loaded by waiting for the product title.
     */
    @Override
    public void assertLoaded() {
        logger.info("Verifying product detail page is loaded");
        waitForElement(PRODUCT_NAME);
        logger.info("Product detail page loaded: {}", getProductName());
    }
}
