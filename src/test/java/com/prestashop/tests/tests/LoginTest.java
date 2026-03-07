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

import java.util.UUID;

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

    @Test
    @DisplayName("TC-039: Should show authentication error when logging in with wrong password")
    public void shouldShowErrorWhenLoginWithWrongPassword() {
        navigateTo("/login");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(TEST_EMAIL, "WrongPassword999!");

        assertTrue(loginPage.isAuthenticationErrorVisible(),
                "Expected authentication error message when logging in with wrong password");
        assertTrue(loginPage.getAuthenticationErrorText().toLowerCase().contains("authentication failed"),
                "Expected 'Authentication failed' error message, but got: "
                        + loginPage.getAuthenticationErrorText());
    }

    @Test
    @DisplayName("TC-040: Should show authentication error when logging in with non-existent email")
    public void shouldShowErrorWhenLoginWithNonExistentEmail() {
        navigateTo("/login");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("nonexistent_" + UUID.randomUUID() + "@test.com", "Test@1234!");

        assertTrue(loginPage.isAuthenticationErrorVisible(),
                "Expected authentication error message when logging in with a non-existent email");
        assertTrue(loginPage.getAuthenticationErrorText().toLowerCase().contains("authentication failed"),
                "Expected 'Authentication failed' error message, but got: "
                        + loginPage.getAuthenticationErrorText());
    }

    @Test
    @DisplayName("TC-041: Should not submit login form when email field is empty")
    public void shouldNotSubmitLoginWhenEmailIsEmpty() {
        navigateTo("/login");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterPassword(TEST_PASSWORD);
        loginPage.clickSignIn();

        // Browser HTML5 required-field validation prevents form submission
        assertTrue(getDriver().getCurrentUrl().contains("/login"),
                "Expected to remain on /login when email is empty, but navigated to: "
                        + getDriver().getCurrentUrl());
    }

    @Test
    @DisplayName("TC-042: Should not submit login form when password field is empty")
    public void shouldNotSubmitLoginWhenPasswordIsEmpty() {
        navigateTo("/login");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterEmail(TEST_EMAIL);
        loginPage.clickSignIn();

        // Browser HTML5 required-field validation prevents form submission
        assertTrue(getDriver().getCurrentUrl().contains("/login"),
                "Expected to remain on /login when password is empty, but navigated to: "
                        + getDriver().getCurrentUrl());
    }
}
