package com.prestashop.tests.assertions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Custom assertions for general element visibility and state verification.
 * Centralizes reusable assertion logic for verifying element properties.
 */
public class ElementAssertions {
    private static final Logger logger = LoggerFactory.getLogger(ElementAssertions.class);

    /**
     * Assert that an element text value is not null and not empty.
     *
     * @param elementText the text content of the element
     * @param elementName the name/description of the element for error messages
     * @throws AssertionError if text is null or empty
     */
    public static void assertElementTextIsNotEmpty(String elementText, String elementName) {
        logger.info("Asserting element '{}' text is not empty. Value: '{}'", elementName, elementText);
        assertNotNull(elementText, elementName + " text should not be null");
        assertTrue(elementText.length() > 0,
                elementName + " text should not be empty");
    }

    /**
     * Assert that an element is visible on the page.
     *
     * @param isVisible whether the element is visible
     * @param elementName the name/description of the element for error messages
     * @throws AssertionError if element is not visible
     */
    public static void assertElementIsVisible(boolean isVisible, String elementName) {
        logger.info("Asserting element '{}' is visible", elementName);
        assertTrue(isVisible,
                "Expected element '" + elementName + "' to be visible, but it was not displayed");
    }

    /**
     * Assert that a string value is not null and not empty.
     *
     * @param value the string value to verify
     * @param fieldName the name/description of the field for error messages
     * @throws AssertionError if value is null or empty
     */
    public static void assertStringIsNotEmpty(String value, String fieldName) {
        logger.info("Asserting field '{}' is not empty", fieldName);
        assertNotNull(value, fieldName + " should not be null");
        assertTrue(value.length() > 0,
                fieldName + " should not be empty");
    }

    /**
     * Assert that an element attribute matches the expected value.
     *
     * @param actualValue the actual attribute value
     * @param expectedValue the expected attribute value
     * @param attributeName the name of the attribute for error messages
     * @throws AssertionError if values don't match
     */
    public static void assertAttributeEquals(String actualValue, String expectedValue, String attributeName) {
        logger.info("Asserting {} attribute matches. Expected: '{}', Actual: '{}'",
                attributeName, expectedValue, actualValue);
        assertTrue(actualValue.equals(expectedValue),
                "Expected " + attributeName + " to be '" + expectedValue + "', but was: '" + actualValue + "'");
    }
}
