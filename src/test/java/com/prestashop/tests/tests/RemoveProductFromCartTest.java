package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.pages.CartPage;
import com.prestashop.tests.pages.ProductDetailPage;
import com.prestashop.tests.fixtures.PrestashopProductApiClient;
import com.prestashop.tests.assertions.CartAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for remove product from cart scenario (TC-004).
 * Tests removing a product from the cart.
 */
@DisplayName("Remove Product from Cart Tests (TC-004)")
public class RemoveProductFromCartTest extends BaseTest {

    private static final String PRODUCT_NAME = "Test Product TC-004";
    private static final double PRODUCT_PRICE = 19.99;

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
        productPage.waitForCartBadge();

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
        cartPage.waitForCartItemsToDisappear();

        // Verify cart is empty
        int finalCount = cartPage.getCartItemCount();
        logger.info("Final cart item count after removal: {}", finalCount);
        CartAssertions.assertCartIsEmpty(finalCount);

        logger.info("TC-004: Remove product test completed successfully");
    }
}
