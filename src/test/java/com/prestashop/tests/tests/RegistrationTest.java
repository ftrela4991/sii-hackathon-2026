package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.fixtures.PrestashopApiClient;
import com.prestashop.tests.pages.MyAccountPage;
import com.prestashop.tests.pages.RegistrationPage;
import com.prestashop.tests.utils.ConfigLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for TC-001: Register New Customer Successfully
 *
 * This test verifies that a new visitor can successfully create a customer account
 * using the registration form and that upon submission the system creates an
 * authenticated session, redirects to /my-account, and displays the user's name.
 *
 * Follows the Arrange-Act-Assert pattern:
 * - Arrange: Generate test data and navigate to registration page
 * - Act: Fill in and submit the registration form
 * - Assert: Verify redirect, greeting, and authenticated session
 */
@DisplayName("TC-001: Register New Customer Successfully")
public class RegistrationTest extends BaseTest {

    private String testEmail;
    private PrestashopApiClient apiClient;

    /**
     * Generate unique test data before each test.
     * Uses UUID-based email to ensure uniqueness and prevent conflicts.
     */
    @BeforeEach
    void generateTestData() {
        logger.info("Generating test data for registration test");
        testEmail = "user_" + UUID.randomUUID() + "@test.com";
        apiClient = new PrestashopApiClient();
        logger.info("Test email generated: {}", testEmail);
    }

    /**
     * Main test: Verify that a new customer can successfully register.
     *
     * Scenario:
     * 1. Navigate to /registration
     * 2. Fill in registration form with valid data
     * 3. Submit the form
     * 4. Verify redirect to /my-account
     * 5. Verify greeting displays the registered name
     * 6. Verify authenticated session (Sign out visible)
     */
    @Test
    @DisplayName("User can register with valid data and is redirected to My Account")
    void shouldRegisterNewCustomerSuccessfully() {
        logger.info("Starting registration test with email: {}", testEmail);

        // ============================================================
        // ARRANGE: Navigate to registration page
        // ============================================================
        String baseUrl = ConfigLoader.getProperty("base.url", "http://145.239.29.235/");
        getDriver().navigate().to(baseUrl + "registration");
        logger.info("Navigated to registration page");

        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        registrationPage.assertLoaded();
        logger.info("Registration page loaded and verified");

        // ============================================================
        // ACT: Fill in and submit the registration form
        // ============================================================
        logger.info("Filling in registration form with email: {}", testEmail);
        registrationPage.selectMrTitle();
        registrationPage.enterFirstName("John");
        logger.info("✓ Entered first name");
        registrationPage.enterLastName("Doe");
        logger.info("✓ Entered last name");
        registrationPage.enterEmail(testEmail);
        logger.info("✓ Entered email");
        registrationPage.enterPassword("Test@1234!");
        logger.info("✓ Entered password");
        registrationPage.checkTermsAndConditions();
        logger.info("✓ Checked terms and conditions");
        registrationPage.checkCustomerPrivacy();
        logger.info("✓ Checked customer privacy");
        registrationPage.clickSave();
        logger.info("Registration form submitted - waiting for redirect");

        // Wait for page navigation to complete
        logger.info("Waiting for redirect to My Account page");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/my-account"));

        // ============================================================
        // ASSERT: Verify successful registration and session
        // ============================================================
        MyAccountPage myAccountPage = new MyAccountPage(getDriver());
        myAccountPage.assertLoaded();
        logger.info("My Account page loaded and verified");

        // R-1: Verify redirect to My Account page
        String currentUrl = getDriver().getCurrentUrl();
        logger.info("Current URL after registration: {}", currentUrl);
        assertTrue(currentUrl.contains("/my-account"),
                "Expected URL to contain '/my-account' after registration, but actual URL was: " + currentUrl);
        logger.info("✓ R-1 Assertion passed: URL contains /my-account");

        // R-2: Verify personalised greeting is displayed
        String greetingText = myAccountPage.getGreetingText();
        logger.info("Greeting text from page: {}", greetingText);
        assertNotNull(greetingText, "Greeting text should not be null");
        assertTrue(greetingText.toLowerCase().contains("john doe"),
                "Expected page to contain greeting 'John Doe' after registration, but greeting text was: " + greetingText);
        logger.info("✓ R-2 Assertion passed: Greeting contains 'John Doe'");

        // R-3: Verify authenticated session is active
        boolean isSignOutVisible = myAccountPage.isSignOutVisible();
        logger.info("Sign out link visible: {}", isSignOutVisible);
        assertTrue(isSignOutVisible,
                "Expected header to show 'Sign out' (authenticated state) after registration, but Sign out was not visible");
        logger.info("✓ R-3 Assertion passed: Sign out link is visible");

        logger.info("Registration test completed successfully");
    }

    /**
     * Clean up: Delete the created customer via API.
     * Runs after each test to ensure test isolation and clean system state.
     * This is critical for test re-runability.
     *
     * Teardown sequence:
     * 1. Find customer ID by email via GET /api/customers?filter[email]=...
     * 2. Delete customer by ID via DELETE /api/customers/{id}
     *
     * Must not throw exceptions that would mask the test result.
     * Errors are logged as warnings only.
     */
    @AfterEach
    void cleanUpCreatedCustomer() {
        logger.info("Starting cleanup for test email: {}", testEmail);
        try {
            // Find customer ID by email
            var customerId = apiClient.findCustomerIdByEmail(testEmail);

            if (customerId.isPresent()) {
                logger.info("Found customer ID: {} for email: {}", customerId.get(), testEmail);

                // Delete the customer
                boolean deleteSuccess = apiClient.deleteCustomer(customerId.get());
                if (deleteSuccess) {
                    logger.info("✓ Successfully deleted customer {} (email: {})", customerId.get(), testEmail);
                } else {
                    logger.warn("Failed to delete customer {} via API, but continuing cleanup", customerId.get());
                }
            } else {
                logger.info("No customer found with email: {} (may have failed before creation)", testEmail);
            }
        } catch (Exception e) {
            // Log but don't throw - cleanup errors should not mask test results
            logger.warn("Cleanup encountered an error, but continuing", e);
        }
    }
}
