package com.prestashop.tests.assertions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Custom assertions for shopping cart verification.
 * Centralizes reusable assertion logic for cart operations, quantities, and totals.
 */
public class CartAssertions {
    private static final Logger logger = LoggerFactory.getLogger(CartAssertions.class);

    /**
     * Assert that the cart counter in the header shows the expected value.
     *
     * @param actualCount the actual cart count displayed
     * @param expectedCount the expected cart count
     * @throws AssertionError if counts don't match
     */
    public static void assertCartCount(String actualCount, String expectedCount) {
        logger.info("Asserting cart count. Expected: '{}', Actual: '{}'", expectedCount, actualCount);
        assertEquals(expectedCount, actualCount,
                "Expected cart counter to show '" + expectedCount + "', but was: '" + actualCount + "'");
    }

    /**
     * Assert that the cart count is exactly zero (empty).
     *
     * @param cartItemCount the current item count in cart
     * @throws AssertionError if cart is not empty
     */
    public static void assertCartIsEmpty(int cartItemCount) {
        logger.info("Asserting cart is empty. Current count: {}", cartItemCount);
        assertEquals(0, cartItemCount,
                "Cart should be empty after product removal, but had " + cartItemCount + " items");
    }

    /**
     * Assert that the cart has the expected number of items.
     *
     * @param actualCount the actual number of items in cart
     * @param expectedCount the expected number of items
     * @throws AssertionError if counts don't match
     */
    public static void assertCartItemCount(int actualCount, int expectedCount) {
        logger.info("Asserting cart item count. Expected: {}, Actual: {}", expectedCount, actualCount);
        assertEquals(expectedCount, actualCount,
                "Expected cart to contain " + expectedCount + " items, but had: " + actualCount);
    }

    /**
     * Assert that the cart subtotal matches the expected price.
     *
     * @param actualSubtotal the actual subtotal displayed in cart
     * @param expectedPrice the expected subtotal price
     * @throws AssertionError if subtotals don't match
     */
    public static void assertCartSubtotal(String actualSubtotal, String expectedPrice) {
        logger.info("Asserting cart subtotal. Expected: '{}', Actual: '{}'", expectedPrice, actualSubtotal);
        assertEquals(expectedPrice, actualSubtotal,
                "Expected cart subtotal to be '" + expectedPrice + "', but was: '" + actualSubtotal + "'");
    }

    /**
     * Assert that a product is present in the cart based on its name.
     *
     * @param isPresent whether the product is present in cart
     * @param productName the name of the product to verify
     * @throws AssertionError if product is not found in cart
     */
    public static void assertGrandTotalMatchesLineItemSum(String grandTotal, List<String> lineTotals) {
        double expectedSum = lineTotals.stream()
                .mapToDouble(CartAssertions::parseCurrencyAmount)
                .sum();
        double actualGrandTotal = parseCurrencyAmount(grandTotal);
        assertEquals(expectedSum, actualGrandTotal, 0.01,
                "Grand total '" + grandTotal + "' should equal sum of line totals " + lineTotals);
    }

    public static void assertLineTotal(String actual, String expected) {
        assertEquals(expected, actual,
                "Expected line total '" + expected + "', but was: '" + actual + "'");
    }

    private static double parseCurrencyAmount(String value) {
        return Double.parseDouble(value.replaceAll("[^0-9.]", ""));
    }

    public static void assertProductInCart(boolean isPresent, String productName) {
        logger.info("Asserting product '{}' is in cart", productName);
        assertTrue(isPresent,
                "Expected product '" + productName + "' to be in cart, but it was not found");
    }
}
