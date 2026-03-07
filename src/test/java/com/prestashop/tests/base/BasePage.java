package com.prestashop.tests.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.prestashop.tests.utils.ConfigLoader;

import java.time.Duration;

/**
 * Base class for all page objects.
 * Provides common WebDriver operations and element waits.
 * Page objects should extend this class and define locators as By constants.
 *
 * IMPORTANT: Page objects contain ONLY UI operations and element locators.
 * Business assertions belong in test classes, not here.
 * Exception: assertLoaded() is allowed to verify page state (e.g., header visible).
 *
 * THREAD SAFETY: This class is NOT thread-safe if the same BasePage instance
 * is shared across threads. Each thread must instantiate its own page object
 * with its own ThreadLocal WebDriver instance.
 */
public class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor accepts a WebDriver instance from the test.
     * @param driver the WebDriver instance (must not be null)
     * @throws IllegalArgumentException if driver is null
     */
    public BasePage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver cannot be null");
        }
        this.driver = driver;
        // Initialize explicit wait with timeout from config
        int waitTimeout = ConfigLoader.getIntProperty("timeout.explicit.wait", 15);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeout));
    }

    /**
     * Wait for an element to be present and visible, then return it.
     */
    protected WebElement waitForElement(By locator) {
        logger.debug("Waiting for element: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Click an element after waiting for it to be visible.
     */
    public void click(By locator) {
        logger.debug("Clicking element: {}", locator);
        waitForElement(locator).click();
    }

    /**
     * Send text to an element (clears field first to ensure clean input).
     * Clears any existing content before typing to prevent input pollution
     * from previous test data.
     * @param locator the element to send keys to
     * @param text the text to send (must not be null)
     */
    public void sendKeys(By locator, String text) {
        if (text == null) {
            logger.warn("Text parameter is null for locator: {}", locator);
            return;
        }
        logger.debug("Sending keys to element: {} with text: {}", locator, text);
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get the text content of an element.
     */
    public String getText(By locator) {
        logger.debug("Getting text from element: {}", locator);
        return waitForElement(locator).getText();
    }

    /**
     * Check if an element is present and visible.
     * @param locator the element locator
     * @return true if the element is visible, false otherwise
     */
    public boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)) != null;
        } catch (TimeoutException e) {
            logger.debug("Element not visible: {}", locator);
            return false;
        }
    }

    /**
     * Wait for an element to be clickable.
     */
    public void waitForClickable(By locator) {
        logger.debug("Waiting for element to be clickable: {}", locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Navigate to a URL.
     * @param url the URL to navigate to (must not be null or empty)
     */
    public void navigateTo(String url) {
        if (url == null || url.trim().isEmpty()) {
            logger.warn("URL cannot be null or empty");
            return;
        }
        logger.debug("Navigating to URL: {}", url);
        driver.navigate().to(url);
    }

    /**
     * Get the current page URL.
     * @return the current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get the current page title.
     * @return the current page title (from &lt;title&gt; tag)
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Verify that the page has loaded (override in subclasses).
     * This is the ONLY assertion allowed in page objects.
     *
     * Example:
     * public void assertLoaded() {
     *     waitForElement(HEADER_LOGO); // verify page header is visible
     * }
     */
    public void assertLoaded() {
        // Default implementation: do nothing
        // Subclasses should override to verify page-specific elements
        logger.debug("Page loaded (assertLoaded called)");
    }
}
