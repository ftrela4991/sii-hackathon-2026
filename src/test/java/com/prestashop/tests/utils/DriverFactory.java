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
 *
 * THREAD SAFETY: Each test thread must have its own WebDriver instance.
 * Use ThreadLocal in BaseTest to store per-thread drivers. This factory
 * is thread-safe for creating drivers in parallel execution.
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

        logger.info("WebDriver created for browser: {}", browserType.toLowerCase());
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
        String width = ConfigLoader.getProperty("browser.window.width", "1920");
        String height = ConfigLoader.getProperty("browser.window.height", "1080");
        logger.debug("Chrome config - headless: {}, window: {}x{}", headless, width, height);
        if (Boolean.parseBoolean(headless)) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");  // Stability in headless
        if (Boolean.parseBoolean(headless)) {
            options.addArguments("--disable-extensions");
        }
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        // Window size
        try {
            int widthVal = Integer.parseInt(width);
            int heightVal = Integer.parseInt(height);
            if (widthVal > 0 && heightVal > 0) {
                options.addArguments(String.format("--window-size=%d,%d", widthVal, heightVal));
            }
        } catch (NumberFormatException e) {
            logger.warn("Invalid window size config: {}x{}, using defaults", width, height);
        }

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
                logger.warn("Error closing WebDriver", e);
            }
        }
    }
}
