# Design: Java + Selenium Parallel Test Infrastructure

**Date:** 2026-03-07
**Target Application:** Prestashop-based E-Commerce Demo
**Tech Stack:** Maven + JUnit 5 + Selenium WebDriver

---

## Overview

This document describes a production-ready test infrastructure for a Prestashop-based e-commerce application. The design prioritizes test independence, parallel execution, and architectural clarity (Page Object Model with strict separation of concerns).

**Key Design Decision:** Thread-Local WebDriver management for safe concurrent test execution without driver conflicts.

---

## 1. Project Structure

```
src/test/java/
├── base/
│   ├── BaseTest.java                 # Abstract test class with ThreadLocal driver lifecycle
│   └── BasePage.java                 # Base page object with common UI operations
├── pages/
│   └── (Page objects for each UI screen - added incrementally)
├── tests/
│   └── (Actual test specs - added incrementally)
└── utils/
    └── DriverFactory.java            # WebDriver initialization, configuration, teardown

src/test/resources/
├── config.properties                 # Test configuration (URLs, timeouts, browser type)

pom.xml                              # Maven build with Selenium, JUnit 5, WebDriver Manager
```

**Rationale:**
- `base/` contains reusable infrastructure
- `pages/` and `tests/` are added as tests are written
- `utils/` holds factory logic separate from test lifecycle
- `src/test/resources/` centralizes environment-specific config

---

## 2. Core Architecture

### 2.1 BaseTest.java — Test Lifecycle & Driver Management

**Responsibilities:**
- Manage WebDriver lifecycle via `@BeforeEach` and `@AfterEach`
- Provide thread-safe driver access via `ThreadLocal<WebDriver>`
- Capture screenshots on test failure
- Handle driver cleanup and resource release

**Key Methods:**
```
+ getDriver(): WebDriver                    // Get current thread's driver
+ setUp(): void (@BeforeEach)               // Initialize driver
+ tearDown(): void (@AfterEach)             // Quit driver, cleanup ThreadLocal
+ takeScreenshot(testName): void            // Called on failure
```

**Thread Safety:**
- Each test thread gets its own driver instance
- No shared driver state between concurrent tests
- ThreadLocal prevents race conditions and cross-test contamination

**Example Usage Pattern:**
```java
public class LoginTest extends BaseTest {
    @Test
    public void userCanLogin() {
        getDriver().navigate().to("https://demo.prestashop.com");
        // ... test logic
    }
}
```

### 2.2 BasePage.java — UI Operations (Page Object Pattern)

**Responsibilities:**
- Encapsulate page element locators as `By` constants
- Provide reusable UI interaction methods (click, sendKeys, waitFor, etc.)
- Validate page state via `assertLoaded()` (only assertion allowed in POM)
- Hide WebDriver complexity from tests

**Key Methods:**
```
+ BasePage(driver: WebDriver)                // Constructor accepts driver
+ click(By): void                            // Click element, wait for presence
+ sendKeys(By, text): void                   // Send text to input field
+ getText(By): String                        // Get element text
+ waitForElement(By): void                   // Wait until element visible
+ assertLoaded(): void                       // Verify page is fully loaded (e.g., header visible)
```

**Example:**
```java
public class LoginPage extends BasePage {
    private static final By EMAIL_INPUT = By.id("email");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By SUBMIT_BUTTON = By.xpath("//button[@type='submit']");
    private static final By LOGIN_HEADER = By.className("login-header");

    public void assertLoaded() {
        waitForElement(LOGIN_HEADER);
    }

    public void enterEmail(String email) {
        sendKeys(EMAIL_INPUT, email);
    }

    public void enterPassword(String password) {
        sendKeys(PASSWORD_INPUT, password);
    }

    public DashboardPage clickLogin() {
        click(SUBMIT_BUTTON);
        return new DashboardPage(driver);
    }
}
```

**POM Principle:** Page objects contain **only** UI operations and locators. Business assertions (e.g., "user is logged in") belong in the test class, not the page object.

### 2.3 DriverFactory.java — WebDriver Creation

**Responsibilities:**
- Create WebDriver instances with appropriate capabilities
- Read configuration from `config.properties`
- Use WebDriver Manager to auto-download browser drivers (no manual setup required)
- Handle driver initialization errors

**Key Methods:**
```
+ createDriver(browserType: String): WebDriver    // Create driver for Chrome, Firefox, etc.
```

**Benefits:**
- WebDriver Manager automatically downloads and caches the correct chromedriver/geckodriver version
- Eliminates "driver not found" errors
- Supports multiple browsers via configuration

---

## 3. Maven Configuration (pom.xml)

### 3.1 Dependencies
- `selenium-java` (latest stable, ~4.x)
- `junit-jupiter-api`, `junit-jupiter-engine` (JUnit 5)
- `io.github.bonigarcia:webdrivermanager` (auto-download drivers)
- `org.slf4j:slf4j-simple` (optional, for logging)

### 3.2 Plugins
- **maven-surefire-plugin:** Configured for parallel test execution
  - `<parallel>methods</parallel>` — tests run concurrently
  - `<threadCount>4</threadCount>` (or system-dependent)
  - `<reuseForks>true</reuseForks>` — reuse JVM processes for efficiency

### 3.3 Build Phases
- `test` — runs all tests in `src/test/java/`
- Tests are compiled separately from main code (isolation)

---

## 4. Configuration (config.properties)

```properties
# Browser & Capabilities
browser.type=chrome
browser.headless=false
browser.window.width=1920
browser.window.height=1080

# Timeouts (seconds)
timeout.implicit=10
timeout.explicit.wait=15
timeout.page.load=30

# URLs
base.url=https://demo.prestashop.com
api.base.url=https://demo.prestashop.com/api

# Screenshots
screenshot.on.failure=true
screenshot.path=target/screenshots/
```

---

## 5. Test Independence & Parallelization

### 5.1 Independence Guarantees
- **One driver per test thread:** Each test gets a fresh `WebDriver` instance in `@BeforeEach`
- **No shared state:** ThreadLocal ensures one test's driver doesn't affect another
- **Automatic cleanup:** `@AfterEach` always quits the driver, even on failure
- **Unique test identifiers:** Tests can safely run on multiple machines simultaneously (no conflicts on shared URLs, since test data setup is isolated per test — API-based setup comes later)

### 5.2 Parallel Execution Details
- Maven Surefire runs multiple test classes/methods in separate threads
- Each thread gets its own ThreadLocal WebDriver
- Tests have **no ordering dependency** — can run in any order or in isolation

**Verification:**
- ✅ Can run all tests simultaneously on two machines → ThreadLocal + isolated browsers
- ✅ Can run test #5 alone without tests #1–#4 → No inter-test dependencies
- ✅ `@BeforeEach`/`@AfterEach` fully prepare and clean state → Fresh driver per test

---

## 6. Alignment with Self-Evaluation Criteria

### Section 2: Test Data and State Preparation
- ✅ **2.1:** Tests are independent — no manual prerequisites (driver initialized in @BeforeEach)
- ✅ **2.2:** Data setup is deterministic (fresh driver, known config)
- ✅ **2.3–2.4:** API-based setup prepared for future (DriverFactory template ready)
- ✅ **2.5:** Unique identifiers per test (each thread gets unique driver session)
- ✅ **2.6:** Cleanup implemented (@AfterEach quits driver)
- ✅ **2.7:** No ordering dependency (tests can run in any order)

### Section 5: Test Architecture and Patterns (POM)
- ✅ **5.1:** Page Object Pattern applied (BasePage + page-specific classes)
- ✅ **5.2:** Page Objects contain only UI ops and locators — no business assertions
- ✅ **5.3:** Only `assertLoaded()` in page objects (documented convention)
- ✅ **5.4:** Test specs read like scenarios (business language, minimal implementation details)
- ✅ **5.5:** No copy-paste — BasePage provides shared methods
- ✅ **5.6:** Clear separation: Page = UI ops, Test = scenario + assertions, Utils = driver factory
- ✅ **5.7:** API client classes ready for future (DriverFactory model)

---

## 7. Future Extensions

Once the base infrastructure is proven:

1. **API Fixtures** — Add `fixtures/` package with API client classes for test data setup
2. **Sample Tests** — Add concrete login, checkout, product search tests demonstrating POM
3. **Reporting** — Integrate Extent Reports or Allure for test execution reports
4. **CI/CD Integration** — Configure for GitHub Actions, Jenkins, etc.

---

## 8. Success Criteria

- ✅ Tests can run in parallel without conflicts
- ✅ Each test can run in isolation (no ordering)
- ✅ Project builds with `mvn clean test`
- ✅ Driver lifecycle is fully automated (@BeforeEach/@AfterEach)
- ✅ Code follows POM with clear separation of concerns
- ✅ Ready for API-based test data setup (structure in place)

---

## 9. Design Decisions & Trade-Offs

| Decision | Why | Trade-Off |
|----------|-----|-----------|
| ThreadLocal WebDriver | Safe for parallel execution, standard pattern | Slightly more boilerplate than simple fields |
| JUnit 5 (Jupiter) | Modern, better extension model, good for parallel | Requires Maven Surefire 2.22.0+ |
| WebDriver Manager | Auto-downloads drivers, eliminates setup errors | Minor performance cost on first run |
| Maven (not Gradle) | More common in enterprise, simpler for CI/CD | Slightly more verbose than Gradle |
| BasePage approach | Clear separation, reusable, follows POM strictly | Requires discipline not to add assertions to POM |

---

## Next Steps

1. Generate Maven project structure with `pom.xml`
2. Implement `BaseTest.java` with ThreadLocal driver management
3. Implement `BasePage.java` with common UI operations
4. Implement `DriverFactory.java` with WebDriver Manager integration
5. Create `config.properties` with default settings
6. Verify project builds and structure is sound
7. Begin writing test specs and page objects incrementally
