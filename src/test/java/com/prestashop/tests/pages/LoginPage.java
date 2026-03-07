package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the Prestashop Login page (/login).
 *
 * Provides UI operations for user authentication:
 * - Entering email and password
 * - Clicking sign-in button
 * - Verifying page is loaded
 *
 * This page object follows POM best practices:
 * - All locators are private static final By constants
 * - All UI operations are public methods with clear names
 * - No business assertions (only assertLoaded for page state verification)
 */
public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // Locators for page elements
    // Email input field (commonly has id or name 'email', 'login', etc.)
    private static final By EMAIL_INPUT = By.id("customer-email");
    private static final By EMAIL_INPUT_ALT1 = By.name("email");
    private static final By EMAIL_INPUT_ALT2 = By.xpath("//input[@type='email']");

    // Password input field
    private static final By PASSWORD_INPUT = By.id("customer-password");
    private static final By PASSWORD_INPUT_ALT1 = By.name("password");
    private static final By PASSWORD_INPUT_ALT2 = By.xpath("//input[@type='password']");

    // Sign-in button (commonly has text 'Sign in', 'Login', 'Submit', etc.)
    private static final By SIGNIN_BUTTON = By.id("submit-login");
    private static final By SIGNIN_BUTTON_ALT1 = By.xpath("//button[@type='submit']");
    private static final By SIGNIN_BUTTON_ALT2 = By.cssSelector("button[type='submit']");

    // Page header or title to verify page is loaded
    private static final By LOGIN_HEADER = By.xpath("//h1[contains(text(), 'Log in')]");
    private static final By LOGIN_HEADER_ALT = By.xpath("//h1[contains(text(), 'Sign')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter email address into the email input field.
     * Clears any existing content before typing.
     *
     * @param email the email address to enter
     */
    public void enterEmail(String email) {
        logger.info("Entering email: {}", email);
        try {
            sendKeys(EMAIL_INPUT, email);
        } catch (Exception e) {
            logger.debug("EMAIL_INPUT not found, trying alternative locators");
            try {
                sendKeys(EMAIL_INPUT_ALT1, email);
            } catch (Exception e2) {
                logger.debug("EMAIL_INPUT_ALT1 not found, trying alternative");
                sendKeys(EMAIL_INPUT_ALT2, email);
            }
        }
    }

    /**
     * Enter password into the password input field.
     * Clears any existing content before typing.
     *
     * @param password the password to enter
     */
    public void enterPassword(String password) {
        logger.info("Entering password (length: {})", password.length());
        try {
            sendKeys(PASSWORD_INPUT, password);
        } catch (Exception e) {
            logger.debug("PASSWORD_INPUT not found, trying alternative locators");
            try {
                sendKeys(PASSWORD_INPUT_ALT1, password);
            } catch (Exception e2) {
                logger.debug("PASSWORD_INPUT_ALT1 not found, trying alternative");
                sendKeys(PASSWORD_INPUT_ALT2, password);
            }
        }
    }

    /**
     * Click the Sign in button to submit the login form.
     */
    public void clickSignIn() {
        logger.info("Clicking Sign in button");
        try {
            click(SIGNIN_BUTTON);
        } catch (Exception e) {
            logger.debug("SIGNIN_BUTTON not found, trying alternative locators");
            try {
                click(SIGNIN_BUTTON_ALT1);
            } catch (Exception e2) {
                logger.debug("SIGNIN_BUTTON_ALT1 not found, trying alternative");
                click(SIGNIN_BUTTON_ALT2);
            }
        }
    }

    /**
     * Perform login with email and password.
     * This is a convenience method that combines entering credentials and clicking sign-in.
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
     * Checks for the presence of login header element.
     * This is the ONLY assertion allowed in page objects.
     */
    @Override
    public void assertLoaded() {
        logger.info("Verifying Login page is loaded");
        try {
            // Try primary locator
            waitForElement(LOGIN_HEADER);
        } catch (Exception e) {
            logger.debug("LOGIN_HEADER not found, trying alternative");
            // Try alternative locator
            waitForElement(LOGIN_HEADER_ALT);
        }
        logger.info("Login page has loaded successfully");
    }
}
