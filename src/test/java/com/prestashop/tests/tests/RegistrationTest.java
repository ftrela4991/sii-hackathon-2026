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

    @Test
    @DisplayName("TC-036: Should show error when registering with an already registered email")
    void shouldShowErrorWhenEmailAlreadyRegistered() {
        // Arrange: pre-create a customer via API so the email already exists in the database
        String existingEmail = "existing_" + UUID.randomUUID() + "@test.com";
        long existingCustomerId = apiClient.createCustomer(existingEmail, "Test@1234!");

        try {
            String baseUrl = ConfigLoader.getProperty("base.url");
            getDriver().navigate().to(baseUrl + "/registration");

            RegistrationPage registrationPage = new RegistrationPage(getDriver());
            registrationPage.assertLoaded();
            registrationPage.selectMrTitle();
            registrationPage.enterFirstName("John");
            registrationPage.enterLastName("Doe");
            registrationPage.enterEmail(existingEmail);
            registrationPage.enterPassword("Test@1234!");
            registrationPage.checkTermsAndConditions();
            registrationPage.checkCustomerPrivacy();
            registrationPage.clickSave();

            // Assert: registration fails with an error about the email already being in use
            assertTrue(registrationPage.isErrorAlertVisible(),
                    "Expected an error alert when registering with an already-registered email address");
            assertTrue(registrationPage.getAlertErrorText().toLowerCase().contains("already"),
                    "Expected error message to mention the email is already registered, but got: "
                            + registrationPage.getAlertErrorText());
        } finally {
            if (existingCustomerId > 0) {
                apiClient.deleteCustomer(existingCustomerId);
            } else {
                apiClient.deleteCustomerByEmail(existingEmail);
            }
        }
    }

    @Test
    @DisplayName("TC-037: Should not submit registration when email format is invalid")
    void shouldNotSubmitRegistrationWhenEmailIsInvalid() {
        String baseUrl = ConfigLoader.getProperty("base.url");
        getDriver().navigate().to(baseUrl + "/registration");

        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        registrationPage.assertLoaded();
        registrationPage.selectMrTitle();
        registrationPage.enterFirstName("John");
        registrationPage.enterLastName("Doe");
        registrationPage.enterEmail("not-a-valid-email");
        registrationPage.enterPassword("Test@1234!");
        registrationPage.checkTermsAndConditions();
        registrationPage.checkCustomerPrivacy();
        registrationPage.clickSave();

        // Assert: browser HTML5 email validation prevents form submission
        assertTrue(getDriver().getCurrentUrl().contains("/registration"),
                "Expected to remain on /registration when email format is invalid, but navigated to: "
                        + getDriver().getCurrentUrl());
    }

    @Test
    @DisplayName("TC-038: Should not submit registration when required fields are empty")
    void shouldNotSubmitRegistrationWhenRequiredFieldsAreEmpty() {
        String baseUrl = ConfigLoader.getProperty("base.url");
        getDriver().navigate().to(baseUrl + "/registration");

        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        registrationPage.assertLoaded();
        // Do not fill any fields — click Save immediately
        registrationPage.clickSave();

        // Assert: browser HTML5 required-field validation prevents form submission
        assertTrue(getDriver().getCurrentUrl().contains("/registration"),
                "Expected to remain on /registration when required fields are empty, but navigated to: "
                        + getDriver().getCurrentUrl());
    }
}
