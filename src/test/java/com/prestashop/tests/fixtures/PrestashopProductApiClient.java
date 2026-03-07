package com.prestashop.tests.fixtures;

import com.prestashop.tests.utils.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API client for Prestashop product management via JSON-based Admin API.
 * Handles product creation, deletion, and updates.
 */
public class PrestashopProductApiClient {
    private static final Logger logger = LoggerFactory.getLogger(PrestashopProductApiClient.class);

    private final String baseUrl;
    private final String apiKey;
    private final HttpClient httpClient;

    public PrestashopProductApiClient() {
        this.baseUrl = ConfigLoader.getProperty("api.base.url", "http://145.239.29.235/api");
        this.apiKey = ConfigLoader.getProperty("api.key", "");
        this.httpClient = HttpClient.newHttpClient();

        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.warn("API key not configured in config.properties");
        }

        logger.info("Prestashop Product API client initialized with base URL: {}", baseUrl);
    }

    /**
     * Create a new product via JSON API.
     *
     * @param name product name
     * @param price product price
     * @return product ID if successful, -1 if failed
     */
    public long createProduct(String name, double price) {
        logger.info("Creating product: {} with price: {}", name, price);
        try {
            String xmlBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<prestashop>" +
                    "<product>" +
                    "<name><language id=\"1\">" + escapeXml(name) + "</language></name>" +
                    "<description><language id=\"1\">Test product</language></description>" +
                    "<price>" + price + "</price>" +
                    "<active>1</active>" +
                    "</product>" +
                    "</prestashop>";

            String endpoint = "/products?ws_key=" + apiKey + "&output_format=JSON";
            String response = makeRequest("POST", endpoint, xmlBody, "application/xml");

            if (response == null || response.isEmpty()) {
                logger.warn("No response from API when creating product: {}", name);
                return -1;
            }

            logger.debug("Product creation API response: {}", response);

            Pattern idPattern = Pattern.compile("\"id\"\\s*:\\s*[\"]?(\\d+)[\"]?");
            Matcher matcher = idPattern.matcher(response);

            if (matcher.find()) {
                long productId = Long.parseLong(matcher.group(1));
                logger.info("Product created successfully with ID {} for name: {}", productId, name);
                return productId;
            } else {
                logger.warn("Could not extract product ID from API response: {}", response);
                return -1;
            }
        } catch (Exception e) {
            logger.error("Failed to create product", e);
            return -1;
        }
    }

    /**
     * Delete a product via API.
     *
     * @param productId product ID to delete
     * @return true if successful
     */
    public boolean deleteProduct(long productId) {
        logger.info("Deleting product: {}", productId);
        try {
            String endpoint = "/products/" + productId + "?ws_key=" + apiKey;
            makeRequest("DELETE", endpoint, null);
            logger.info("Product {} deleted successfully", productId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to delete product: {}", productId, e);
            return false;
        }
    }

    /**
     * Create a product with combinations (variants).
     *
     * @param name product name
     * @param basePrice base product price
     * @param attributes comma-separated attribute values (e.g., "S,M,L")
     * @return product ID if successful, -1 if failed
     */
    public long createProductWithCombinations(String name, double basePrice, String attributes) {
        logger.info("Creating product with combinations: {}", name);
        try {
            String xmlBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<prestashop>" +
                    "<product>" +
                    "<name><language id=\"1\">" + escapeXml(name) + "</language></name>" +
                    "<description><language id=\"1\">Test product with combinations</language></description>" +
                    "<price>" + basePrice + "</price>" +
                    "<active>1</active>" +
                    "</product>" +
                    "</prestashop>";

            String endpoint = "/products?ws_key=" + apiKey + "&output_format=JSON";
            String response = makeRequest("POST", endpoint, xmlBody, "application/xml");

            if (response == null || response.isEmpty()) {
                logger.warn("No response from API when creating product with combinations: {}", name);
                return -1;
            }

            Pattern idPattern = Pattern.compile("\"id\"\\s*:\\s*[\"]?(\\d+)[\"]?");
            Matcher matcher = idPattern.matcher(response);

            if (matcher.find()) {
                long productId = Long.parseLong(matcher.group(1));
                logger.info("Product with combinations created with ID {} for name: {}", productId, name);
                return productId;
            } else {
                logger.warn("Could not extract product ID from API response");
                return -1;
            }
        } catch (Exception e) {
            logger.error("Failed to create product with combinations", e);
            return -1;
        }
    }

    /**
     * Make HTTP request with default JSON content type.
     */
    private String makeRequest(String method, String endpoint, String body) throws Exception {
        return makeRequest(method, endpoint, body, "application/json");
    }

    /**
     * Make HTTP request with custom content type.
     */
    private String makeRequest(String method, String endpoint, String body, String contentType) throws Exception {
        String url = baseUrl + endpoint;
        logger.debug("Making {} request to: {} with content-type: {}", method, url, contentType);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", contentType);

        switch (method.toUpperCase()) {
            case "POST":
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));
                break;
            case "PUT":
                requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));
                break;
            case "DELETE":
                requestBuilder.DELETE();
                break;
            case "GET":
            default:
                requestBuilder.GET();
                break;
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            logger.debug("API request successful ({})", response.statusCode());
            return response.body();
        } else {
            logger.error("API request failed ({}): {}", response.statusCode(), response.body());
            return null;
        }
    }

    /**
     * Escape special characters in XML strings.
     */
    private String escapeXml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
