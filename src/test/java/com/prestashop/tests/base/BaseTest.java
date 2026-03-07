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

        try {
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
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver", e);
            driverThreadLocal.remove();  // Ensure cleanup on failure
            throw e;  // Fail the test setup
        }
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
            if (shouldTakeScreenshot()) {
                takeScreenshot("test_" + System.currentTimeMillis());
            }

            // Quit driver and clean up ThreadLocal
            DriverFactory.closeDriver(driver);
            driverThreadLocal.remove();
        } else {
            logger.warn("WebDriver is null during tearDown - may have been already cleaned up");
        }

        logger.info("WebDriver cleanup complete for thread: {}", Thread.currentThread().getId());
    }

    /**
     * Check if screenshots should be taken based on configuration.
     * Validates the screenshot.on.failure config value.
     *
     * @return true if screenshots should be taken, false otherwise
     */
    private boolean shouldTakeScreenshot() {
        String value = ConfigLoader.getProperty("screenshot.on.failure", "true");
        // Validate the configuration value
        if (value == null || !value.trim().equalsIgnoreCase("true")) {
            if (!value.equalsIgnoreCase("false")) {
                logger.warn("Invalid screenshot.on.failure config value: {}. Use 'true' or 'false'", value);
            }
            return false;
        }
        return true;
    }

    /**
     * Take a screenshot and save to target/screenshots/ directory.
     * Creates a timestamped PNG file with the specified filename prefix.
     *
     * @param filename the filename prefix (timestamp will be appended)
     */
    protected void takeScreenshot(String filename) {
        try {
            WebDriver driver = getDriver();
            if (driver == null) {
                logger.warn("Cannot take screenshot: WebDriver is null");
                return;
            }

            String screenshotPath = ConfigLoader.getProperty("screenshot.path", "target/screenshots/");
            File directory = new File(screenshotPath);

            if (!directory.exists()) {
                try {
                    Files.createDirectories(Paths.get(screenshotPath));
                } catch (IOException e) {
                    logger.error("Failed to create screenshot directory: {}", screenshotPath, e);
                    return;
                }
            }

            if (driver instanceof TakesScreenshot) {
                TakesScreenshot screenshot = (TakesScreenshot) driver;
                File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fullFilename = screenshotPath + filename + "_" + timestamp + ".png";

                Files.copy(sourceFile.toPath(), Paths.get(fullFilename));
                logger.info("Screenshot saved: {}", fullFilename);

                // Clean up temporary file
                if (!sourceFile.delete()) {
                    logger.warn("Failed to delete temporary screenshot file: {}", sourceFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            logger.warn("Failed to take screenshot: {}", filename, e);
        }
    }

    /**
     * Navigate to base URL (helper method).
     * Requires WebDriver to be initialized (from @BeforeEach).
     *
     * @throws IllegalStateException if WebDriver is not initialized
     */
    protected void navigateToHome() {
        WebDriver driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Ensure setUp() has completed.");
        }
        String baseUrl = ConfigLoader.getProperty("base.url", "https://demo.prestashop.com");
        driver.navigate().to(baseUrl);
        logger.info("Navigated to: {}", baseUrl);
    }

    /**
     * Navigate to a specific path relative to base URL (helper method).
     * Requires WebDriver to be initialized (from @BeforeEach).
     *
     * @param path the path to navigate to (relative to base URL, e.g., "/registration", "/login")
     * @throws IllegalStateException if WebDriver is not initialized
     */
    protected void navigateTo(String path) {
        WebDriver driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Ensure setUp() has completed.");
        }
        String baseUrl = ConfigLoader.getProperty("base.url");
        String fullUrl = baseUrl + (path.startsWith("/") ? "" : "/") + path;
        driver.navigate().to(fullUrl);
        logger.info("Navigated to: {}", fullUrl);
    }
}
