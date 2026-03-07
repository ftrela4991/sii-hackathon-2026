package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.pages.LoginPage;
import com.prestashop.tests.fixtures.PrestashopApiClient;
import com.prestashop.tests.utils.ConfigLoader;
import com.prestashop.tests.assertions.AuthenticationAssertions;
import com.prestashop.tests.assertions.NavigationAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    public void setupTestData() {
        apiClient = new PrestashopApiClient();
        apiClient.deleteCustomerByEmail(TEST_EMAIL);
        testCustomerId = apiClient.createCustomer(TEST_EMAIL, TEST_PASSWORD);
        if (testCustomerId <= 0) {
            logger.error("Failed to create test customer");
        }
    }

    @AfterEach
    public void cleanupTestData() {
        if (testCustomerId > 0) {
            if (!apiClient.deleteCustomer(testCustomerId)) {
                logger.error("Failed to delete customer {}", testCustomerId);
            }
        } else {
            apiClient.deleteCustomerByEmail(TEST_EMAIL);
        }
    }

    @Test
    @DisplayName("TC-002: Should login successfully with valid credentials")
    public void shouldLoginWithValidCredentials() {
        navigateTo("/login");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);
        loginPage.waitForSignOutLink();

        String baseUrl = ConfigLoader.getProperty("base.url");
        NavigationAssertions.assertCurrentUrlMatches(getDriver().getCurrentUrl(), baseUrl);
        AuthenticationAssertions.assertUserIsAuthenticated(loginPage.isSignOutLinkVisible());
        AuthenticationAssertions.assertUserNameInHeader(loginPage.getUserNameInHeader().trim(), "Test Customer");
    }
}
