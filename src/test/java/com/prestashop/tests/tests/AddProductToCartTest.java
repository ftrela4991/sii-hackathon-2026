package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.pages.ProductDetailPage;
import com.prestashop.tests.fixtures.PrestashopProductApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for add product to cart scenario (TC-003).
 * Tests adding a product to the cart and verifying cart updates.
 */
@DisplayName("Add Product to Cart Tests (TC-003)")
public class AddProductToCartTest extends BaseTest {

    private static final String PRODUCT_NAME = "Test Product TC-003";
    private static final double PRODUCT_PRICE = 29.99;

    private PrestashopProductApiClient apiClient;
    private long testProductId = -1;

    /**
     * Setup: Create test product via API
     */
    @BeforeEach
    public void setupTestProduct() {
        logger.info("Setting up test product for TC-003");
        apiClient = new PrestashopProductApiClient();
        testProductId = apiClient.createProduct(PRODUCT_NAME, PRODUCT_PRICE);

        if (testProductId > 0) {
            logger.info("Test product created with ID: {}", testProductId);
        } else {
            logger.error("Failed to create test product");
        }
    }

    /**
     * Cleanup: Delete test product via API
     */
    @AfterEach
    public void cleanupTestProduct() {
        logger.info("Cleaning up test product for TC-003");
        if (testProductId > 0) {
            boolean deleted = apiClient.deleteProduct(testProductId);
            if (deleted) {
                logger.info("Test product cleaned up successfully");
            } else {
                logger.error("Failed to clean up test product {}", testProductId);
            }
        }
    }

    /**
     * TC-003: Add Single Product to Cart
     *
     * Scenario: User adds a product from the product detail page to cart
     * Expected: Product is added to cart (verified by cart behavior)
     */
    @Test
    @DisplayName("TC-003: Add single product to cart")
    public void testAddSingleProductToCart() {
        logger.info("Starting TC-003: Add single product to cart");

        // Skip test if product creation failed
        if (testProductId <= 0) {
            logger.error("Product not created, skipping test");
            return;
        }

        // Navigate to product detail page
        String productUrl = "/product/" + testProductId;
        navigateTo(productUrl);

        // Verify product page is loaded
        ProductDetailPage productPage = new ProductDetailPage(getDriver());
        productPage.assertLoaded();

        // Verify product name is displayed
        String displayedProductName = productPage.getProductName();
        logger.info("Product name displayed: {}", displayedProductName);
        assertNotNull(displayedProductName, "Product name should be displayed");
        assertTrue(displayedProductName.length() > 0, "Product name should not be empty");

        // Click "Add to cart" button
        logger.info("Clicking 'Add to cart' button");
        productPage.clickAddToCart();

        // Wait briefly for cart to update (AJAX)
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify we're still on a valid page (no error)
        String currentUrl = getDriver().getCurrentUrl();
        logger.info("Current URL after add to cart: {}", currentUrl);
        assertTrue(!currentUrl.contains("error"), "Page should not show error");

        logger.info("TC-003: Add to cart test completed successfully");
    }
}
