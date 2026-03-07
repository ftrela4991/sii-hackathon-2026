package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.pages.LoginPage;
import com.prestashop.tests.fixtures.PrestashopApiClient;
import com.prestashop.tests.utils.ConfigLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private static final int WAIT_TIMEOUT = 15;

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
     * Expected Result: User remains on homepage with "Sign out" and full name visible in header
     *
     * Test Steps:
     * 1. Navigate to login page (/login)
     * 2. Verify login page is loaded
     * 3. Enter valid email address
     * 4. Enter valid password
     * 5. Click Sign in button
     * 6. Verify user remains on homepage
     * 7. Verify Sign out link and customer name are visible in header
     */
    @Test
    @DisplayName("TC-002: Should login successfully with valid credentials")
    public void shouldLoginWithValidCredentials() {
        logger.info("Starting TC-002: Login with valid credentials");

        // Step 1: Navigate to login page
        navigateTo("/login");

        // Step 2: Verify login page is loaded
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.assertLoaded();

        // Step 3-5: Perform login
        logger.info("Performing login with credentials");
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);

        // Wait for Sign out link to appear in header (confirms session + page load)
        // Actual selector: a.logout (class="logout hidden-sm-down", href="?mylogout=")
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(WAIT_TIMEOUT));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.logout")));
        logger.info("Sign out link appeared - login complete");

        // Step 6: Verify user remains on homepage (not redirected to /my-account)
        String baseUrl = ConfigLoader.getProperty("base.url");
        String currentUrl = getDriver().getCurrentUrl();
        logger.info("Current URL after login: {}", currentUrl);
        assertTrue(
            currentUrl.equals(baseUrl + "/") || currentUrl.equals(baseUrl.replaceAll("/$", "")),
            "User should remain on homepage after login, but was: " + currentUrl
        );
        logger.info("✓ R-1 Assertion passed: URL is homepage");

        // Step 7a: Verify Sign out link is visible (authenticated state)
        // Actual selector: a.logout (class="logout hidden-sm-down")
        WebElement signOutLink = getDriver().findElement(By.cssSelector("a.logout"));
        assertTrue(
            signOutLink.isDisplayed(),
            "Sign out link should be visible in header after successful login"
        );
        logger.info("✓ R-2 Assertion passed: Sign out link is visible");

        // Step 7b: Verify customer full name is visible in header next to Sign out
        // Actual selector: .user-info a.account span.hidden-sm-down
        // createCustomer(TEST_EMAIL, TEST_PASSWORD) defaults to firstName="Test", lastName="Customer"
        WebElement userNameEl = getDriver().findElement(
            By.cssSelector(".user-info a.account span.hidden-sm-down"));
        String userName = userNameEl.getText().trim();
        logger.info("User name in header: {}", userName);
        assertTrue(
            userName.equalsIgnoreCase("Test Customer"),
            "Header should display 'Test Customer' after login, but found: '" + userName + "'"
        );
        logger.info("✓ R-3 Assertion passed: Header displays 'Test Customer'");

        logger.info("TC-002: Login test completed successfully");
    }
}
