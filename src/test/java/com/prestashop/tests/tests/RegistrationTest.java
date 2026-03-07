package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.fixtures.PrestashopApiClient;
import com.prestashop.tests.pages.RegistrationPage;
import com.prestashop.tests.utils.ConfigLoader;
import com.prestashop.tests.assertions.AuthenticationAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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

    @BeforeEach
    void generateTestData() {
        testEmail = "user_" + UUID.randomUUID() + "@test.com";
        apiClient = new PrestashopApiClient();
    }

    @Test
    @DisplayName("User can register with valid data and homepage shows authenticated state")
    void shouldRegisterNewCustomerSuccessfully() {
        String baseUrl = ConfigLoader.getProperty("base.url");
        getDriver().navigate().to(baseUrl + "/registration");

        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        registrationPage.assertLoaded();
        registrationPage.selectMrTitle();
        registrationPage.enterFirstName("John");
        registrationPage.enterLastName("Doe");
        registrationPage.enterEmail(testEmail);
        registrationPage.enterPassword("Test@1234!");
        registrationPage.checkTermsAndConditions();
        registrationPage.checkCustomerPrivacy();
        registrationPage.clickSave();
        registrationPage.waitForSignOutLink();

        AuthenticationAssertions.assertUserOnHomepage(getDriver().getCurrentUrl(), baseUrl);
        AuthenticationAssertions.assertUserNameInHeader(registrationPage.getUserNameInHeader().trim(), "John Doe");
        AuthenticationAssertions.assertUserIsAuthenticated(registrationPage.isSignOutLinkVisible());
    }

    @AfterEach
    void cleanUpCreatedCustomer() {
        try {
            var customerId = apiClient.findCustomerIdByEmail(testEmail);
            if (customerId.isPresent()) {
                if (!apiClient.deleteCustomer(customerId.get())) {
                    logger.warn("Failed to delete customer {}", customerId.get());
                }
            }
        } catch (Exception e) {
            logger.warn("Cleanup error", e);
        }
    }
}
