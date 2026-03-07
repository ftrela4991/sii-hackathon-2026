package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.fixtures.PrestashopApiClient;
import com.prestashop.tests.pages.CartModalPage;
import com.prestashop.tests.pages.ProductPage;
import com.prestashop.tests.utils.ConfigLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
 * Test Data Setup:
 * - @BeforeEach creates a dedicated test product via the admin panel API
 * - @AfterEach deletes the product to keep the catalogue clean
 *
 * Each test starts with a fresh WebDriver session (empty cart).
 *
 * Follows Arrange-Act-Assert pattern.
 */
@DisplayName("TC-003: Add Single Product to Cart")
public class AddToCartTest extends BaseTest {

    // Test data constants
    private static final String TEST_PRODUCT_NAME  = "TC-003 Test Product";
    private static final double TEST_PRODUCT_PRICE = 19.99;
    private static final int    TEST_PRODUCT_QTY   = 10;

    // API client for test data setup / cleanup
    private PrestashopApiClient apiClient;
    private long testProductId = -1;

    /**
     * Setup phase: Create a dedicated test product via the admin panel before each test.
     *
     * Using a freshly-created product guarantees:
     * - known name and price (no dependency on pre-existing catalogue)
     * - sufficient stock quantity so Add-to-cart always succeeds
     * - test isolation (no cross-test contamination from other catalogue changes)
     */
    @BeforeEach
    public void setupTestProduct() {
        logger.info("Setting up test product for TC-003");
        apiClient = new PrestashopApiClient();

        testProductId = apiClient.createProduct(TEST_PRODUCT_NAME, TEST_PRODUCT_PRICE, TEST_PRODUCT_QTY);

        if (testProductId > 0) {
            logger.info("Test product created successfully with ID: {}", testProductId);
        } else {
            logger.error("Failed to create test product — test may fail");
        }
    }

    /**
     * Cleanup phase: Delete the test product via the admin panel after each test.
     * Guaranteed to run even if the test itself fails (@AfterEach semantics).
     */
    @AfterEach
    public void cleanupTestProduct() {
        logger.info("Cleaning up test product for TC-003");
        if (testProductId > 0) {
            boolean deleted = apiClient.deleteProduct(testProductId);
            if (deleted) {
                logger.info("Test product {} cleaned up successfully", testProductId);
            } else {
                logger.error("Failed to clean up test product {}", testProductId);
            }
        }
    }

    /**
     * TC-003: Add a single product to the cart from the product detail page.
     *
     * Scenario:
     * 1. Navigate directly to the test product detail page via its canonical URL
     * 2. Save product name and price displayed on the page
     * 3. Click "Add to cart"
     * 4. Verify cart counter in header increments to (1)
     * 5. Verify modal shows the correct product name
     * 6. Verify modal subtotal matches the product price
     */
    @Test
    @DisplayName("Adding a product to cart updates counter and shows correct modal")
    void shouldAddSingleProductToCart() {
        logger.info("Starting TC-003: Add Single Product to Cart");

        // ============================================================
        // ARRANGE: Navigate directly to the test product detail page
        // ============================================================
        // Canonical URL: /index.php?id_product={id}&controller=product
        // This form works regardless of link_rewrite / friendly URLs setting.
        String productUrl = ConfigLoader.getProperty("base.url", "http://145.239.29.235")
                + "/index.php?id_product=" + testProductId + "&controller=product";
        logger.info("Navigating to test product URL: {}", productUrl);
        getDriver().navigate().to(productUrl);

        ProductPage productPage = new ProductPage(getDriver());
        productPage.assertLoaded();

        // Save product name and price before adding to cart
        String expectedProductName = productPage.getProductName();
        String expectedPrice       = productPage.getProductPrice();
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
