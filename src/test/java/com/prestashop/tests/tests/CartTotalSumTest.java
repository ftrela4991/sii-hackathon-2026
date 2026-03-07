package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.fixtures.PrestashopApiClient;
import com.prestashop.tests.pages.CartPage;
import com.prestashop.tests.pages.ProductDetailPage;
import com.prestashop.tests.assertions.CartAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("TC-023: Verify Cart Total Sum is Correct")
public class CartTotalSumTest extends BaseTest {

    private static final double PRODUCT_A_PRICE = 20.00;
    private static final double PRODUCT_B_PRICE = 15.00;

    private PrestashopApiClient apiClient;
    private long productAId = -1;
    private long productBId = -1;

    @BeforeEach
    void setupProducts() {
        apiClient = new PrestashopApiClient();
        productAId = apiClient.createProduct("TC-023 Product A", PRODUCT_A_PRICE);
        productBId = apiClient.createProduct("TC-023 Product B", PRODUCT_B_PRICE);
    }

    @AfterEach
    void cleanupProducts() {
        if (productAId > 0) apiClient.deleteProduct(productAId);
        if (productBId > 0) apiClient.deleteProduct(productBId);
    }

    @Test
    @DisplayName("TC-023: Grand total equals sum of all line item subtotals")
    void shouldCalculateCartGrandTotalCorrectly() {
        // Add Product A — click add to cart twice to get quantity 2
        navigateTo("/?id_product=" + productAId + "&controller=product");
        ProductDetailPage productPage = new ProductDetailPage(getDriver());
        productPage.assertLoaded();
        productPage.clickAddToCart();
        productPage.waitForCartBadge();

        navigateTo("/?id_product=" + productAId + "&controller=product");
        productPage.assertLoaded();
        productPage.clickAddToCart();
        productPage.waitForCartBadge();

        // Add Product B — quantity 1
        navigateTo("/?id_product=" + productBId + "&controller=product");
        productPage.assertLoaded();
        productPage.clickAddToCart();
        productPage.waitForCartBadge();

        navigateTo("/cart?action=show");
        CartPage cartPage = new CartPage(getDriver());
        cartPage.assertLoaded();
        cartPage.waitForLineTotalsCount(2);

        List<String> lineTotals = cartPage.getLineTotals();
        String productsSubtotal = cartPage.getProductsSubtotal();

        CartAssertions.assertGrandTotalMatchesLineItemSum(productsSubtotal, lineTotals);
    }
}
