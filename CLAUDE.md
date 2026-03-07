# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Java + Selenium test automation framework for Prestashop e-commerce demo application. Built with Maven, JUnit 5, and Selenium WebDriver. Designed for parallel test execution with thread-safe WebDriver management and Page Object Model architecture.

## Quick Start Commands

### Build & Test
```bash
# Run all tests with parallel execution (4 threads)
mvn clean test

# Run a single test class
mvn test -Dtest=SampleTests

# Run a specific test method
mvn test -Dtest=SampleTests#testWebDriverInitialized

# Compile only (skip tests)
mvn clean compile -DskipTests

# Run tests sequentially (override parallel config)
mvn clean test -DthreadCount=1
```

### Configuration
- Browser type, headless mode, window size, timeouts configured in `src/test/resources/config.properties`
- Change browser: edit `browser.type=chrome` (or `firefox`)
- Adjust timeouts: edit `timeout.implicit`, `timeout.explicit.wait`, `timeout.page.load`

## Architecture

### Core Design Principle
**Thread-Local WebDriver Management**: Each test thread gets its own isolated WebDriver instance via `ThreadLocal<WebDriver>`. This enables safe parallel execution without cross-test contamination or race conditions.

### Project Structure
```
src/test/java/com/prestashop/tests/
├── base/                          # Test infrastructure (do not modify casually)
│   ├── BaseTest.java              # Abstract test class with @BeforeEach/@AfterEach
│   └── BasePage.java              # POM base class with common UI operations
├── pages/                         # Page objects (add one per page/feature)
│   └── (LoginPage.java, CheckoutPage.java, etc.)
├── tests/                         # Test specifications
│   └── SampleTests.java           # Reference implementation
├── fixtures/                      # API clients and test data builders
│   └── PrestashopApiClient.java   # Template for Prestashop REST API calls
└── utils/                         # Utilities
    ├── ConfigLoader.java          # Load config.properties with defaults
    └── DriverFactory.java         # Create and configure WebDriver instances

src/test/resources/
└── config.properties              # Test configuration (browser, timeouts, URLs)

pom.xml                            # Maven: Selenium 4.15, JUnit 5.9.3, WebDriver Manager
```

### Test Execution Flow

1. **@BeforeEach (setUp)**
   - ConfigLoader reads `config.properties`
   - DriverFactory creates WebDriver (Chrome or Firefox)
   - Driver stored in ThreadLocal
   - Implicit & page load timeouts configured

2. **Test Method**
   - Call `getDriver()` to access thread's WebDriver
   - Use page objects (extend BasePage) for UI interaction
   - Write assertions in test, NOT in page objects

3. **@AfterEach (tearDown)**
   - Screenshot captured (configurable)
   - DriverFactory.closeDriver() quits WebDriver
   - ThreadLocal.remove() cleans up

### Strict Separation of Concerns

- **BaseTest**: Manages driver lifecycle only. No test logic.
- **BasePage**: Contains locators and UI operations only. NO business assertions (except `assertLoaded()`).
- **Test Class**: Contains test scenarios and assertions. Calls page objects and assertions.
- **Fixtures**: API clients for test data setup (not yet implemented).

**Bad:**
```java
// ❌ Don't put assertions in page objects
public class LoginPage extends BasePage {
    public void login(String email, String password) {
        sendKeys(emailInput, email);
        sendKeys(passwordInput, password);
        click(submitButton);
        assertTrue(getCurrentUrl().contains("/dashboard")); // ❌ WRONG
    }
}
```

**Good:**
```java
// ✅ Assertions in tests, operations in page objects
public class LoginTest extends BaseTest {
    @Test
    public void userCanLogin() {
        LoginPage login = new LoginPage(getDriver());
        login.login("test@example.com", "password123");

        // Assertion in test, not page object
        assertTrue(getDriver().getCurrentUrl().contains("/dashboard"));
    }
}
```

## Parallel Execution

**Configuration**: `pom.xml` Maven Surefire plugin
- `<parallel>methods</parallel>` - test methods run in parallel
- `<threadCount>4</threadCount>` - 4 concurrent browser instances
- `<reuseForks>true</reuseForks>` - reuse JVM processes

**Thread Safety Guaranteed By:**
- ThreadLocal WebDriver ensures each thread has isolated driver
- Each test gets fresh driver in @BeforeEach
- Full cleanup in @AfterEach (even on failure)
- ConfigLoader and DriverFactory are stateless

**Verification:**
- Run `mvn test` and check logs for multiple thread IDs (e.g., "thread: 21, thread: 22, thread: 23")
- Run single test with `-Dtest=ClassName#methodName` to verify isolation

## Adding New Tests

### 1. Create Page Object (if needed)
```java
package com.prestashop.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.prestashop.tests.base.BasePage;

public class CheckoutPage extends BasePage {
    // Locators as constants
    private static final By PROCEED_BUTTON = By.id("proceed");
    private static final By CART_ITEMS = By.className("cart-item");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // UI operations only (no assertions)
    public void clickProceed() {
        click(PROCEED_BUTTON);
    }

    public int getCartItemCount() {
        return waitForElement(CART_ITEMS).getSize();
    }

    // Only assertion allowed in POM
    @Override
    public void assertLoaded() {
        waitForElement(PROCEED_BUTTON); // Verify page is ready
    }
}
```

### 2. Create Test Class
```java
package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.pages.CheckoutPage;
import org.junit.jupiter.api.Test;

public class CheckoutTest extends BaseTest {
    @Test
    public void userCanCheckout() {
        navigateToHome();
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.assertLoaded();
        checkout.clickProceed();

        // Assertions in test class
        assertTrue(getDriver().getCurrentUrl().contains("checkout"));
    }
}
```

## Configuration Management

### config.properties
Located: `src/test/resources/config.properties`

**Key Settings:**
- `browser.type` - chrome or firefox
- `browser.headless` - true or false
- `timeout.implicit` - implicit wait (seconds)
- `timeout.explicit.wait` - explicit wait (seconds)
- `base.url` - application under test
- `screenshot.on.failure` - true to capture screenshots

### Accessing Configuration
```java
// In BaseTest or page objects
String browserType = ConfigLoader.getProperty("browser.type", "chrome");
int timeout = ConfigLoader.getIntProperty("timeout.explicit.wait", 15);
```

## Test Data Setup (Future)

**Design Intent**: Use API-based setup instead of UI interactions

```java
// Template in PrestashopApiClient.java
@BeforeEach
void setupTestData() {
    PrestashopApiClient api = new PrestashopApiClient();
    long customerId = api.createCustomer("test@example.com", "password");
    long productId = api.createProduct("Test Product", 99.99);
    // Use in test...
}
```

**Status**: Template provided, methods not yet implemented. Implement as needed.

## Common Issues & Troubleshooting

### Test Hangs or Times Out
- Check `timeout.explicit.wait` in config.properties (default 15 seconds)
- Verify element locators are correct (use browser dev tools)
- Check if page requires JavaScript rendering (may need waits for visibility)

### WebDriver Not Found Error
- WebDriver Manager auto-downloads drivers on first run (can be large download)
- Ensure internet connection available
- Drivers cached in ~/.wdm/ directory

### Tests Pass Locally but Fail in CI/CD
- Verify `browser.headless=true` in CI environment
- Check screenshot directory is writable (`target/screenshots/`)
- Ensure timeouts are sufficient for CI infrastructure

### Cross-Test Data Contamination
- Verify tests cleanup data in @AfterEach or use unique test identifiers
- ThreadLocal WebDriver prevents driver sharing, but API data persists
- Use unique emails/IDs with timestamp: `"user_" + System.currentTimeMillis()`

## Key Files to Know

| File | Purpose |
|------|---------|
| `pom.xml` | Maven: Selenium 4.15, JUnit 5.9.3, Surefire plugin (parallel config) |
| `BaseTest.java` | Test lifecycle, ThreadLocal driver, screenshot capture |
| `BasePage.java` | POM base class, waits, UI operations |
| `DriverFactory.java` | WebDriver creation with browser options |
| `ConfigLoader.java` | Load and access config.properties |
| `config.properties` | Browser settings, timeouts, URLs |

## Design Documents

- `docs/plans/2026-03-07-selenium-java-setup-design.md` - Full architecture design
- `docs/plans/2026-03-07-selenium-java-implementation.md` - Implementation plan with all task details

## Next Steps for Development

1. **Implement Prestashop API Client** - Add actual REST API calls in `fixtures/PrestashopApiClient.java`
2. **Add Page Objects** - Create one per major page (login, checkout, product details, etc.)
3. **Write Functional Tests** - Add test specs in `tests/` folder following SampleTests pattern
4. **Add Reporting** - Integrate Extent Reports or Allure for test reports
5. **Setup CI/CD** - Configure GitHub Actions or Jenkins to run tests on commits
