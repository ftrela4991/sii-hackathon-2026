package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.pages.CartModalPage;
import com.prestashop.tests.pages.ProductPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for TC-003: Add Single Product to Cart
 *
 * Verifies that a visitor can add a product to the cart from the product
 * detail page. After clicking "Add to cart":
 * - the header cart counter increments to (1)
 * - the confirmation modal shows the correct product name
 * - the modal subtotal matches the product price
 *
 * No API setup required — products exist in the store by default.
 * Each test starts with a fresh WebDriver session (empty cart).
 *
 * Follows Arrange-Act-Assert pattern.
 */
@DisplayName("TC-003: Add Single Product to Cart")
public class AddToCartTest extends BaseTest {

    /**
     * TC-003: Add a single product to the cart from the product detail page.
     *
     * Scenario:
     * 1. Navigate to homepage and open first visible product
     * 2. Save product name and price
     * 3. Click "Add to cart"
     * 4. Verify cart counter in header increments to (1)
     * 5. Verify modal shows correct product name
     * 6. Verify modal subtotal matches product price
     */
    @Test
    @DisplayName("Adding a product to cart updates counter and shows correct modal")
    void shouldAddSingleProductToCart() {
        logger.info("Starting TC-003: Add Single Product to Cart");

        // ============================================================
        // ARRANGE: Navigate to homepage and open first product
        // ============================================================
        navigateToHome();
        logger.info("On homepage — finding first available product");

        // Click first product title link visible in the product listing
        // Selector: .product-miniature .product-title a
        WebElement firstProductLink = getDriver()
                .findElement(By.cssSelector(".product-miniature .product-title a"));
        String productUrl = firstProductLink.getAttribute("href");
        logger.info("Navigating to product: {}", productUrl);
        getDriver().navigate().to(productUrl);

        ProductPage productPage = new ProductPage(getDriver());
        productPage.assertLoaded();

        // Save product name and price before adding to cart
        String expectedProductName = productPage.getProductName();
        String expectedPrice = productPage.getProductPrice();
        logger.info("Product to add — name: '{}', price: '{}'", expectedProductName, expectedPrice);

        // ============================================================
        // ACT: Click "Add to cart"
        // ============================================================
        productPage.clickAddToCart();
        logger.info("Clicked Add to cart — waiting for confirmation modal");

        // ============================================================
        // ASSERT: Verify cart state and modal content
        // ============================================================
        CartModalPage cartModal = new CartModalPage(getDriver());
        cartModal.assertLoaded();
        logger.info("Add-to-cart confirmation modal appeared");

        // R-1: Cart counter in header increments to (1)
        String cartCount = productPage.getCartHeaderCount();
        logger.info("Cart header count: {}", cartCount);
        assertEquals("(1)", cartCount,
                "Expected cart counter to show '(1)' after adding one product, but was: '" + cartCount + "'");
        logger.info("✓ R-1 Assertion passed: cart counter is (1)");

        // R-2: Modal shows correct product name
        String modalProductName = cartModal.getProductName();
        logger.info("Modal product name: '{}'", modalProductName);
        assertTrue(modalProductName.equalsIgnoreCase(expectedProductName),
                "Expected modal to show product name '" + expectedProductName
                        + "', but found: '" + modalProductName + "'");
        logger.info("✓ R-2 Assertion passed: modal shows '{}'", expectedProductName);

        // R-3: Modal subtotal matches product price
        String modalSubtotal = cartModal.getSubtotal();
        logger.info("Modal subtotal: '{}', expected: '{}'", modalSubtotal, expectedPrice);
        assertEquals(expectedPrice, modalSubtotal,
                "Expected modal subtotal to be '" + expectedPrice
                        + "', but was: '" + modalSubtotal + "'");
        logger.info("✓ R-3 Assertion passed: subtotal matches product price '{}'", expectedPrice);

        logger.info("TC-003 completed successfully");
    }
}
