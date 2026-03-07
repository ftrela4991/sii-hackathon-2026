package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the add-to-cart confirmation modal (#blockcart-modal).
 *
 * This modal appears asynchronously (AJAX) after clicking "Add to cart"
 * on any product page. It shows product name, price, cart item count
 * and the cart subtotal.
 *
 * Selectors verified against live app: http://145.239.29.235
 *
 * Example modal structure:
 *   #blockcart-modal
 *     h4.modal-title        — "Product successfully added to your shopping cart"
 *     h6.product-name       — product name
 *     p.product-price       — product price (e.g. "£22.94")
 *     p.cart-products-count — "There is 1 item in your cart."
 *     span.subtotal.value   — subtotal (e.g. "£22.94")
 */
public class CartModalPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CartModalPage.class);

    // Modal root — appears after AJAX add-to-cart
    private static final By MODAL = By.id("blockcart-modal");

    // Product name inside modal
    private static final By PRODUCT_NAME = By.cssSelector("#blockcart-modal h6.product-name");

    // Product price inside modal
    private static final By PRODUCT_PRICE = By.cssSelector("#blockcart-modal p.product-price");

    // "There is 1 item in your cart." text
    private static final By CART_ITEM_COUNT_TEXT = By.cssSelector("#blockcart-modal p.cart-products-count");

    // Subtotal value (matches product price for single item, no promotions)
    private static final By SUBTOTAL = By.cssSelector("#blockcart-modal .subtotal.value");

    public CartModalPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Get the product name displayed inside the modal.
     *
     * @return product name text
     */
    public String getProductName() {
        String name = waitForElement(PRODUCT_NAME).getText().trim();
        logger.info("Modal product name: {}", name);
        return name;
    }

    /**
     * Get the product price displayed inside the modal (e.g. "£22.94").
     *
     * @return price text with currency symbol
     */
    public String getProductPrice() {
        String price = waitForElement(PRODUCT_PRICE).getText().trim();
        logger.info("Modal product price: {}", price);
        return price;
    }

    /**
     * Get the cart item count text from inside the modal,
     * e.g. "There is 1 item in your cart."
     *
     * @return full item count sentence
     */
    public String getCartItemCountText() {
        String text = waitForElement(CART_ITEM_COUNT_TEXT).getText().trim();
        logger.info("Modal cart item count text: {}", text);
        return text;
    }

    /**
     * Get the subtotal value displayed in the modal (e.g. "£22.94").
     *
     * @return subtotal text with currency symbol
     */
    public String getSubtotal() {
        String subtotal = waitForElement(SUBTOTAL).getText().trim();
        logger.info("Modal subtotal: {}", subtotal);
        return subtotal;
    }

    /**
     * Verify the modal is visible. Waits for #blockcart-modal to appear
     * (it is injected/shown via AJAX after clicking Add to cart).
     */
    @Override
    public void assertLoaded() {
        logger.info("Waiting for add-to-cart modal to become visible");
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL));
        logger.info("Add-to-cart modal is visible");
    }
}
