package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.fixtures.PrestashopApiClient;
import com.prestashop.tests.pages.ProductPage;
import com.prestashop.tests.utils.ConfigLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for TC-006: Create Standard Product (No Variants)
 *
 * Verifies that a standard product created via the admin panel:
 * - is visible on the storefront with the correct name
 * - displays the correct retail price ("£29.99")
 * - does NOT show any variant/combination selectors
 *
 * Test Data Setup:
 * - @BeforeEach creates a unique product via the admin panel API
 *   (name includes a short UUID to prevent conflicts with failed previous runs)
 * - @AfterEach deletes the product via the admin panel API
 *
 * Each test starts with a fresh WebDriver session.
 * Follows Arrange-Act-Assert pattern.
 */
@DisplayName("TC-006: Create Standard Product (No Variants)")
public class CreateProductTest extends BaseTest {

    // Test data constants
    private static final double TEST_PRODUCT_PRICE    = 29.99;
    private static final int    TEST_PRODUCT_QTY      = 50;
    private static final String EXPECTED_PRICE_DISPLAY = "£29.99";

    // API client for test data setup / cleanup
    private PrestashopApiClient apiClient;
    private long   testProductId   = -1;
    private String testProductName;

    /**
     * Setup phase: Create a unique test product via the admin panel before each test.
     *
     * A short UUID suffix is appended to the product name so that each test run
     * uses a distinct name — preventing false-positive matches against products
     * left over from previously failed test runs.
     */
    @BeforeEach
    public void setupTestProduct() {
        logger.info("Setting up test product for TC-006");
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        testProductName = "Test Simple Product " + uuid;

        apiClient = new PrestashopApiClient();
        testProductId = apiClient.createProduct(testProductName, TEST_PRODUCT_PRICE, TEST_PRODUCT_QTY);

        if (testProductId > 0) {
            logger.info("Test product created: '{}' (ID: {})", testProductName, testProductId);
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
        logger.info("Cleaning up test product for TC-006");
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
     * TC-006: Standard product created via admin panel is visible on the storefront
     * with the correct name, price, and without any variant/combination selectors.
     *
     * Scenario:
     * 1. Product is created in @BeforeEach via admin panel API
     *    (name: "Test Simple Product {uuid}", price: 29.99, qty: 50)
     * 2. Navigate directly to the product detail page via canonical URL
     * 3. Verify the displayed product name matches the created product
     * 4. Verify the displayed price is "£29.99"
     * 5. Verify no variant/combination selectors are present on the page
     */
    @Test
    @DisplayName("Standard product is visible on storefront with correct name, price, and no variant selectors")
    void shouldDisplaySimpleProductWithoutVariants() {
        logger.info("Starting TC-006: Create Standard Product (No Variants)");

        // ============================================================
        // ARRANGE: Verify product was created successfully in @BeforeEach
        // ============================================================
        assertTrue(testProductId > 0,
                "Pre-condition failed: test product was not created (ID=" + testProductId + ")");

        // ============================================================
        // ACT: Navigate to the product detail page on the storefront.
        // Canonical URL is used so the test works regardless of whether
        // friendly URLs / link_rewrite are enabled on the store.
        // ============================================================
        String productUrl = ConfigLoader.getProperty("base.url", "http://145.239.29.235")
                + "/index.php?id_product=" + testProductId + "&controller=product";
        logger.info("Navigating to product URL: {}", productUrl);
        getDriver().navigate().to(productUrl);

        ProductPage productPage = new ProductPage(getDriver());
        productPage.assertLoaded();

        // ============================================================
        // ASSERT
        // ============================================================

        // R-1: Storefront shows the correct product name
        String displayedName = productPage.getProductName();
        logger.info("Displayed product name: '{}'", displayedName);
        assertTrue(displayedName.equalsIgnoreCase(testProductName),
                "Expected product name '" + testProductName
                        + "', but storefront showed: '" + displayedName + "'");
        logger.info("✓ R-1 Assertion passed: product name is '{}'", displayedName);

        // R-2: Storefront shows the correct retail price
        String displayedPrice = productPage.getProductPrice();
        logger.info("Displayed product price: '{}'", displayedPrice);
        assertEquals(EXPECTED_PRICE_DISPLAY, displayedPrice,
                "Expected price '" + EXPECTED_PRICE_DISPLAY
                        + "', but storefront showed: '" + displayedPrice + "'");
        logger.info("✓ R-2 Assertion passed: price is '{}'", EXPECTED_PRICE_DISPLAY);

        // R-3: No variant/combination selectors rendered (standard product, no combinations)
        boolean hasVariants = productPage.hasVariantSelectors();
        logger.info("Variant selectors present: {}", hasVariants);
        assertFalse(hasVariants,
                "Expected no variant/combination selectors for a standard product, "
                        + "but .product-variants was found on the page");
        logger.info("✓ R-3 Assertion passed: no variant selectors present");

        logger.info("TC-006 completed successfully");
    }
}
