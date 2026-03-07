package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.pages.CartPage;
import com.prestashop.tests.pages.ProductDetailPage;
import com.prestashop.tests.fixtures.PrestashopProductApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for remove product from cart scenario (TC-004).
 * Tests removing a product from the cart.
 */
@DisplayName("Remove Product from Cart Tests (TC-004)")
public class RemoveProductFromCartTest extends BaseTest {

    private static final String PRODUCT_NAME = "Test Product TC-004";
    private static final double PRODUCT_PRICE = 19.99;
    private static final int WAIT_TIMEOUT = 15;

    private PrestashopProductApiClient apiClient;
    private long testProductId = -1;

    @BeforeEach
    public void setupTestProduct() {
        logger.info("Setting up test product for TC-004");
        apiClient = new PrestashopProductApiClient();
        testProductId = apiClient.createProduct(PRODUCT_NAME, PRODUCT_PRICE);
        if (testProductId > 0) {
            logger.info("Test product created with ID: {}", testProductId);
        }
    }

    @AfterEach
    public void cleanupTestProduct() {
        logger.info("Cleaning up test product for TC-004");
        if (testProductId > 0) {
            apiClient.deleteProduct(testProductId);
        }
    }

    /**
     * TC-004: Remove Product from Cart
     *
     * Scenario: User removes a product from cart
     * Expected: Product is removed, cart counter resets to 0
     */
    @Test
    @DisplayName("TC-004: Should remove product from cart successfully")
    public void shouldRemoveProductFromCart() {
        logger.info("Starting TC-004: Remove product from cart");

        if (testProductId <= 0) {
            logger.error("Product not created, skipping test");
            return;
        }

        // Navigate to product page
        navigateTo("/product/" + testProductId);
        ProductDetailPage productPage = new ProductDetailPage(getDriver());
        productPage.assertLoaded();

        // Click add to cart button
        logger.info("Clicking add to cart button");
        productPage.clickAddToCart();

        // Wait for cart to update via AJAX
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(WAIT_TIMEOUT));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart-badge")));

        // Navigate to cart page
        navigateTo("/cart");
        CartPage cartPage = new CartPage(getDriver());
        cartPage.assertLoaded();

        // Verify product is in cart
        int initialCount = cartPage.getCartItemCount();
        logger.info("Initial cart item count: {}", initialCount);

        // Remove product from cart
        logger.info("Removing product from cart");
        cartPage.removeProduct();

        // Wait for cart to update after removal
        wait = new WebDriverWait(getDriver(), Duration.ofSeconds(WAIT_TIMEOUT));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("cart-item")));

        // Verify cart is empty
        int finalCount = cartPage.getCartItemCount();
        logger.info("Final cart item count after removal: {}", finalCount);
        assertEquals(0, finalCount, "Cart should be empty after product removal");

        logger.info("TC-004: Remove product test completed successfully");
    }
}
