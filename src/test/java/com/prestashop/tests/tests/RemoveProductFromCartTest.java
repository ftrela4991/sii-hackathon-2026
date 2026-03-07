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

@DisplayName("Remove Product from Cart Tests (TC-004)")
public class RemoveProductFromCartTest extends BaseTest {

    private static final String PRODUCT_NAME = "Test Product TC-004";
    private static final double PRODUCT_PRICE = 19.99;

    private PrestashopProductApiClient apiClient;
    private long testProductId = -1;

    @BeforeEach
    public void setupTestProduct() {
        apiClient = new PrestashopProductApiClient();
        testProductId = apiClient.createProduct(PRODUCT_NAME, PRODUCT_PRICE);
    }

    @AfterEach
    public void cleanupTestProduct() {
        if (testProductId > 0) {
            apiClient.deleteProduct(testProductId);
        }
    }

    @Test
    @DisplayName("TC-004: Should remove product from cart successfully")
    public void shouldRemoveProductFromCart() {
        if (testProductId <= 0) {
            logger.error("Product creation failed");
            return;
        }

        navigateTo("/product/" + testProductId);
        ProductDetailPage productPage = new ProductDetailPage(getDriver());
        productPage.assertLoaded();

        productPage.clickAddToCart();
        productPage.waitForCartBadge();

        navigateTo("/cart");
        CartPage cartPage = new CartPage(getDriver());
        cartPage.assertLoaded();

        cartPage.removeProduct();
        cartPage.waitForCartItemsToDisappear();

        CartAssertions.assertCartIsEmpty(cartPage.getCartItemCount());
    }
}
