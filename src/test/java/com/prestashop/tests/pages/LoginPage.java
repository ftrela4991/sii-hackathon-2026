package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the Prestashop Login page (/login).
 *
 * Provides UI operations for user authentication:
 * - Entering email and password
 * - Clicking sign-in button
 * - Verifying page is loaded
 */
public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // Locators for page elements (prefer id/data-testid for stability)
    private static final By EMAIL_INPUT = By.id("field-email");
    private static final By PASSWORD_INPUT = By.id("field-password");
    private static final By SIGNIN_BUTTON = By.id("submit-login");
    // Fallback to h1 if no data-testid available (page verification)
    private static final By LOGIN_HEADER = By.cssSelector("h1.page-title");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter email address into the email input field.
     *
     * @param email the email address to enter
     */
    public void enterEmail(String email) {
        logger.info("Entering email: {}", email);
        sendKeys(EMAIL_INPUT, email);
    }

    /**
     * Enter password into the password input field.
     *
     * @param password the password to enter
     */
    public void enterPassword(String password) {
        logger.info("Entering password (length: {})", password.length());
        sendKeys(PASSWORD_INPUT, password);
    }

    /**
     * Click the Sign in button to submit the login form.
     */
    public void clickSignIn() {
        logger.info("Clicking Sign in button");
        click(SIGNIN_BUTTON);
    }

    /**
     * Perform login with email and password.
     *
     * @param email the email address to login with
     * @param password the password to login with
     */
    public void login(String email, String password) {
        logger.info("Performing login with email: {}", email);
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
    }

    /**
     * Verify that the Login page is loaded.
     */
    @Override
    public void assertLoaded() {
        logger.info("Verifying Login page is loaded");
        waitForElement(LOGIN_HEADER);
        logger.info("Login page has loaded successfully");
    }
}
