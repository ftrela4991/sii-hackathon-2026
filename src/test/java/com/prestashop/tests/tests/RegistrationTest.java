package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.fixtures.PrestashopApiClient;
import com.prestashop.tests.pages.RegistrationPage;
import com.prestashop.tests.utils.ConfigLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for TC-001: Register New Customer Successfully
 *
 * This test verifies that a new visitor can successfully create a customer account
 * using the registration form and that upon submission the system keeps the user
 * on the homepage with an active authenticated session (Sign out + full name visible).
 *
 * Follows the Arrange-Act-Assert pattern:
 * - Arrange: Generate test data and navigate to registration page
 * - Act: Fill in and submit the registration form
 * - Assert: Verify homepage URL, Sign out visible, full name in header
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
     * 4. Verify user stays on homepage
     * 5. Verify header shows "Sign out" and full name "John Doe"
     */
    @Test
    @DisplayName("User can register with valid data and homepage shows authenticated state")
    void shouldRegisterNewCustomerSuccessfully() {
        logger.info("Starting registration test with email: {}", testEmail);

        // ============================================================
        // ARRANGE: Navigate to registration page
        // ============================================================
        String baseUrl = ConfigLoader.getProperty("base.url");
        getDriver().navigate().to(baseUrl + "/registration");
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
        logger.info("Registration form submitted - waiting for homepage");

        // Wait for Sign out link to appear in header (confirms successful authentication and page transition)
        registrationPage.waitForSignOutLink();
        logger.info("Sign out link appeared - page transition complete");

        // ============================================================
        // ASSERT: Verify successful registration and session
        // ============================================================

        // R-1: Verify user stays on homepage (not redirected to /my-account)
        String currentUrl = getDriver().getCurrentUrl();
        logger.info("Current URL after registration: {}", currentUrl);
        assertTrue(currentUrl.equals(baseUrl + "/"),
                "Expected URL to be homepage '" + baseUrl + "' after registration, but actual URL was: " + currentUrl);
        logger.info("✓ R-1 Assertion passed: URL is homepage");

        // R-2: Verify full name is displayed in header next to Sign out
        String userName = registrationPage.getUserNameInHeader().trim();
        logger.info("User name in header: {}", userName);
        assertTrue(userName.equalsIgnoreCase("John Doe"),
                "Expected header to display 'John Doe' after registration, but found: '" + userName + "'");
        logger.info("✓ R-2 Assertion passed: Header displays 'John Doe'");

        // R-3: Verify Sign out link is visible (authenticated state)
        assertTrue(registrationPage.isSignOutLinkVisible(),
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
