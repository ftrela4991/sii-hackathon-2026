# Test Plan: TC-001 — Register New Customer Successfully

**Test ID:** TC-001
**Category:** BASIC
**Type:** Positive
**Priority:** High
**Tags:** `@registration` `@account` `@smoke`
**Author:** Test Plan Architect
**Date:** 2026-03-07
**Application URL:** `http://145.239.29.235`

---

## Table of Contents

1. [Objective](#objective)
2. [Scope](#scope)
3. [Application Discovery Summary](#application-discovery-summary)
4. [Preconditions](#preconditions)
5. [API Setup](#api-setup)
6. [Test Steps](#test-steps)
7. [Expected Results](#expected-results)
8. [API Teardown](#api-teardown)
9. [Test Data Requirements](#test-data-requirements)
10. [Locator Strategy](#locator-strategy)
11. [Assertions Specification](#assertions-specification)
12. [Architecture & Implementation Guidance](#architecture--implementation-guidance)
13. [Risks and Mitigations](#risks-and-mitigations)
14. [Notes and Decisions](#notes-and-decisions)

---

## Objective

Verify that a new visitor can successfully create a customer account using valid data through the registration form at `/registration`, and that upon submission the system creates an authenticated session, redirects the user to the My Account page, and displays a personalised greeting with the registered name.

This test protects the critical business capability: **new customer acquisition via self-registration**. A failure here means no new customer can join the store through the standard flow.

---

## Scope

**In scope:**
- Navigation to the registration page
- Completion of all required form fields with valid data
- Submission of the registration form
- Verification of post-registration redirect to `/my-account`
- Verification of authenticated session state (greeting with name, "Sign out" visible)
- API-based teardown of the created customer account

**Out of scope:**
- Negative registration scenarios (invalid data, duplicate email, missing required fields)
- Email confirmation flows (if any)
- Password strength enforcement (covered by a separate negative test)
- Optional fields (birthdate, newsletter subscription, partner offers)

---

## Application Discovery Summary

The following was established by direct exploration of `http://145.239.29.235/registration`:

### Registration Form Fields

| Field | HTML Element | ID / Name | Type | Required |
|---|---|---|---|---|
| Social Title — Mr. | `input[type=radio]` | `id="field-id_gender-1"` / `name="id_gender"` | radio | No |
| Social Title — Mrs. | `input[type=radio]` | `id="field-id_gender-2"` / `name="id_gender"` | radio | No |
| First name | `input[type=text]` | `id="field-firstname"` / `name="firstname"` | text | Yes |
| Last name | `input[type=text]` | `id="field-lastname"` / `name="lastname"` | text | Yes |
| Email | `input[type=email]` | `id="field-email"` / `name="email"` | email | Yes |
| Password | `input[type=password]` | `id="field-password"` / `name="password"` | password | Yes |
| Birthdate | `input[type=text]` | `id="field-birthday"` / `name="birthday"` | text (MM/DD/YYYY) | No |
| Receive offers from partners | `input[type=checkbox]` | `name="optin"` | checkbox | No |
| I agree to terms and conditions and the privacy policy | `input[type=checkbox]` | `name="psgdpr"` | checkbox | Yes |
| Sign up for newsletter | `input[type=checkbox]` | `name="newsletter"` | checkbox | No |
| Customer data privacy | `input[type=checkbox]` | `name="customer_privacy"` | checkbox | Yes |
| Save (submit) | `button[type=submit]` | — | submit | — |

**Key observations from exploration:**
- The form POSTs to `http://145.239.29.235/registration`
- No `data-testid` or `data-test` attributes are present on any form element — stable `id` attributes are available for most inputs and are the recommended locator strategy
- Checkbox inputs for `psgdpr` and `customer_privacy` have **no `id` attribute** — they must be located via `name` attribute or CSS selector (`input[name="psgdpr"]`, `input[name="customer_privacy"]`)
- First name and last name hints read: "Only letters and the dot (.) character, followed by a space, are allowed."
- A password strength feedback element is present (`.password-strength-feedback`), providing real-time strength indication
- Unauthenticated access to `/my-account` redirects to `/login?back=my-account`, confirming the redirect assertion must check the post-registration URL, not attempt to navigate there independently

### API Verification

| Concern | Result |
|---|---|
| API accessible | Yes — `GET /api/customers` returns customer list |
| Authentication method | Query parameter: `ws_key=E5XH2FV5WYJA42I2XWBMUMBYJK1LF1KE` |
| Filter by email | Supported: `filter[email]=<value>` |
| Delete by ID | `DELETE /api/customers/{id}` — standard REST, confirmed available |
| Output format | `output_format=JSON` |

---

## Preconditions

1. The application at `http://145.239.29.235` is accessible and responsive.
2. The Prestashop Webservice API is enabled and the provided API key (`E5XH2FV5WYJA42I2XWBMUMBYJK1LF1KE`) has at minimum `GET`, `POST`, and `DELETE` permissions on the `customers` resource.
3. No existing customer account uses the test email address generated for this run. (Guaranteed by the UUID-based email generation strategy — see [Test Data Requirements](#test-data-requirements).)
4. The test is executed from a machine with network access to `http://145.239.29.235`.
5. A supported browser (Chrome or Firefox) and its corresponding WebDriver are available.

---

## API Setup

**Decision: No pre-test API setup is required for TC-001.**

Rationale:
- This test creates a new customer through the UI. There is no existing state that needs to be prepared before the test runs.
- The only risk of pre-existing conflicting data is a duplicate email. This is fully mitigated by generating a unique email per test run using a UUID (see [Test Data Requirements](#test-data-requirements)).
- Attempting to pre-delete a non-existent customer by email would add unnecessary complexity without benefit.

The API is used **only in teardown** to clean up the account created during the test.

---

## Test Steps

The steps below describe the complete test execution sequence. Each step specifies the action taken and the immediate expected behavior. Final outcome assertions are consolidated in the [Expected Results](#expected-results) section.

**Arrange (precondition verification):**

| Step | Action | Immediate Expected Behavior |
|---|---|---|
| 1 | Generate a unique test email address in the format `user_<UUID>@test.com` | A unique email string is produced and stored for use throughout the test and teardown |
| 2 | Navigate the browser to `http://145.239.29.235/registration` | The page titled "Create an account" loads; the registration form is visible with all fields in their default (empty/unchecked) state; the header shows "Sign in" |

**Act (form interaction):**

| Step | Action | Immediate Expected Behavior |
|---|---|---|
| 3 | Click the radio button for Social Title "Mr." (`id="field-id_gender-1"`) | The "Mr." radio button becomes selected |
| 4 | Enter "John" into the First name field (`id="field-firstname"`) | The field displays "John" |
| 5 | Enter "Doe" into the Last name field (`id="field-lastname"`) | The field displays "Doe" |
| 6 | Enter the generated unique email into the Email field (`id="field-email"`) | The field displays the generated email address |
| 7 | Enter "Test@1234!" into the Password field (`id="field-password"`) | The field accepts the input; the password strength indicator reflects a "Strong" rating |
| 8 | Check the "I agree to the terms and conditions and the privacy policy" checkbox (`input[name="psgdpr"]`) | The checkbox becomes checked |
| 9 | Check the "Customer data privacy" checkbox (`input[name="customer_privacy"]`) | The checkbox becomes checked |
| 10 | Click the "Save" submit button (`button[type="submit"]`) | The form is submitted; the browser begins a page transition |

**Assert (outcome verification):**

| Step | Action | Immediate Expected Behavior |
|---|---|---|
| 11 | Wait for page navigation to complete | The browser URL changes and the new page content loads fully |
| 12 | Verify URL, greeting, and header state (see [Expected Results](#expected-results)) | All three assertions pass |

---

## Expected Results

The following assertions must all pass for TC-001 to be considered a success.

| # | Assertion | Detail | Criterion |
|---|---|---|---|
| R-1 | **Redirect to My Account** | After form submission, the browser URL contains `/my-account` | Business outcome: the system recognised the registration as successful and established a session |
| R-2 | **Personalised greeting is displayed** | The page contains text that includes the string "John Doe" (full name of the registered user) | Business outcome: the account was created with the correct personal details and the session is associated with the right user |
| R-3 | **Authenticated session is active** | The page header contains "Sign out" and does NOT contain "Sign in" | Business outcome: the user is logged in — the registration simultaneously authenticated the new customer |

**Assertion failure message guidance for implementors:**

- R-1: `"Expected URL to contain '/my-account' after registration, but actual URL was: <actual_url>"`
- R-2: `"Expected page to contain greeting 'John Doe' after registration, but greeting text was: <actual_text>"`
- R-3: `"Expected header to show 'Sign out' (authenticated state) after registration, but found: <actual_header_text>"`

---

## API Teardown

**Decision: Post-test API teardown is required and must be implemented.**

Rationale:
- The test creates a real customer record in the database. Without teardown, repeated test runs accumulate orphaned accounts.
- More critically, if a UUID collision ever occurred (statistically negligible but architecturally important to handle), a leftover account would cause the next run to fail with a "duplicate email" error rather than the expected success — masking test reliability.
- Teardown maintains test independence (criterion 2.7) and a clean system state.
- Teardown must execute even when the test fails (implement in `@AfterEach`).

### Teardown Procedure

The teardown must be implemented in `@AfterEach` using the `PrestashopApiClient`. The sequence is:

**Step T-1 — Find the customer ID by email:**

```
GET /api/customers
  ?ws_key=E5XH2FV5WYJA42I2XWBMUMBYJK1LF1KE
  &output_format=JSON
  &filter[email]=<generated_test_email>
  &display=[id]
```

Expected response (if account was created):
```json
{"customers": [{"id": <customer_id>}]}
```

If the response contains an empty `customers` array, the account was not created (test may have failed before submission) — teardown is complete, no further action needed.

**Step T-2 — Delete the customer by ID:**

```
DELETE /api/customers/<customer_id>
  ?ws_key=E5XH2FV5WYJA42I2XWBMUMBYJK1LF1KE
```

Expected HTTP response status: `200 OK`

**Teardown is considered successful when:**
- The customer ID lookup returned empty (account never created), OR
- The DELETE returned `200 OK`

**Teardown must NOT throw an exception that masks the test result.** Any teardown failure should be logged as a warning but must not override a test pass/fail result.

---

## Test Data Requirements

| Data Point | Value | Generation Strategy |
|---|---|---|
| Social Title | Mr. | Static — fixed value per scenario |
| First Name | John | Static — fixed value per scenario |
| Last Name | Doe | Static — fixed value per scenario |
| Email | `user_<UUID>@test.com` | Dynamic — generated fresh per test run using `UUID.randomUUID()` |
| Password | `Test@1234!` | Static — meets the application's "Strong" password criteria |
| Terms checkbox | Checked | Static action |
| Customer privacy checkbox | Checked | Static action |

**Email uniqueness implementation note:**

```java
// In test or fixture — generate once and store for both the test and teardown
String testEmail = "user_" + UUID.randomUUID() + "@test.com";
```

The generated email must be accessible to both the test method and the `@AfterEach` teardown method. Store it as an instance field on the test class, initialised in `@BeforeEach` or at field declaration.

---

## Locator Strategy

Based on exploration of the live application, no `data-testid` or `data-test` attributes are present. The following locator strategy is prescribed for this test, in order of preference:

| Element | Recommended Locator | Rationale |
|---|---|---|
| Social Title "Mr." radio | `By.id("field-id_gender-1")` | Stable `id` attribute present |
| First name input | `By.id("field-firstname")` | Stable `id` attribute present |
| Last name input | `By.id("field-lastname")` | Stable `id` attribute present |
| Email input | `By.id("field-email")` | Stable `id` attribute present |
| Password input | `By.id("field-password")` | Stable `id` attribute present |
| Terms checkbox (psgdpr) | `By.cssSelector("input[name='psgdpr']")` | No `id` present; `name` attribute is stable |
| Customer privacy checkbox | `By.cssSelector("input[name='customer_privacy']")` | No `id` present; `name` attribute is stable |
| Submit button | `By.cssSelector("button[type='submit']")` within the registration form | No `id` present; type attribute is stable and scoped to form |
| "Sign out" header link | `By.cssSelector(".user-info a[href*='logout'], a[title='Log me out']")` or text-based fallback | Verify actual rendered HTML during implementation |

**Do not use:**
- XPaths based on DOM depth or positional indices
- Locators based on visible label text for non-verified elements (e.g., do not find the submit button by its "Save" label text — use `type=submit`)

---

## Assertions Specification

The three assertions (R-1, R-2, R-3) align with the self-evaluation guidelines as follows:

| Assertion | Guideline | Compliance Note |
|---|---|---|
| R-1: URL contains `/my-account` | 6.2 — verify business outcome, not element presence | Confirms successful registration and session creation, not just that a page loaded |
| R-2: Page contains "John Doe" | 6.2, 6.3 — resilient to minor UI changes | Checks text content, not CSS class or layout position |
| R-3: Header shows "Sign out" | 6.2 — verify business data (authenticated state) | Confirms session is active; complements R-1 and R-2 |

Total assertions: **3** — well within the 10-assertion ceiling flagged as a code smell in the guidelines.

All assertions must include descriptive failure messages (see [Expected Results](#expected-results) section).

---

## Architecture & Implementation Guidance

This section provides structural guidance for the implementation team to ensure alignment with the project's Page Object Model architecture and self-evaluation scoring criteria.

### Required Page Objects

| Page Object | Responsibility |
|---|---|
| `RegistrationPage` | Locators and UI operations for the `/registration` form: `selectMrTitle()`, `enterFirstName(String)`, `enterLastName(String)`, `enterEmail(String)`, `enterPassword(String)`, `checkTermsAndConditions()`, `checkCustomerPrivacy()`, `clickSave()`, `assertLoaded()` |
| `MyAccountPage` | Locators and UI operations for `/my-account`: `getGreetingText()`, `isSignOutVisible()`, `assertLoaded()` |
| `HeaderComponent` | Shared header operations reusable across pages: `isSignOutVisible()`, `isSignInVisible()` — may be composed into page objects rather than extending |

### Required Fixture / API Client

`PrestashopApiClient` (already scaffolded in `fixtures/`) must implement:
- `findCustomerIdByEmail(String email)` — returns `Optional<Long>`
- `deleteCustomer(long customerId)` — executes DELETE and asserts 200 OK

### Test Class Structure

The test class should follow the Arrange-Act-Assert pattern and read as a business scenario:

```
class RegistrationTest extends BaseTest {

    private String testEmail;

    @BeforeEach
    void generateTestData() {
        testEmail = "user_" + UUID.randomUUID() + "@test.com";
    }

    @Test
    void shouldRedirectToMyAccountAfterSuccessfulRegistration() {
        // Arrange
        navigateTo("/registration");
        RegistrationPage registrationPage = new RegistrationPage(getDriver());
        registrationPage.assertLoaded();

        // Act
        registrationPage.selectMrTitle();
        registrationPage.enterFirstName("John");
        registrationPage.enterLastName("Doe");
        registrationPage.enterEmail(testEmail);
        registrationPage.enterPassword("Test@1234!");
        registrationPage.checkTermsAndConditions();
        registrationPage.checkCustomerPrivacy();
        registrationPage.clickSave();

        // Assert
        MyAccountPage myAccountPage = new MyAccountPage(getDriver());
        myAccountPage.assertLoaded();
        assertThat(getDriver().getCurrentUrl())
            .as("URL should contain /my-account after successful registration")
            .contains("/my-account");
        assertThat(myAccountPage.getGreetingText())
            .as("Greeting should contain the registered user's full name")
            .contains("John Doe");
        assertThat(myAccountPage.isSignOutVisible())
            .as("Sign out link should be visible, confirming authenticated session")
            .isTrue();
    }

    @AfterEach
    void cleanUpCreatedCustomer() {
        PrestashopApiClient api = new PrestashopApiClient();
        api.findCustomerIdByEmail(testEmail)
           .ifPresent(api::deleteCustomer);
    }
}
```

### Stability Requirements

Implementors must adhere to the following to prevent flakiness (self-evaluation Section 3):

- Do NOT use `Thread.sleep()`. Any waiting must use `WebDriverWait` with `ExpectedConditions`.
- After clicking "Save", wait for the URL to change or for a unique element on `/my-account` to become visible before reading assertions.
- The password field triggers a JavaScript strength indicator — wait for the field to be interactable before sending keys.
- Form submission triggers a full page navigation — use an explicit wait for URL condition or for the greeting element's presence before asserting.

Recommended post-submit wait:
```
WebDriverWait.until(ExpectedConditions.urlContains("/my-account"))
```

---

## Risks and Mitigations

| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|
| Test email collision with existing account | Very Low | High (test fails with wrong error) | UUID-based email generation; teardown after each run |
| Teardown fails silently, leaving orphaned data | Low | Medium (accumulates data; no direct test impact) | Log teardown errors as warnings; monitor API response codes in teardown |
| Application is slow / registration takes longer than timeout | Low | Medium (false failure) | Configure `timeout.explicit.wait` to at least 15 seconds; use element-state waits rather than time-based waits |
| `psgdpr` or `customer_privacy` checkbox is not checked — form rejects submission | Low | High (test fails at submit) | Confirm both required checkboxes are checked before clicking Save; add `assertLoaded()` to verify form state |
| "Sign out" link selector is fragile if UI changes | Medium | Low (assertion fails on UI-only change) | Use `name` or `href` attribute containing `logout` rather than visible text; verify locator during implementation against live DOM |
| Parallel test execution causes interference | Very Low | Low | Each test generates a unique email and operates an isolated WebDriver via ThreadLocal; no shared state |

---

## Notes and Decisions

### Decision 1: No pre-test API setup required

The test scenario creates a brand-new customer. There is no pre-existing state dependency. UUID-based email generation eliminates any risk of email conflicts. Pre-test API calls would add complexity with zero benefit.

### Decision 2: Post-test API teardown is mandatory

A customer account is a persistent database record. Without cleanup, the test suite is not re-runnable to a clean state. Teardown is implemented in `@AfterEach` to guarantee execution even on test failure. The implementation must be non-throwing — teardown errors are logged but do not affect the test result.

### Decision 3: Both `psgdpr` and `customer_privacy` checkboxes must be checked

Application exploration revealed two required checkboxes beyond the terms agreement described in the original scenario: `psgdpr` (labelled "I agree to the terms and conditions and the privacy policy") and `customer_privacy` (labelled "Customer data privacy"). The original scenario mentioned only the terms checkbox. Both have `required` semantics and must be checked for the form to submit successfully. The `customer_privacy` checkbox is added to the test steps accordingly.

### Decision 4: Social Title (gender) is optional but included

The `id_gender` radio buttons are not `required` on the form. However, the scenario explicitly specifies selecting "Mr.", so it is included in the test steps as a scenario fidelity requirement — not a system constraint.

### Decision 5: Password value meets "Strong" strength requirement

The password `Test@1234!` was selected to satisfy the application's password strength requirement. The application renders a real-time strength indicator (`.password-strength-feedback`). The implementation should verify the field accepts the value but need not assert on the strength label text (that is a concern for a separate negative/boundary test).

### Alignment with Self-Evaluation Standards

| Standard | How TC-001 Complies |
|---|---|
| 1.1 — Tests verify business essence | The three assertions confirm account creation, correct identity, and session activation — not just element presence |
| 2.1 — Tests are independent | No manual prerequisites; UUID email ensures isolation |
| 2.3 — Setup via API where possible | No setup needed; teardown is API-based |
| 2.5 — Unique identifiers | UUID email per run |
| 2.6 — Cleanup implemented | `@AfterEach` API teardown specified and required |
| 3.2 / 3.3 — Explicit waits on element state | Required; `Thread.sleep()` prohibited |
| 4.1 / 4.2 — Stable locators | `id` attributes used where available; `name` CSS selectors where `id` absent; no structural XPaths |
| 5.1 — POM applied | `RegistrationPage` and `MyAccountPage` objects specified |
| 5.2 — No assertions in page objects | All assertions in test class; page objects expose data-retrieval methods only |
| 5.7 — API client for teardown | `PrestashopApiClient` handles all HTTP calls |
| 6.4 — Descriptive assertion messages | Failure message templates provided for all three assertions |
| 8.4 — No magic values | Email pattern, password, and URL fragments referenced via constants or config |
| 8.6 — AAA structure | Test class structure follows Arrange-Act-Assert explicitly |
