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
        properties = new Properties();
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
            logger.error("Error loading config.properties", e);
        }
    }

    /**
     * Get a property value with a default fallback.
     * @param key the property key to retrieve
     * @param defaultValue the value to return if key not found
     * @return the property value or defaultValue if not found
     */
    public static String getProperty(String key, String defaultValue) {
        if (key == null || key.trim().isEmpty()) {
            logger.warn("Property key cannot be null or empty");
            return defaultValue;
        }
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get a property value as a string.
     * @param key the property key to retrieve
     * @return the property value, or empty string if not found
     */
    public static String getProperty(String key) {
        return getProperty(key, "");
    }

    /**
     * Get a property value as an integer.
     * @param key the property key to retrieve
     * @param defaultValue the value to return if key not found or invalid
     * @return the property value parsed as integer, or defaultValue if invalid
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key, "");
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key {}, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }
}
