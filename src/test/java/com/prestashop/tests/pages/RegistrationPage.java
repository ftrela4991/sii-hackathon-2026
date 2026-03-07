package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the Prestashop Customer Registration page (/registration).
 *
 * Provides UI operations for filling out and submitting the registration form.
 * Contains locators for all form elements and methods to interact with them.
 */
public class RegistrationPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationPage.class);

    // Locators for form fields (based on exploration of http://145.239.29.235/registration)
    // Using XPath to click the visible parent span wrapper, not the hidden input (opacity: 0)
    private static final By MR_TITLE_RADIO = By.xpath("//input[@name='id_gender'][@value='1']/parent::span[@class='custom-radio']");
    private static final By FIRST_NAME_INPUT = By.id("field-firstname");
    private static final By LAST_NAME_INPUT = By.id("field-lastname");
    private static final By EMAIL_INPUT = By.id("field-email");
    private static final By PASSWORD_INPUT = By.id("field-password");
    // Click parent label (visible), not hidden checkbox input (opacity: 0)
    private static final By TERMS_CHECKBOX = By.xpath("//input[@name='psgdpr']/parent::label");
    private static final By CUSTOMER_PRIVACY_CHECKBOX = By.xpath("//input[@name='customer_privacy']/parent::label");
    private static final By SAVE_BUTTON = By.cssSelector("button[type='submit']");
    // Use the email input as the main element to verify page is loaded
    private static final By PAGE_LOADED_ELEMENT = By.id("field-email");

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Select "Mr." as the social title (gender).
     */
    public void selectMrTitle() {
        logger.info("Selecting 'Mr.' social title");
        click(MR_TITLE_RADIO);
    }

    /**
     * Enter first name in the First name field.
     *
     * @param firstName the first name to enter
     */
    public void enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        sendKeys(FIRST_NAME_INPUT, firstName);
    }

    /**
     * Enter last name in the Last name field.
     *
     * @param lastName the last name to enter
     */
    public void enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        sendKeys(LAST_NAME_INPUT, lastName);
    }

    /**
     * Enter email in the Email field.
     *
     * @param email the email address to enter
     */
    public void enterEmail(String email) {
        logger.info("Entering email: {}", email);
        sendKeys(EMAIL_INPUT, email);
    }

    /**
     * Enter password in the Password field.
     *
     * @param password the password to enter
     */
    public void enterPassword(String password) {
        logger.info("Entering password (length: {})", password.length());
        sendKeys(PASSWORD_INPUT, password);
    }

    /**
     * Check the "I agree to the terms and conditions and the privacy policy" checkbox.
     */
    public void checkTermsAndConditions() {
        logger.info("Checking terms and conditions checkbox");
        if (!isCheckboxChecked("psgdpr")) {
            click(TERMS_CHECKBOX);
        }
    }

    /**
     * Check the "Customer data privacy" checkbox.
     */
    public void checkCustomerPrivacy() {
        logger.info("Checking customer privacy checkbox");
        if (!isCheckboxChecked("customer_privacy")) {
            click(CUSTOMER_PRIVACY_CHECKBOX);
        }
    }

    /**
     * Click the "Save" (submit) button to submit the registration form.
     */
    public void clickSave() {
        logger.info("Clicking Save button to submit registration");
        click(SAVE_BUTTON);
    }

    /**
     * Verify that the registration page is loaded.
     * Checks for the presence of the email input field which is always present on the registration form.
     */
    @Override
    public void assertLoaded() {
        logger.info("Verifying registration page is loaded");
        waitForElement(PAGE_LOADED_ELEMENT);
    }

    /**
     * Helper method to check if a checkbox is already checked.
     * Uses JavaScript to check the actual checkbox state (hidden inputs not visible to Selenium waits).
     *
     * @param checkboxName the name attribute of the checkbox input
     * @return true if checkbox is checked, false otherwise
     */
    private boolean isCheckboxChecked(String checkboxName) {
        // Use JavaScript to check the checkbox state directly
        Object result = ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "return document.querySelector('input[name=\"" + checkboxName + "\"]').checked;"
        );
        return result instanceof Boolean && (Boolean) result;
    }
}
