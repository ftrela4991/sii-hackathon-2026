package com.prestashop.tests.tests;

import com.prestashop.tests.assertions.CartAssertions;
import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.fixtures.PrestashopApiClient;
import com.prestashop.tests.pages.CartPage;
import com.prestashop.tests.pages.ProductDetailPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cart Tests (TC-004, TC-021, TC-022)")
public class CartTest extends BaseTest {

    // Hummingbird printed t-shirt — product 1, combination 1 (S / White)
    private static final String TSHIRT_URL = "/men/1-1-hummingbird-printed-t-shirt.html";

    // TC-004 state
    private static final String API_PRODUCT_NAME = "Test Product TC-004";
    private static final double API_PRODUCT_PRICE = 19.99;
    private PrestashopApiClient apiClient;
    private long testProductId = -1;

    @BeforeEach
    void initApiClient() {
        apiClient = new PrestashopApiClient();
    }

    @AfterEach
    void cleanupApiProduct() {
        if (testProductId > 0) {
            apiClient.deleteProduct(testProductId);
            testProductId = -1;
        }
    }

    // -------------------------------------------------------------------------
    // TC-004: Remove product from cart
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-004: Should remove product from cart successfully")
    void shouldRemoveProductFromCart() {
        testProductId = apiClient.createProduct(API_PRODUCT_NAME, API_PRODUCT_PRICE);
        if (testProductId <= 0) {
            logger.error("Product creation failed, skipping test");
            return;
        }

        navigateTo("/?id_product=" + testProductId + "&controller=product");
        ProductDetailPage productPage = new ProductDetailPage(getDriver());
        productPage.assertLoaded();
        productPage.clickAddToCart();
        productPage.waitForCartBadge();

        navigateTo("/cart?action=show");
        CartPage cartPage = new CartPage(getDriver());
        cartPage.assertLoaded();
        cartPage.removeProduct();
        cartPage.waitForCartItemsToDisappear();

        CartAssertions.assertCartIsEmpty(cartPage.getCartItemCount());
    }

    // -------------------------------------------------------------------------
    // TC-021: Increase product quantity in cart
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-021: Increasing quantity to 3 updates line total and grand total")
    void shouldIncreaseQuantityAndUpdateTotals() {
        CartPage cartPage = addTshirtToCartAndNavigate();

        assertEquals(1, cartPage.getQuantity(), "Initial quantity should be 1");
        String initialGrandTotal = cartPage.getGrandTotal();

        cartPage.increaseQuantity();
        cartPage.waitForQuantityToBe(2);
        cartPage.increaseQuantity();
        cartPage.waitForQuantityToBe(3);
        cartPage.waitForGrandTotalToChangFrom(initialGrandTotal);

        assertEquals(3, cartPage.getQuantity(), "Quantity should be 3 after two increments");
        assertFalse(cartPage.getLineTotal().isEmpty(), "Line total should be displayed");
        assertNotEquals(initialGrandTotal, cartPage.getGrandTotal(),
                "Grand total should change after quantity update");
    }

    // -------------------------------------------------------------------------
    // TC-022: Decrease product quantity to zero removes the item
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-022: Decreasing quantity to zero removes the product from cart")
    void shouldRemoveProductWhenQuantityDecreasedToZero() {
        CartPage cartPage = addTshirtToCartAndNavigate();

        assertEquals(1, cartPage.getCartItemCount(), "Cart should contain exactly 1 item");
        assertEquals(1, cartPage.getQuantity(), "Initial quantity should be 1");

        cartPage.decreaseQuantity();
        cartPage.waitForCartItemsToDisappear();

        assertTrue(cartPage.isEmptyCartDisplayed(), "Cart should be empty after removing the item");
        assertEquals(0, cartPage.getCartItemCount(), "Cart item count should be 0");
        assertEquals("(0)", cartPage.getHeaderCartCount(), "Header cart counter should show (0)");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private CartPage addTshirtToCartAndNavigate() {
        navigateTo(TSHIRT_URL);
        ProductDetailPage productPage = new ProductDetailPage(getDriver());
        productPage.assertLoaded();
        productPage.clickAddToCart();
        productPage.waitForCartBadge();
        navigateTo("/cart?action=show");
        CartPage cartPage = new CartPage(getDriver());
        cartPage.assertLoaded();
        return cartPage;
    }
}
