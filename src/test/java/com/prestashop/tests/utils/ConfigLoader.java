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
