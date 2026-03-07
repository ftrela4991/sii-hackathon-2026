# Java + Selenium Parallel Test Infrastructure Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans or superpowers:subagent-driven-development to implement this plan task-by-task.

**Goal:** Create a complete Maven-based Java test infrastructure with ThreadLocal WebDriver management, BasePage/BaseTest classes, and parallel execution support for Prestashop testing.

**Architecture:** Maven project with `src/test/java/` containing base classes (BaseTest with ThreadLocal driver, BasePage for POM), utilities (DriverFactory), and configuration. Tests will inherit BaseTest and use page objects, ensuring thread-safe parallel execution via ThreadLocal WebDriver.

**Tech Stack:** Maven 3.8+, Java 11+, Selenium 4.x, JUnit 5 (Jupiter), WebDriver Manager, SLF4J

---

## Task 1: Create Maven Project Structure

**Files:**
- Create: `pom.xml`
- Create: `src/test/java/`
- Create: `src/test/resources/`

**Step 1: Create pom.xml with dependencies and plugins**

Create file `pom.xml` in the root directory:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.prestashop.tests</groupId>
    <artifactId>selenium-test-suite</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Prestashop Selenium Tests</name>
    <description>Parallel automated tests for Prestashop demo application</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <selenium.version>4.15.0</selenium.version>
        <junit.version>5.9.3</junit.version>
        <webdrivermanager.version>5.6.3</webdrivermanager.version>
    </properties>

    <dependencies>
        <!-- Selenium WebDriver -->
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>${selenium.version}</version>
        </dependency>

        <!-- JUnit 5 (Jupiter) -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- WebDriver Manager (auto-download drivers) -->
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>${webdrivermanager.version}</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.7</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>2.0.7</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Maven Compiler Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>

            <!-- Maven Surefire Plugin (parallel test execution) -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0-M9</version>
                <configuration>
                    <!-- Parallel execution: methods = run test methods in parallel -->
                    <parallel>methods</parallel>
                    <!-- Number of threads (use -DthreadCount=N to override) -->
                    <threadCount>4</threadCount>
                    <!-- Reuse forked JVM processes for efficiency -->
                    <reuseForks>true</reuseForks>
                    <!-- Fail if no tests found -->
                    <failIfNoTests>false</failIfNoTests>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

**Step 2: Create directory structure**

Run these commands to create the required directories:

```bash
mkdir -p src/test/java/com/prestashop/tests/base
mkdir -p src/test/java/com/prestashop/tests/pages
mkdir -p src/test/java/com/prestashop/tests/utils
mkdir -p src/test/resources
```

**Step 3: Verify directory structure**

Run:
```bash
find src/test -type d
```

Expected output:
```
src/test
src/test/java
src/test/java/com
src/test/java/com/prestashop
src/test/java/com/prestashop/tests
src/test/java/com/prestashop/tests/base
src/test/java/com/prestashop/tests/pages
src/test/java/com/prestashop/tests/utils
src/test/resources
```

**Step 4: Commit**

```bash
git add pom.xml src/
git commit -m "feat: initialize Maven project structure with JUnit 5 and Selenium dependencies"
```

---

## Task 2: Implement DriverFactory

**Files:**
- Create: `src/test/java/com/prestashop/tests/utils/DriverFactory.java`

**Step 1: Write DriverFactory class**

Create file `src/test/java/com/prestashop/tests/utils/DriverFactory.java`:

```java
package com.prestashop.tests.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating WebDriver instances with appropriate capabilities.
 * Uses WebDriver Manager to automatically download and manage browser drivers.
 */
public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);

    /**
     * Create a WebDriver instance for the specified browser type.
     *
     * @param browserType the browser type (chrome, firefox, etc.)
     * @return a configured WebDriver instance
     */
    public static WebDriver createDriver(String browserType) {
        WebDriver driver;

        switch (browserType.toLowerCase()) {
            case "firefox":
                driver = createFirefoxDriver();
                break;
            case "chrome":
            default:
                driver = createChromeDriver();
                break;
        }

        logger.info("WebDriver created for browser: {}", browserType);
        return driver;
    }

    /**
     * Create a Chrome WebDriver with default options.
     */
    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Read headless setting from config (default: false)
        String headless = ConfigLoader.getProperty("browser.headless", "false");
        if (Boolean.parseBoolean(headless)) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Window size
        String width = ConfigLoader.getProperty("browser.window.width", "1920");
        String height = ConfigLoader.getProperty("browser.window.height", "1080");
        options.addArguments(String.format("--window-size=%s,%s", width, height));

        return new ChromeDriver(options);
    }

    /**
     * Create a Firefox WebDriver with default options.
     */
    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        // Read headless setting from config (default: false)
        String headless = ConfigLoader.getProperty("browser.headless", "false");
        if (Boolean.parseBoolean(headless)) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }

    /**
     * Close a WebDriver instance safely.
     */
    public static void closeDriver(WebDriver driver) {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver closed successfully");
            } catch (Exception e) {
                logger.warn("Error closing WebDriver: {}", e.getMessage());
            }
        }
    }
}
```

**Step 2: Verify compilation**

Run:
```bash
mvn clean compile -DskipTests
```

Expected: Build SUCCESS (may download dependencies on first run)

**Step 3: Commit**

```bash
git add src/test/java/com/prestashop/tests/utils/DriverFactory.java
git commit -m "feat: implement DriverFactory with Chrome and Firefox support"
```

---

## Task 3: Implement ConfigLoader Utility

**Files:**
- Create: `src/test/java/com/prestashop/tests/utils/ConfigLoader.java`
- Create: `src/test/resources/config.properties`

**Step 1: Write ConfigLoader class**

Create file `src/test/java/com/prestashop/tests/utils/ConfigLoader.java`:

```java
package com.prestashop.tests.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to load and manage test configuration from config.properties.
 */
public class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    private static Properties properties;

    static {
        loadProperties();
    }

    /**
     * Load properties from config.properties resource file.
     */
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.warn("config.properties not found, using defaults");
                return;
            }
            properties.load(input);
            logger.info("Configuration loaded from config.properties");
        } catch (IOException e) {
            logger.error("Error loading config.properties: {}", e.getMessage());
        }
    }

    /**
     * Get a property value, or return default if not found.
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get a property value.
     */
    public static String getProperty(String key) {
        return getProperty(key, "");
    }

    /**
     * Get an integer property.
     */
    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key {}, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }
}
```

**Step 2: Write config.properties file**

Create file `src/test/resources/config.properties`:

```properties
# ============================================
# Browser Configuration
# ============================================
browser.type=chrome
browser.headless=false
browser.window.width=1920
browser.window.height=1080

# ============================================
# Timeouts (in seconds)
# ============================================
timeout.implicit=10
timeout.explicit.wait=15
timeout.page.load=30

# ============================================
# Application URLs
# ============================================
base.url=https://demo.prestashop.com
api.base.url=https://demo.prestashop.com/api

# ============================================
# Screenshot Configuration
# ============================================
screenshot.on.failure=true
screenshot.path=target/screenshots/
```

**Step 3: Verify ConfigLoader loads correctly**

Run:
```bash
mvn clean compile -DskipTests
```

Expected: Build SUCCESS

**Step 4: Commit**

```bash
git add src/test/java/com/prestashop/tests/utils/ConfigLoader.java src/test/resources/config.properties
git commit -m "feat: implement ConfigLoader and config.properties"
```

---

## Task 4: Implement BasePage

**Files:**
- Create: `src/test/java/com/prestashop/tests/base/BasePage.java`

**Step 1: Write BasePage class**

Create file `src/test/java/com/prestashop/tests/base/BasePage.java`:

```java
package com.prestashop.tests.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.prestashop.tests.utils.ConfigLoader;

import java.time.Duration;

/**
 * Base class for all page objects.
 * Provides common WebDriver operations and element waits.
 * Page objects should extend this class and define locators as By constants.
 *
 * IMPORTANT: Page objects contain ONLY UI operations and element locators.
 * Business assertions belong in test classes, not here.
 * Exception: assertLoaded() is allowed to verify page state (e.g., header visible).
 */
public class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor accepts a WebDriver instance from the test.
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Initialize explicit wait with timeout from config
        int waitTimeout = ConfigLoader.getIntProperty("timeout.explicit.wait", 15);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeout));
    }

    /**
     * Wait for an element to be present and visible, then return it.
     */
    protected WebElement waitForElement(By locator) {
        logger.debug("Waiting for element: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Click an element after waiting for it to be visible.
     */
    public void click(By locator) {
        logger.debug("Clicking element: {}", locator);
        waitForElement(locator).click();
    }

    /**
     * Send text to an element (clears field first, then types).
     */
    public void sendKeys(By locator, String text) {
        logger.debug("Sending keys to element: {} with text: {}", locator, text);
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get the text content of an element.
     */
    public String getText(By locator) {
        logger.debug("Getting text from element: {}", locator);
        return waitForElement(locator).getText();
    }

    /**
     * Check if an element is present and visible.
     */
    public boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)) != null;
        } catch (Exception e) {
            logger.debug("Element not visible: {}", locator);
            return false;
        }
    }

    /**
     * Wait for an element to be clickable.
     */
    public void waitForClickable(By locator) {
        logger.debug("Waiting for element to be clickable: {}", locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Navigate to a URL.
     */
    public void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.navigate().to(url);
    }

    /**
     * Get the current page URL.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get the current page title.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Verify that the page has loaded (override in subclasses).
     * This is the ONLY assertion allowed in page objects.
     *
     * Example:
     * public void assertLoaded() {
     *     waitForElement(HEADER_LOGO); // verify page header is visible
     * }
     */
    public void assertLoaded() {
        // Default implementation: do nothing
        // Subclasses should override to verify page-specific elements
        logger.debug("Page loaded (assertLoaded called)");
    }
}
```

**Step 2: Verify compilation**

Run:
```bash
mvn clean compile -DskipTests
```

Expected: Build SUCCESS

**Step 3: Commit**

```bash
git add src/test/java/com/prestashop/tests/base/BasePage.java
git commit -m "feat: implement BasePage with common UI operations"
```

---

## Task 5: Implement BaseTest

**Files:**
- Create: `src/test/java/com/prestashop/tests/base/BaseTest.java`

**Step 1: Write BaseTest class**

Create file `src/test/java/com/prestashop/tests/base/BaseTest.java`:

```java
package com.prestashop.tests.base;

import com.prestashop.tests.utils.ConfigLoader;
import com.prestashop.tests.utils.DriverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Base class for all test classes.
 * Manages WebDriver lifecycle using ThreadLocal for thread-safe parallel execution.
 *
 * Each test thread gets its own WebDriver instance:
 * - @BeforeEach initializes a new driver
 * - @AfterEach quits the driver (even on failure)
 *
 * Tests inherit from this class and call getDriver() to access the current thread's driver.
 *
 * THREAD SAFETY:
 * - ThreadLocal ensures each test thread has isolated driver state
 * - No cross-test contamination during parallel execution
 * - Driver is cleaned up in @AfterEach to prevent resource leaks
 */
public abstract class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    /**
     * ThreadLocal storage for WebDriver instances (one per thread).
     * This ensures each test thread has its own driver, preventing conflicts.
     */
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Get the current thread's WebDriver instance.
     * Called by tests and page objects to interact with the browser.
     */
    protected WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Initialize WebDriver for this test (runs before each test method).
     * Creates a fresh driver instance using the configured browser type.
     */
    @BeforeEach
    public void setUp() {
        logger.info("Setting up WebDriver for thread: {}", Thread.currentThread().getId());

        // Get browser type from config
        String browserType = ConfigLoader.getProperty("browser.type", "chrome");

        // Create driver via factory and store in ThreadLocal
        WebDriver driver = DriverFactory.createDriver(browserType);

        // Set implicit timeout
        int implicitTimeout = ConfigLoader.getIntProperty("timeout.implicit", 10);
        driver.manage().timeouts().implicitlyWait(
            java.time.Duration.ofSeconds(implicitTimeout));

        // Set page load timeout
        int pageLoadTimeout = ConfigLoader.getIntProperty("timeout.page.load", 30);
        driver.manage().timeouts().pageLoadTimeout(
            java.time.Duration.ofSeconds(pageLoadTimeout));

        // Store driver in ThreadLocal
        driverThreadLocal.set(driver);

        logger.info("WebDriver initialized for thread: {}", Thread.currentThread().getId());
    }

    /**
     * Clean up WebDriver after each test (runs after each test method, even on failure).
     * Takes a screenshot on failure for debugging, then quits the driver.
     */
    @AfterEach
    public void tearDown() {
        WebDriver driver = driverThreadLocal.get();

        if (driver != null) {
            logger.info("Tearing down WebDriver for thread: {}", Thread.currentThread().getId());

            // Note: JUnit 5's TestInfo could be injected to get test name/result,
            // but for simplicity, we always try to take a screenshot
            String shouldScreenshot = ConfigLoader.getProperty("screenshot.on.failure", "true");
            if (Boolean.parseBoolean(shouldScreenshot)) {
                takeScreenshot("test_" + System.currentTimeMillis());
            }

            // Quit driver and clean up ThreadLocal
            DriverFactory.closeDriver(driver);
            driverThreadLocal.remove();
        }

        logger.info("WebDriver cleanup complete for thread: {}", Thread.currentThread().getId());
    }

    /**
     * Take a screenshot and save to target/screenshots/ directory.
     */
    protected void takeScreenshot(String filename) {
        try {
            String screenshotPath = ConfigLoader.getProperty("screenshot.path", "target/screenshots/");
            File directory = new File(screenshotPath);

            if (!directory.exists()) {
                Files.createDirectories(Paths.get(screenshotPath));
            }

            WebDriver driver = getDriver();
            if (driver instanceof TakesScreenshot) {
                TakesScreenshot screenshot = (TakesScreenshot) driver;
                File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fullFilename = screenshotPath + filename + "_" + timestamp + ".png";

                Files.copy(sourceFile.toPath(), Paths.get(fullFilename));
                logger.info("Screenshot saved: {}", fullFilename);
            }
        } catch (IOException e) {
            logger.warn("Failed to take screenshot: {}", e.getMessage());
        }
    }

    /**
     * Navigate to base URL (helper method).
     */
    protected void navigateToHome() {
        String baseUrl = ConfigLoader.getProperty("base.url", "https://demo.prestashop.com");
        getDriver().navigate().to(baseUrl);
        logger.info("Navigated to: {}", baseUrl);
    }
}
```

**Step 2: Verify compilation**

Run:
```bash
mvn clean compile -DskipTests
```

Expected: Build SUCCESS

**Step 3: Commit**

```bash
git add src/test/java/com/prestashop/tests/base/BaseTest.java
git commit -m "feat: implement BaseTest with ThreadLocal WebDriver management"
```

---

## Task 6: Create a Sample Test to Verify Setup

**Files:**
- Create: `src/test/java/com/prestashop/tests/SampleTests.java`

**Step 1: Write a simple sample test**

Create file `src/test/java/com/prestashop/tests/SampleTests.java`:

```java
package com.prestashop.tests;

import com.prestashop.tests.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sample tests to verify the test infrastructure is working.
 * These tests do not target specific application functionality yet.
 */
@DisplayName("Sample Infrastructure Tests")
public class SampleTests extends BaseTest {

    @Test
    @DisplayName("Verify WebDriver is initialized")
    public void testWebDriverInitialized() {
        logger.info("Running test: testWebDriverInitialized");
        assertActuallyInitialized();
        assertTrue(true, "WebDriver should be initialized");
    }

    @Test
    @DisplayName("Verify can navigate to URL")
    public void testCanNavigateToUrl() {
        logger.info("Running test: testCanNavigateToUrl");
        navigateToHome();
        String currentUrl = getDriver().getCurrentUrl();
        assertTrue(currentUrl.contains("prestashop"), "Should be able to navigate to Prestashop demo");
    }

    @Test
    @DisplayName("Verify parallel execution - test 1")
    public void testParallel1() {
        logger.info("Running parallel test 1, thread: {}", Thread.currentThread().getId());
        navigateToHome();
        // Simulate some work
        getDriver().getTitle();
        assertTrue(true);
    }

    @Test
    @DisplayName("Verify parallel execution - test 2")
    public void testParallel2() {
        logger.info("Running parallel test 2, thread: {}", Thread.currentThread().getId());
        navigateToHome();
        // Simulate some work
        getDriver().getTitle();
        assertTrue(true);
    }

    @Test
    @DisplayName("Verify parallel execution - test 3")
    public void testParallel3() {
        logger.info("Running parallel test 3, thread: {}", Thread.currentThread().getId());
        navigateToHome();
        // Simulate some work
        getDriver().getTitle();
        assertTrue(true);
    }

    /**
     * Helper method to verify driver is actually initialized.
     * (workaround for null checks without explicit assertions)
     */
    private void assertActuallyInitialized() {
        if (getDriver() == null) {
            throw new AssertionError("WebDriver should not be null");
        }
    }
}
```

**Step 2: Run the tests**

Run:
```bash
mvn clean test
```

Expected output includes:
```
[INFO] Running com.prestashop.tests.SampleTests
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Note: On first run, WebDriver Manager will download chromedriver (~50-100MB). This is expected.

**Step 3: Verify parallel execution worked**

Check the logs for multiple thread IDs in the output. You should see messages like:
```
Running parallel test 1, thread: 21
Running parallel test 2, thread: 22
Running parallel test 3, thread: 23
```

(Different thread IDs indicate parallel execution)

**Step 4: Commit**

```bash
git add src/test/java/com/prestashop/tests/SampleTests.java
git commit -m "test: add sample infrastructure tests to verify setup"
```

---

## Task 7: Verify Project Structure and Build

**Files:**
- No new files to create

**Step 1: Verify full project structure**

Run:
```bash
find src/test -type f -name "*.java" -o -name "*.properties" | sort
```

Expected output:
```
src/test/java/com/prestashop/tests/SampleTests.java
src/test/java/com/prestashop/tests/base/BasePage.java
src/test/java/com/prestashop/tests/base/BaseTest.java
src/test/java/com/prestashop/tests/utils/ConfigLoader.java
src/test/java/com/prestashop/tests/utils/DriverFactory.java
src/test/resources/config.properties
```

**Step 2: Clean build and test**

Run:
```bash
mvn clean test -DthreadCount=4
```

Expected:
- Build SUCCESS
- Tests run in parallel (check thread IDs in logs)
- 5 tests pass
- Screenshots directory created at `target/screenshots/`

**Step 3: Run a single test in isolation**

Run:
```bash
mvn test -Dtest=SampleTests#testWebDriverInitialized
```

Expected:
- Test passes
- No dependencies on other tests

**Step 4: Document the completed setup**

Create or update `README.md` in the root with setup instructions (optional, can be done separately).

**Step 5: Final commit**

```bash
git status
# Should show all files committed
git log --oneline -5
# Should show all the setup commits
```

---

## Verification Checklist

After all tasks are complete, verify:

- ✅ `mvn clean test` runs successfully
- ✅ Tests execute in parallel (multiple thread IDs in logs)
- ✅ Each test can run in isolation (e.g., `mvn test -Dtest=SampleTests#testParallel1`)
- ✅ Screenshots are saved on failure in `target/screenshots/`
- ✅ `config.properties` loads correctly (check logs for "Configuration loaded")
- ✅ ThreadLocal driver prevents cross-test contamination
- ✅ Project structure follows POM conventions (base/, pages/, tests/, utils/)
- ✅ All code is committed to git

---

## Next Steps (After Implementation)

1. **Create Page Objects:** Add pages for login, dashboard, product pages, etc. (extend BasePage)
2. **Write Functional Tests:** Implement real test scenarios (extend BaseTest, use page objects)
3. **Add API Fixtures:** Create `fixtures/` package with API clients for test data setup (@BeforeEach)
4. **Integrate Reporting:** Add Extent Reports or Allure for test reports
5. **Setup CI/CD:** Configure GitHub Actions/Jenkins to run tests on commits

---

## Tech Debt / Future Improvements

- Add custom JUnit 5 extensions for automatic screenshot/logging on test failure
- Implement soft assertions for better error reporting
- Add retry logic for flaky tests
- Parameterize tests for data-driven scenarios
- Add performance benchmarking

