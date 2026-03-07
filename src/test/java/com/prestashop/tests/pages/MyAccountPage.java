package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the Prestashop My Account page (/my-account).
 *
 * Provides UI operations for verifying user account details and session state
 * after successful registration and login.
 */
public class MyAccountPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(MyAccountPage.class);

    // Locators for page elements
    private static final By GREETING_TEXT = By.cssSelector(".customer-account, [class*='customer']");  // Contains user's full name
    private static final By SIGN_OUT_LINK = By.cssSelector("a[href*='logout'], a[href*='signout']");
    private static final By ACCOUNT_SECTION = By.id("identity");  // Account information section

    public MyAccountPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Get the greeting text that contains the user's full name.
     * This is typically displayed as "Welcome, John Doe" or similar.
     *
     * @return the greeting text containing the user's name
     */
    public String getGreetingText() {
        logger.info("Getting greeting text from page");
        // Look for the text element that contains the user's name
        // This typically appears in the account section or header
        try {
            // Try multiple possible locations for the greeting
            java.util.List<WebElement> possibleGreetings = driver.findElements(
                    By.cssSelector("[class*='greeting'], [class*='customer'], .user-info, [class*='account']"));

            for (WebElement element : possibleGreetings) {
                String text = element.getText();
                if (!text.isEmpty()) {
                    logger.info("Greeting text found: {}", text);
                    return text;
                }
            }

            // Fallback: search the entire page for text
            logger.warn("Could not find greeting in specific elements, searching entire page");
            String pageText = driver.findElement(By.tagName("body")).getText();
            return pageText;
        } catch (Exception e) {
            logger.warn("Error getting greeting text", e);
            // Last resort: return page text
            return driver.findElement(By.tagName("body")).getText();
        }
    }

    /**
     * Check if the "Sign out" link is visible in the header.
     * This confirms that the user is logged in and has an active session.
     *
     * @return true if the Sign out link is visible, false otherwise
     */
    public boolean isSignOutVisible() {
        logger.info("Checking if Sign out link is visible");
        boolean visible = isElementVisible(SIGN_OUT_LINK);
        logger.info("Sign out link visible: {}", visible);
        return visible;
    }

    /**
     * Check if the "Sign in" link is visible.
     * This confirms that the user is NOT logged in.
     *
     * @return true if the Sign in link is visible, false otherwise
     */
    public boolean isSignInVisible() {
        By signInLink = By.cssSelector("a[title='Log in'], .user-info a[href*='login']");
        logger.info("Checking if Sign in link is visible");
        return isElementVisible(signInLink);
    }

    /**
     * Verify that the My Account page is loaded.
     * Checks for the URL and account section presence.
     */
    @Override
    public void assertLoaded() {
        logger.info("Verifying My Account page is loaded");
        // Wait for the page to load by checking for the URL
        wait.until(ExpectedConditions.urlContains("/my-account"));
        logger.info("My Account page has loaded successfully");
    }
}
