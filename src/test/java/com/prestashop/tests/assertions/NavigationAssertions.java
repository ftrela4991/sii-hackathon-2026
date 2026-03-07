package com.prestashop.tests.assertions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Custom assertions for page navigation and URL verification.
 * Centralizes reusable assertion logic for verifying page loads and navigation.
 */
public class NavigationAssertions {
    private static final Logger logger = LoggerFactory.getLogger(NavigationAssertions.class);

    /**
     * Assert that the current URL is not null and not empty.
     *
     * @param currentUrl the current page URL
     * @throws AssertionError if URL is null or empty
     */
    public static void assertCurrentUrlIsValid(String currentUrl) {
        logger.info("Asserting current URL is valid: {}", currentUrl);
        assertTrue(currentUrl != null && currentUrl.length() > 0,
                "Current URL should not be null or empty");
    }

    /**
     * Assert that the page does not contain an error state.
     *
     * @param currentUrl the current page URL
     * @throws AssertionError if URL contains "error"
     */
    public static void assertPageHasNoError(String currentUrl) {
        logger.info("Asserting page has no error. URL: {}", currentUrl);
        assertFalse(currentUrl.contains("error"),
                "Page should not show error, but URL was: " + currentUrl);
    }

    /**
     * Assert that page title is present and not empty.
     *
     * @param pageTitle the page title
     * @throws AssertionError if title is null or empty
     */
    public static void assertPageTitleIsValid(String pageTitle) {
        logger.info("Asserting page title is valid: {}", pageTitle);
        assertTrue(pageTitle != null && pageTitle.length() > 0,
                "Page title should be available and not empty");
    }

    /**
     * Assert that the current URL matches the expected base URL.
     *
     * @param currentUrl the current page URL
     * @param expectedUrl the expected URL
     * @throws AssertionError if URLs don't match
     */
    public static void assertCurrentUrlMatches(String currentUrl, String expectedUrl) {
        logger.info("Asserting URL matches. Current: '{}', Expected: '{}'", currentUrl, expectedUrl);
        boolean matches = currentUrl.equals(expectedUrl) ||
                         currentUrl.equals(expectedUrl + "/") ||
                         currentUrl.equals(expectedUrl.replaceAll("/$", ""));
        assertTrue(matches,
                "Expected URL to be '" + expectedUrl + "', but was: " + currentUrl);
    }
}
