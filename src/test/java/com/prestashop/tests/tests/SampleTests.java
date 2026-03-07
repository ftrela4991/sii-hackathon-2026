package com.prestashop.tests;

import com.prestashop.tests.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sample tests to verify the test infrastructure is working.
 * These tests do not target specific application functionality yet.
 *
 * Use these as reference for structure:
 * - Each test extends BaseTest
 * - Tests call getDriver() to access WebDriver
 * - Tests log thread IDs to verify parallel execution
 */
@DisplayName("Sample Infrastructure Tests")
public class SampleTests extends BaseTest {

    @Test
    @DisplayName("Should initialize WebDriver successfully")
    public void shouldInitializeWebDriver() {
        logger.info("Running test: shouldInitializeWebDriver");
        assertActuallyInitialized();
    }

    @Test
    @DisplayName("Should navigate to home URL successfully")
    public void shouldNavigateToHomeUrl() {
        logger.info("Running test: shouldNavigateToHomeUrl");
        navigateToHome();
        String currentUrl = getDriver().getCurrentUrl();
        assertNotNull(currentUrl, "Current URL should not be null");
        assertTrue(currentUrl.length() > 0, "Should be able to navigate to home page");
    }

    @Test
    @DisplayName("Should execute in parallel - thread 1")
    public void shouldExecuteInParallel1() {
        logger.info("Running parallel test 1, thread: {}", Thread.currentThread().getId());
        navigateToHome();
        String title = getDriver().getTitle();
        assertNotNull(title, "Page title should be available");
        assertTrue(title.length() > 0, "Page title should not be empty");
    }

    @Test
    @DisplayName("Should execute in parallel - thread 2")
    public void shouldExecuteInParallel2() {
        logger.info("Running parallel test 2, thread: {}", Thread.currentThread().getId());
        navigateToHome();
        String title = getDriver().getTitle();
        assertNotNull(title, "Page title should be available");
        assertTrue(title.length() > 0, "Page title should not be empty");
    }

    @Test
    @DisplayName("Should execute in parallel - thread 3")
    public void shouldExecuteInParallel3() {
        logger.info("Running parallel test 3, thread: {}", Thread.currentThread().getId());
        navigateToHome();
        String title = getDriver().getTitle();
        assertNotNull(title, "Page title should be available");
        assertTrue(title.length() > 0, "Page title should not be empty");
    }

    /**
     * Helper method to verify driver is actually initialized.
     */
    private void assertActuallyInitialized() {
        assertNotNull(getDriver(), "WebDriver should not be null");
    }
}
