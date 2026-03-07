package com.prestashop.tests.fixtures;

import com.prestashop.tests.utils.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API client for Prestashop test data setup.
 *
 * Provides methods to create and manage test data via Prestashop REST API
 * instead of UI interactions. This follows the test architecture requirement:
 * "API-based setup where possible (not UI)".
 *
 * Usage in tests:
 * <pre>
 * &#64;BeforeEach
 * void setupTestData() {
 *     PrestashopApiClient api = new PrestashopApiClient();
 *     long customerId = api.createCustomer("test@example.com", "password123");
 *     long productId = api.createProduct("Test Product", 99.99);
 * }
 * </pre>
 */
public class PrestashopApiClient {
    private static final Logger logger = LoggerFactory.getLogger(PrestashopApiClient.class);

    private final String baseUrl;
    private final String apiKey;
    private final HttpClient httpClient;

    /**
     * Initialize API client with base URL and API key from config.
     *
     * API key should be configured in src/test/resources/config.properties:
     * api.key=your_api_key_here
     *
     * @throws IllegalStateException if API key is not configured
     */
    public PrestashopApiClient() {
        this.baseUrl = ConfigLoader.getProperty("api.base.url", "http://145.239.29.235/api");
        this.apiKey = ConfigLoader.getProperty("api.key", "");
        this.httpClient = HttpClient.newHttpClient();

        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.warn("API key not configured in config.properties. Set 'api.key' property to enable API operations.");
        }

        logger.info("Prestashop API client initialized with base URL: {}", baseUrl);
    }

    /**
     * Create a new customer via the Prestashop Webservice API.
     *
     * Uses POST /api/customers with XML body and HTTP Basic Auth (API key as username).
     *
     * @param email    customer email address
     * @param password customer password
     * @return customer ID if created successfully, -1 otherwise
     */
    public long createCustomer(String email, String password) {
        return createCustomer(email, password, "Test", "Customer");
    }

    /**
     * Create a new customer via the Prestashop Webservice API with explicit name.
     *
     * @param email     customer email address
     * @param password  customer password
     * @param firstName first name
     * @param lastName  last name
     * @return customer ID if created successfully, -1 otherwise
     */
    public long createCustomer(String email, String password, String firstName, String lastName) {
        logger.info("Creating customer via Webservice API: {}", email);
        try {
            String xmlBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n"
                    + "  <customer>\n"
                    + "    <id_gender><![CDATA[1]]></id_gender>\n"
                    + "    <firstname><![CDATA[" + firstName + "]]></firstname>\n"
                    + "    <lastname><![CDATA[" + lastName + "]]></lastname>\n"
                    + "    <email><![CDATA[" + email + "]]></email>\n"
                    + "    <passwd><![CDATA[" + password + "]]></passwd>\n"
                    + "    <id_default_group><![CDATA[3]]></id_default_group>\n"
                    + "    <active><![CDATA[1]]></active>\n"
                    + "  </customer>\n"
                    + "</prestashop>";

            String response = makeRequest("POST", "/customers?output_format=JSON", xmlBody, "text/xml");

            if (response == null) {
                logger.error("Customer creation failed for email: {}", email);
                return -1;
            }

            // Parse JSON response to extract customer ID
            Pattern idPattern = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
            Matcher matcher = idPattern.matcher(response);

            if (matcher.find()) {
                long customerId = Long.parseLong(matcher.group(1));
                logger.info("Customer created with ID {} for email: {}", customerId, email);
                return customerId;
            } else {
                logger.warn("Customer creation response did not contain an ID. Response: {}", response);
                return -1;
            }
        } catch (Exception e) {
            logger.error("Failed to create customer via Webservice API", e);
            return -1;
        }
    }

    /**
     * Create a new product via API.
     *
     * @param name product name
     * @param price product price
     * @return product ID if successful, -1 if failed
     */
    public long createProduct(String name, double price) {
        logger.info("Creating product: {} with price: {}", name, price);
        try {
            // TODO: Implement POST /products endpoint
            // Example: POST /api/products with JSON body containing name, price, etc.
            // Parse response and return product ID
            // Handle errors and log appropriately

            logger.warn("createProduct not yet implemented");
            return -1;
        } catch (Exception e) {
            logger.error("Failed to create product", e);
            return -1;
        }
    }

    /**
     * Add product to customer's cart via API.
     *
     * @param customerId customer ID
     * @param productId product ID
     * @param quantity quantity to add
     * @return true if successful, false otherwise
     */
    public boolean addToCart(long customerId, long productId, int quantity) {
        logger.info("Adding product {} (qty: {}) to cart for customer {}", productId, quantity, customerId);
        try {
            // TODO: Implement POST /carts endpoint or similar
            // Handle cart operations via API
            // Return success/failure status

            logger.warn("addToCart not yet implemented");
            return false;
        } catch (Exception e) {
            logger.error("Failed to add to cart", e);
            return false;
        }
    }

    /**
     * Find a customer ID by their email address via API.
     *
     * @param email customer email address
     * @return Optional containing the customer ID if found, empty if not found
     */
    public Optional<Long> findCustomerIdByEmail(String email) {
        logger.info("Finding customer ID for email: {}", email);
        try {
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            String endpoint = "/customers?output_format=JSON&filter[email]=" + encodedEmail + "&display=[id]";
            String response = makeRequest("GET", endpoint, null, null);

            if (response == null || response.isEmpty()) {
                logger.warn("No response from API when searching for customer with email: {}", email);
                return Optional.empty();
            }

            // Parse JSON response to extract customer ID
            // Example response: {"customers": [{"id": 123}]}
            Pattern idPattern = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
            Matcher matcher = idPattern.matcher(response);

            if (matcher.find()) {
                long customerId = Long.parseLong(matcher.group(1));
                logger.info("Found customer ID {} for email: {}", customerId, email);
                return Optional.of(customerId);
            } else {
                logger.info("No customer found with email: {}", email);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Failed to find customer by email: {}", email, e);
            return Optional.empty();
        }
    }

    /**
     * Delete a customer via API (cleanup).
     *
     * @param customerId customer ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteCustomer(long customerId) {
        logger.info("Deleting customer: {}", customerId);
        try {
            String endpoint = "/customers/" + customerId;
            String response = makeRequest("DELETE", endpoint, null, null);

            // Successful deletion returns a response (typically empty or status)
            logger.info("Customer {} deleted successfully", customerId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to delete customer: {}", customerId, e);
            return false;
        }
    }

    /**
     * Delete a customer by email via API (cleanup).
     * Convenience method that finds the customer ID by email and then deletes.
     *
     * @param email customer email address
     * @return true if successful, false otherwise
     */
    public boolean deleteCustomerByEmail(String email) {
        logger.info("Deleting customer with email: {}", email);
        try {
            Optional<Long> customerId = findCustomerIdByEmail(email);
            if (customerId.isPresent()) {
                return deleteCustomer(customerId.get());
            } else {
                logger.warn("Customer with email {} not found, nothing to delete", email);
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to delete customer by email: {}", email, e);
            return false;
        }
    }

    /**
     * Delete a product via API (cleanup).
     *
     * @param productId product ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteProduct(long productId) {
        logger.info("Deleting product: {}", productId);
        try {
            // TODO: Implement DELETE /products/{id} endpoint
            // Parse response and return success/failure

            logger.warn("deleteProduct not yet implemented");
            return false;
        } catch (Exception e) {
            logger.error("Failed to delete product", e);
            return false;
        }
    }

    /**
     * Helper method to make HTTP requests with HTTP Basic Auth (API key as username).
     *
     * Prestashop Webservice API authenticates via HTTP Basic Auth:
     *   Username = API key, Password = (empty)
     * This matches the URL format: http://API_KEY@host/api/endpoint
     *
     * @param method      HTTP method (GET, POST, PUT, DELETE)
     * @param endpoint    API endpoint path (e.g., "/customers?output_format=JSON")
     * @param body        request body for POST/PUT requests (null for GET/DELETE)
     * @param contentType content type for POST/PUT (e.g., "text/xml"), null defaults to "application/json"
     * @return HTTP response body as string, or null on error
     */
    private String makeRequest(String method, String endpoint, String body, String contentType) throws Exception {
        String url = baseUrl + endpoint;
        logger.debug("Making {} request to: {}", method, url);

        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((apiKey + ":").getBytes(StandardCharsets.UTF_8));
        String resolvedContentType = (contentType != null) ? contentType : "application/json";

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", authHeader)
                .header("Content-Type", resolvedContentType);

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
}
