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

    // Locators for login form elements
    private static final By EMAIL_INPUT = By.id("field-email");
    private static final By PASSWORD_INPUT = By.id("field-password");
    private static final By SIGNIN_BUTTON = By.id("submit-login");
    // Locator for page verification (login page header)
    private static final By LOGIN_HEADER = By.cssSelector("header-top");

    // Locators for header elements (post-login verification)
    private static final By SIGN_OUT_LINK = By.cssSelector("a.logout");
    private static final By USER_NAME_DISPLAY = By.cssSelector(".user-info a.account span.hidden-sm-down");
    // Alert error locator (shown on authentication failure)
    private static final By ALERT_ERROR = By.cssSelector(".alert.alert-danger");

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
     * Check if the authentication error alert is visible on the page.
     * Appears when Prestashop rejects the login (e.g., wrong password or non-existent email).
     *
     * @return true if authentication error is visible, false otherwise
     */
    public boolean isAuthenticationErrorVisible() {
        logger.info("Checking if authentication error alert is visible");
        return isElementVisible(ALERT_ERROR);
    }

    /**
     * Get the text of the authentication error alert.
     *
     * @return authentication error alert text
     */
    public String getAuthenticationErrorText() {
        logger.info("Getting authentication error alert text");
        return getText(ALERT_ERROR);
    }

    /**
     * Wait for Sign out link to appear in header (confirms authentication).
     * Useful for waiting after login form submission before verifying authenticated state.
     */
    public void waitForSignOutLink() {
        logger.info("Waiting for Sign out link to appear in header");
        waitForElement(SIGN_OUT_LINK);
    }

    /**
     * Check if the Sign out link is visible in the header.
     *
     * @return true if Sign out link is displayed, false otherwise
     */
    public boolean isSignOutLinkVisible() {
        logger.info("Checking if Sign out link is visible");
        return isElementVisible(SIGN_OUT_LINK);
    }

    /**
     * Get the user name displayed in the header (after successful login).
     *
     * @return the user name text from the header
     */
    public String getUserNameInHeader() {
        logger.info("Getting user name from header");
        return getText(USER_NAME_DISPLAY);
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
