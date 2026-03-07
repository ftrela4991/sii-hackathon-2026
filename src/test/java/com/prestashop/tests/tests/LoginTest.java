package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.pages.LoginPage;
import com.prestashop.tests.pages.MyAccountPage;
import com.prestashop.tests.fixtures.PrestashopApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for customer login scenarios (TC-002).
 * Tests valid login flow with email and password.
 *
 * Test Data Setup:
 * - Uses Prestashop API to create test account before test execution
 * - Uses Prestashop API to delete test account after test completion
 * - Ensures test isolation and no cross-test data contamination
 */
@DisplayName("Customer Login Tests (TC-002)")
public class LoginTest extends BaseTest {

    // Test data constants
    private static final String TEST_EMAIL = "existing_user@test.com";
    private static final String TEST_PASSWORD = "Test@1234!";

    // API client for test data setup/cleanup
    private PrestashopApiClient apiClient;
    private long testCustomerId = -1;

    /**
     * Setup phase: Create test customer via API before each test.
     * This follows the test architecture requirement:
     * "API-based setup where possible (not UI)".
     *
     * The test account is created with valid credentials that will be used
     * in the actual login test.
     */
    @BeforeEach
    public void setupTestData() {
        logger.info("Setting up test data for TC-002");

        // Initialize API client
        apiClient = new PrestashopApiClient();

        // Clean up any existing test account (in case previous test failed)
        apiClient.deleteCustomerByEmail(TEST_EMAIL);

        // Create fresh test account
        testCustomerId = apiClient.createCustomer(TEST_EMAIL, TEST_PASSWORD);

        if (testCustomerId > 0) {
            logger.info("Test customer created successfully with ID: {}", testCustomerId);
        } else {
            logger.error("Failed to create test customer. Test may fail.");
        }
    }

    /**
     * Cleanup phase: Delete test customer via API after each test.
     * This ensures test isolation and cleans up test data from the system.
     *
     * Cleanup happens even if the test fails (guaranteed by @AfterEach).
     */
    @AfterEach
    public void cleanupTestData() {
        logger.info("Cleaning up test data for TC-002");

        if (testCustomerId > 0) {
            boolean deleted = apiClient.deleteCustomer(testCustomerId);
            if (deleted) {
                logger.info("Test customer cleaned up successfully");
            } else {
                logger.error("Failed to clean up test customer {}", testCustomerId);
            }
        } else {
            // Try to delete by email as fallback
            apiClient.deleteCustomerByEmail(TEST_EMAIL);
        }
    }

    /**
     * TC-002: Login with Valid Credentials
     *
     * Scenario: User logs in with valid email and password
     * Expected Result: User is redirected to /my-account page with name visible in header
     *
     * Test Steps:
     * 1. Navigate to login page (/login)
     * 2. Verify login page is loaded
     * 3. Enter valid email address
     * 4. Enter valid password
     * 5. Click Sign in button
     * 6. Verify user is redirected to /my-account
     * 7. Verify customer name and sign-out link are visible
     */
    @Test
    @DisplayName("TC-002: Login with valid credentials (existing_user@test.com)")
    public void testLoginWithValidCredentials() {
        logger.info("Starting TC-002: Login with valid credentials");

        // Step 1: Navigate to login page
        navigateTo("/login");

        // Step 2: Verify login page is loaded
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.assertLoaded();

        // Step 3-5: Perform login
        logger.info("Performing login with credentials");
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);

        // Step 6: Verify redirection to /my-account
        MyAccountPage myAccountPage = new MyAccountPage(getDriver());
        myAccountPage.assertLoaded();

        String currentUrl = getDriver().getCurrentUrl();
        logger.info("Current URL after login: {}", currentUrl);
        assertTrue(
            currentUrl.contains("/my-account"),
            "User should be redirected to /my-account page after successful login"
        );

        // Step 7: Verify customer name and sign-out link are visible
        logger.info("Verifying account information is displayed");

        // Verify sign-out link is visible (confirms user is logged in)
        boolean signOutVisible = myAccountPage.isSignOutVisible();
        assertTrue(
            signOutVisible,
            "Sign out link should be visible in header after successful login"
        );

        // Verify customer name is visible
        String greetingText = myAccountPage.getGreetingText();
        assertNotNull(greetingText, "Greeting text should not be null");
        assertTrue(
            greetingText.length() > 0,
            "Greeting text should contain customer name after successful login"
        );

        logger.info("TC-002: Login test completed successfully");
        logger.info("Greeting text: {}", greetingText);
    }
}
