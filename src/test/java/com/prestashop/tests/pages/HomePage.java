package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the Prestashop Home page.
 *
 * Provides UI operations for home page interactions:
 * - Getting cart item count
 * - Getting cart preview information
 * - Getting cart total
 */
public class HomePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    // Cart counter in header
    private static final By CART_COUNTER = By.xpath("//span[@class='cart-products-count']");
    private static final By CART_COUNTER_ALT = By.xpath("//a[@href*='cart']//span[@class='badge']");
    private static final By CART_COUNTER_ALT2 = By.xpath("//div[@id='cart-total']");

    // Cart preview/mini-cart
    private static final By CART_PREVIEW = By.xpath("//div[@class='cart-preview']");
    private static final By CART_PREVIEW_ALT = By.xpath("//div[@id='mini-cart']");
    private static final By CART_PRODUCT_NAME = By.xpath("//div[@class='product-name']");

    // Cart total
    private static final By CART_TOTAL = By.xpath("//span[@class='cart-total']");
    private static final By CART_TOTAL_ALT = By.xpath("//div[@class='cart-summary']//span[@class='total']");

    // Product listing
    private static final By FIRST_PRODUCT_LINK = By.cssSelector(".product-miniature .product-title a");

    // Page verification
    private static final By HOME_PAGE_HEADER = By.xpath("//header");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Get the cart item count from the counter in the header.
     *
     * @return number of items in cart
     */
    public int getCartItemCount() {
        logger.info("Getting cart item count");
        try {
            String countText = getText(CART_COUNTER).trim();
            // Remove non-numeric characters
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
                logger.debug("CART_COUNTER_ALT not found, trying alternative");
                try {
                    String countText = getText(CART_COUNTER_ALT2).trim();
                    countText = countText.replaceAll("[^0-9]", "");
                    if (countText.isEmpty()) {
                        return 0;
                    }
                    return Integer.parseInt(countText);
                } catch (Exception e3) {
                    logger.error("Could not get cart count", e3);
                    return 0;
                }
            }
        }
    }

    /**
     * Get the cart preview text (product names in mini-cart).
     *
     * @return cart preview text
     */
    public String getCartPreviewText() {
        logger.info("Getting cart preview text");
        try {
            return getText(CART_PREVIEW);
        } catch (Exception e) {
            logger.debug("CART_PREVIEW not found, trying alternative");
            try {
                return getText(CART_PREVIEW_ALT);
            } catch (Exception e2) {
                logger.debug("Could not get cart preview text");
                try {
                    return getText(CART_PRODUCT_NAME);
                } catch (Exception e3) {
                    logger.error("Could not get cart preview", e3);
                    return "";
                }
            }
        }
    }

    /**
     * Get the cart total price.
     *
     * @return cart total text
     */
    public String getCartTotal() {
        logger.info("Getting cart total");
        try {
            return getText(CART_TOTAL);
        } catch (Exception e) {
            logger.debug("CART_TOTAL not found, trying alternative");
            try {
                return getText(CART_TOTAL_ALT);
            } catch (Exception e2) {
                logger.error("Could not get cart total", e2);
                return "";
            }
        }
    }

    /**
     * Get the URL of the first visible product on the home page.
     *
     * @return the href attribute of the first product link
     */
    public String getFirstProductUrl() {
        logger.info("Getting first product URL from home page");
        return driver.findElement(FIRST_PRODUCT_LINK).getAttribute("href");
    }

    /**
     * Click on the first visible product on the home page.
     */
    public void clickFirstProduct() {
        logger.info("Clicking first product on home page");
        click(FIRST_PRODUCT_LINK);
    }

    /**
     * Verify that the Home page is loaded.
     */
    @Override
    public void assertLoaded() {
        logger.info("Verifying Home page is loaded");
        waitForElement(HOME_PAGE_HEADER);
        logger.info("Home page has loaded successfully");
    }
}
