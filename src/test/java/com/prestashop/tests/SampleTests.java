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
    @DisplayName("Verify WebDriver is initialized")
    public void testWebDriverInitialized() {
        logger.info("Running test: testWebDriverInitialized");
        assertActuallyInitialized();
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
    @DisplayName("Verify parallel execution - thread 1")
    public void testCanExecuteInParallelThread1() {
        logger.info("Running parallel test 1, thread: {}", Thread.currentThread().getId());
        navigateToHome();
        String title = getDriver().getTitle();
        assertNotNull(title, "Page title should be available");
        assertTrue(title.length() > 0, "Page title should not be empty");
    }

    @Test
    @DisplayName("Verify parallel execution - thread 2")
    public void testCanExecuteInParallelThread2() {
        logger.info("Running parallel test 2, thread: {}", Thread.currentThread().getId());
        navigateToHome();
        String title = getDriver().getTitle();
        assertNotNull(title, "Page title should be available");
        assertTrue(title.length() > 0, "Page title should not be empty");
    }

    @Test
    @DisplayName("Verify parallel execution - thread 3")
    public void testCanExecuteInParallelThread3() {
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
