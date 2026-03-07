package com.prestashop.tests.assertions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Custom assertions for authentication and user account verification.
 * Centralizes reusable assertion logic for login, registration, and user profile tests.
 */
public class AuthenticationAssertions {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAssertions.class);

    /**
     * Assert that the Sign out link is visible (user is authenticated).
     *
     * @param isVisible whether the sign-out link is visible
     * @throws AssertionError if sign-out link is not visible
     */
    public static void assertUserIsAuthenticated(boolean isVisible) {
        logger.info("Asserting user is authenticated (Sign out link visible)");
        assertTrue(isVisible,
                "Expected Sign out link to be visible (authenticated state), but it was not displayed");
    }

    /**
     * Assert that the user name matches the expected value in the header.
     *
     * @param actualName the actual user name displayed in header
     * @param expectedName the expected user name
     * @throws AssertionError if names don't match
     */
    public static void assertUserNameInHeader(String actualName, String expectedName) {
        logger.info("Asserting user name matches. Expected: '{}', Actual: '{}'", expectedName, actualName);
        assertTrue(actualName.equalsIgnoreCase(expectedName),
                "Expected header to display '" + expectedName + "' after registration, but found: '" + actualName + "'");
    }

    /**
     * Assert that user remains on homepage after authentication action.
     *
     * @param currentUrl the current page URL
     * @param baseUrl the expected base URL
     * @throws AssertionError if URL doesn't match homepage
     */
    public static void assertUserOnHomepage(String currentUrl, String baseUrl) {
        logger.info("Asserting user is on homepage. Current: '{}', Expected: '{}'", currentUrl, baseUrl);
        boolean isHomepage = currentUrl.equals(baseUrl + "/") || currentUrl.equals(baseUrl.replaceAll("/$", ""));
        assertTrue(isHomepage,
                "Expected user to remain on homepage '" + baseUrl + "' after authentication, but was: " + currentUrl);
    }

    /**
     * Assert that a modal product name matches the expected product name.
     *
     * @param actualName the actual product name in modal
     * @param expectedName the expected product name
     * @throws AssertionError if names don't match
     */
    public static void assertModalProductName(String actualName, String expectedName) {
        logger.info("Asserting modal product name matches. Expected: '{}', Actual: '{}'", expectedName, actualName);
        assertTrue(actualName.equalsIgnoreCase(expectedName),
                "Expected modal to show product name '" + expectedName + "', but found: '" + actualName + "'");
    }

    /**
     * Assert that a modal price matches the expected price.
     *
     * @param actualPrice the actual price in modal
     * @param expectedPrice the expected price
     * @throws AssertionError if prices don't match
     */
    public static void assertModalPrice(String actualPrice, String expectedPrice) {
        logger.info("Asserting modal price matches. Expected: '{}', Actual: '{}'", expectedPrice, actualPrice);
        assertTrue(actualPrice.equals(expectedPrice),
                "Expected modal price to be '" + expectedPrice + "', but was: '" + actualPrice + "'");
    }
}
