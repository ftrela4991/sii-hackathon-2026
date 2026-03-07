package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.pages.CartModalPage;
import com.prestashop.tests.pages.HomePage;
import com.prestashop.tests.pages.ProductPage;
import com.prestashop.tests.assertions.CartAssertions;
import com.prestashop.tests.assertions.AuthenticationAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TC-003: Add Single Product to Cart")
public class AddToCartTest extends BaseTest {

    @Test
    @DisplayName("Adding a product to cart updates counter and shows correct modal")
    void shouldAddSingleProductToCart() {
        navigateToHome();

        HomePage homePage = new HomePage(getDriver());
        String productUrl = homePage.getFirstProductUrl();
        getDriver().navigate().to(productUrl);

        ProductPage productPage = new ProductPage(getDriver());
        productPage.assertLoaded();

        String expectedProductName = productPage.getProductName();
        String expectedPrice = productPage.getProductPrice();

        productPage.clickAddToCart();

        CartModalPage cartModal = new CartModalPage(getDriver());
        cartModal.assertLoaded();

        CartAssertions.assertCartCount(productPage.getCartHeaderCount(), "(1)");
        AuthenticationAssertions.assertModalProductName(cartModal.getProductName(), expectedProductName);
        AuthenticationAssertions.assertModalPrice(cartModal.getSubtotal(), expectedPrice);
    }
}
