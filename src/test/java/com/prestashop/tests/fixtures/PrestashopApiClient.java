package com.prestashop.tests.fixtures;

import com.prestashop.tests.utils.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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

    // Admin panel session — initialised lazily on first admin operation
    private HttpClient adminHttpClient;

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
     * Create a new customer via the registration form endpoint.
     *
     * Uses POST /registration with application/x-www-form-urlencoded body,
     * mirroring the browser fetch that the real registration form submits.
     * On success the server returns a 302 redirect; the customer ID is then
     * fetched via the REST API using the email address.
     *
     * Required form fields (verified against live app):
     *   id_gender, firstname, lastname, email, password,
     *   psgdpr=1, customer_privacy=1, submitCreate=1
     *
     * @param email    customer email address
     * @param password customer password
     * @return customer ID if created successfully, -1 otherwise
     */
    public long createCustomer(String email, String password) {
        return createCustomer(email, password, "Test", "Customer");
    }

    /**
     * Create a new customer via the registration form endpoint with explicit name.
     *
     * @param email     customer email address
     * @param password  customer password
     * @param firstName first name
     * @param lastName  last name
     * @return customer ID if created successfully, -1 otherwise
     */
    public long createCustomer(String email, String password, String firstName, String lastName) {
        logger.info("Creating customer via registration form: {}", email);
        try {
            String registrationUrl = ConfigLoader.getProperty("base.url", "http://145.239.29.235/")
                    + "/registration";

            // Build form body identical to the browser POST observed via DevTools:
            // id_gender=1&firstname=...&lastname=...&email=...&password=...
            // &birthday=&psgdpr=1&customer_privacy=1&submitCreate=1
            String formBody = "id_gender=1"
                    + "&firstname=" + URLEncoder.encode(firstName, StandardCharsets.UTF_8)
                    + "&lastname="  + URLEncoder.encode(lastName,  StandardCharsets.UTF_8)
                    + "&email="     + URLEncoder.encode(email,     StandardCharsets.UTF_8)
                    + "&password="  + URLEncoder.encode(password,  StandardCharsets.UTF_8)
                    + "&birthday="
                    + "&psgdpr=1"
                    + "&customer_privacy=1"
                    + "&submitCreate=1";

            // Do NOT follow redirects — successful registration returns HTTP 302.
            // If we follow the redirect we lose the status code and cannot detect errors.
            HttpClient nonRedirectingClient = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(registrationUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(formBody))
                    .build();

            HttpResponse<String> response = nonRedirectingClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            logger.debug("Registration response status: {}", response.statusCode());

            // Prestashop returns 302 on successful registration
            if (response.statusCode() == 302 || response.statusCode() == 301) {
                // Retrieve the customer ID from the REST API using the email
                Optional<Long> customerId = findCustomerIdByEmail(email);
                if (customerId.isPresent()) {
                    logger.info("Customer created with ID {} for email: {}", customerId.get(), email);
                    return customerId.get();
                } else {
                    logger.warn("Registration succeeded ({}), but customer ID not found for: {}",
                            response.statusCode(), email);
                    return -1;
                }
            } else {
                logger.error("Registration failed — unexpected status {} for email: {}",
                        response.statusCode(), email);
                return -1;
            }
        } catch (Exception e) {
            logger.error("Failed to create customer via registration form", e);
            return -1;
        }
    }

    /**
     * Create a new product via the Prestashop admin panel.
     * Delegates to {@link #createProduct(String, double, int)} with a default quantity of 10.
     *
     * @param name  product name
     * @param price product price (tax excluded)
     * @return product ID if successful, -1 if failed
     */
    public long createProduct(String name, double price) {
        return createProduct(name, price, 10);
    }

    /**
     * Create a new product via the Prestashop admin panel.
     *
     * Flow:
     * 1. Ensure admin session is active (login once, reuse session)
     * 2. GET the "new product" page — Prestashop creates a draft and redirects to
     *    the edit URL which contains the newly-assigned product ID
     * 3. Extract CSRF tokens from the redirect URL and form HTML
     * 4. POST the product form (name, price, quantity, tokens) to make the product live
     *
     * Admin credentials and URL are read from config.properties:
     *   admin.base.url, admin.email, admin.password
     *
     * @param name     product name
     * @param price    product price (tax excluded)
     * @param quantity initial stock quantity
     * @return product ID if successful, -1 if failed
     */
    public long createProduct(String name, double price, int quantity) {
        logger.info("Creating product via admin panel: name='{}', price={}, qty={}", name, price, quantity);
        try {
            ensureAdminLoggedIn();
            String adminBase = ConfigLoader.getProperty("admin.base.url", "http://145.239.29.235/admin_hackathon");

            // Step 1: GET new product page — Prestashop creates a draft and redirects to
            // /sell/catalog/products-v2/{newId}/edit, giving us the product ID and tokens
            String newProductUrl = adminBase + "/index.php/sell/catalog/products-v2/new";
            HttpResponse<String> newPageResponse = adminHttpClient.send(
                    HttpRequest.newBuilder().uri(new URI(newProductUrl)).GET().build(),
                    HttpResponse.BodyHandlers.ofString());

            // Step 2: Extract product ID from the final (post-redirect) URL
            String finalUrl = newPageResponse.uri().toString();
            long productId = extractProductIdFromUrl(finalUrl);
            if (productId < 0) {
                logger.error("Could not extract product ID from redirect URL: {}", finalUrl);
                return -1;
            }
            logger.debug("New product ID: {}", productId);

            // Step 3: Extract CSRF tokens — URL _token and form product[_token]
            String urlToken = extractAdminToken(finalUrl);
            String formToken = extractInputValue(newPageResponse.body(), "product[_token]");
            logger.debug("URL _token: {}, form product[_token]: {}", urlToken, formToken);

            // Step 4: POST product form to the edit endpoint to set name/price/qty and publish
            String editUrl = adminBase + "/index.php/sell/catalog/products-v2/" + productId
                    + "/edit?forceDefaultActive=0&_token=" + URLEncoder.encode(urlToken, StandardCharsets.UTF_8);
            String formBody = buildProductFormBody(name, price, quantity, formToken);

            HttpResponse<String> editResponse = adminHttpClient.send(
                    HttpRequest.newBuilder()
                            .uri(new URI(editUrl))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(formBody))
                            .build(),
                    HttpResponse.BodyHandlers.ofString());

            logger.info("Product created — ID: {}, HTTP {}", productId, editResponse.statusCode());
            return productId;
        } catch (Exception e) {
            logger.error("Failed to create product via admin panel", e);
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
            String endpoint = "/customers?ws_key=" + apiKey + "&output_format=JSON&filter[email]=" + encodedEmail + "&display=[id]";
            String response = makeRequest("GET", endpoint, null);

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
            String endpoint = "/customers/" + customerId + "?ws_key=" + apiKey;
            String response = makeRequest("DELETE", endpoint, null);

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
     * Delete a product via the Prestashop admin panel.
     *
     * Flow:
     * 1. Ensure admin session is active
     * 2. GET the product list page to retrieve a fresh CSRF token for this route
     * 3. POST to the delete-from-shop endpoint (token in URL, empty body)
     *
     * @param productId product ID to delete
     * @return true if the HTTP response indicates success (status < 400), false otherwise
     */
    public boolean deleteProduct(long productId) {
        logger.info("Deleting product via admin panel: {}", productId);
        try {
            ensureAdminLoggedIn();
            String adminBase = ConfigLoader.getProperty("admin.base.url", "http://145.239.29.235/admin_hackathon");

            // GET product list page to extract a valid CSRF token for the products-v2 route
            String listUrl = adminBase + "/index.php/sell/catalog/products-v2";
            HttpResponse<String> listResponse = adminHttpClient.send(
                    HttpRequest.newBuilder().uri(new URI(listUrl)).GET().build(),
                    HttpResponse.BodyHandlers.ofString());

            // Token may appear in the page HTML (action links) or in the final URL
            String token = extractAdminToken(listResponse.body());
            if (token.isEmpty()) {
                token = extractAdminToken(listResponse.uri().toString());
            }
            logger.debug("Delete _token: {}", token);

            // POST to delete-from-shop (form body is empty; all auth info is in URL + cookies)
            String deleteUrl = adminBase + "/index.php/sell/catalog/products-v2/" + productId
                    + "/delete-from-shop/1?_token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

            HttpResponse<String> deleteResponse = adminHttpClient.send(
                    HttpRequest.newBuilder()
                            .uri(new URI(deleteUrl))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(""))
                            .build(),
                    HttpResponse.BodyHandlers.ofString());

            boolean success = deleteResponse.statusCode() < 400;
            logger.info("Product {} deletion — HTTP {}, success={}", productId, deleteResponse.statusCode(), success);
            return success;
        } catch (Exception e) {
            logger.error("Failed to delete product via admin panel: {}", productId, e);
            return false;
        }
    }

    // =========================================================================
    // Admin panel helpers
    // =========================================================================

    /**
     * Ensure the admin {@link HttpClient} is authenticated.
     * Logs in once on first call and reuses the cookie-backed session for all
     * subsequent admin operations within this {@code PrestashopApiClient} instance.
     *
     * Admin credentials and URL are read from config.properties:
     *   admin.base.url, admin.email, admin.password
     */
    private void ensureAdminLoggedIn() throws Exception {
        if (adminHttpClient != null) {
            return; // session already established
        }

        String adminBase    = ConfigLoader.getProperty("admin.base.url", "http://145.239.29.235/admin_hackathon");
        String adminEmail   = ConfigLoader.getProperty("admin.email",    "admin@local.dev");
        String adminPassword = ConfigLoader.getProperty("admin.password", "hackathon-sii-2026");
        String loginUrl     = adminBase + "/index.php?controller=AdminLogin";

        logger.info("Logging in to admin panel: {}", loginUrl);

        // Shared cookie store — keeps the admin session cookie across all subsequent requests
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        // GET login page to extract the CSRF token embedded in the form
        HttpResponse<String> loginPage = client.send(
                HttpRequest.newBuilder().uri(new URI(loginUrl)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        String loginToken = extractInputValue(loginPage.body(), "_token");
        logger.debug("Admin login CSRF token: {}", loginToken);

        // POST credentials — on success the server redirects to the admin dashboard
        String loginBody = "email="   + URLEncoder.encode(adminEmail,    StandardCharsets.UTF_8)
                + "&passwd="          + URLEncoder.encode(adminPassword,  StandardCharsets.UTF_8)
                + "&_token="          + URLEncoder.encode(loginToken,     StandardCharsets.UTF_8)
                + "&submitLogin=1";

        client.send(
                HttpRequest.newBuilder()
                        .uri(new URI(loginUrl))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(loginBody))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        adminHttpClient = client;
        logger.info("Admin login successful — session established");
    }

    /**
     * Extract the {@code value} attribute of an HTML {@code <input>} element
     * identified by its {@code name} attribute.
     *
     * Handles both attribute orderings:
     *   {@code <input name="foo" value="bar">} and
     *   {@code <input value="bar" name="foo">}
     *
     * @param html HTML source
     * @param name the {@code name} attribute of the input element
     * @return the {@code value} attribute string, or {@code ""} if not found
     */
    private String extractInputValue(String html, String name) {
        String quotedName = Pattern.quote(name);
        // Attempt 1: name attribute appears before value attribute
        Pattern p1 = Pattern.compile(
                "<input[^>]+name=[\"']" + quotedName + "[\"'][^>]+value=[\"']([^\"']*)[\"']",
                Pattern.CASE_INSENSITIVE);
        Matcher m1 = p1.matcher(html);
        if (m1.find()) return m1.group(1);
        // Attempt 2: value attribute appears before name attribute
        Pattern p2 = Pattern.compile(
                "<input[^>]+value=[\"']([^\"']*)[\"'][^>]+name=[\"']" + quotedName + "[\"']",
                Pattern.CASE_INSENSITIVE);
        Matcher m2 = p2.matcher(html);
        if (m2.find()) return m2.group(1);
        return "";
    }

    /**
     * Extract the {@code _token} CSRF parameter value from a URL or HTML fragment.
     *
     * Matches patterns such as {@code ?_token=XXX}, {@code &_token=XXX}, or
     * {@code "_token":"XXX"} (JSON). The token must be at least 20 characters.
     *
     * @param text URL string or HTML/JSON fragment containing the token
     * @return the token value, or {@code ""} if not found
     */
    private String extractAdminToken(String text) {
        Pattern p = Pattern.compile("[?&\"']_token[\"'=:]+([A-Za-z0-9_\\-]{20,})");
        Matcher m = p.matcher(text);
        return m.find() ? m.group(1) : "";
    }

    /**
     * Extract the numeric product ID from a Prestashop admin edit-page URL.
     *
     * Matches the pattern {@code /products-v2/{id}/} in the URL path.
     *
     * @param url admin product edit URL
     * @return numeric product ID, or {@code -1} if the pattern is not found
     */
    private long extractProductIdFromUrl(String url) {
        Pattern p = Pattern.compile("/products-v2/(\\d+)/");
        Matcher m = p.matcher(url);
        return m.find() ? Long.parseLong(m.group(1)) : -1;
    }

    /**
     * Build the URL-encoded form body for the admin product create/edit endpoint.
     *
     * Fields mirror the browser POST observed via DevTools when saving a product
     * in the Prestashop 8 back-office. Only fields required for a working,
     * purchasable product are included.
     *
     * @param name      product name (set for language ID 1 = English)
     * @param price     product price (tax excluded)
     * @param quantity  initial stock quantity
     * @param formToken value of the {@code product[_token]} CSRF hidden field
     * @return URL-encoded form body string
     */
    private String buildProductFormBody(String name, double price, int quantity, String formToken) {
        return "product[name][1]="         + URLEncoder.encode(name, StandardCharsets.UTF_8)
                + "&product[type]=standard"
                + "&product[active]=1"
                + "&product[price]="        + String.format("%.6f", price)
                + "&product[unity]="
                + "&product[unit_price_ratio]=0.000000"
                + "&product[minimal_quantity]=1"
                + "&product[low_stock_threshold]=0"
                + "&product[low_stock_alert]=0"
                + "&product[visibility]=both"
                + "&product[condition]=new"
                + "&product[show_price]=1"
                + "&product[on_sale]=0"
                + "&product[online_only]=0"
                + "&product[quantity]="     + quantity
                + "&product[out_of_stock]=0"
                + "&product[pack_stock_type]=3"
                + "&product[_token]="       + URLEncoder.encode(formToken, StandardCharsets.UTF_8)
                + "&save=1";
    }

    // =========================================================================
    // REST API helper
    // =========================================================================

    /**
     * Helper method to make HTTP requests with API authentication (internal use).
     *
     * Prestashop API uses query parameter authentication (ws_key) rather than header authentication.
     * The ws_key should be included in the endpoint URL by the caller.
     *
     * @param method HTTP method (GET, POST, DELETE, etc.)
     * @param endpoint API endpoint path (e.g., "/customers?ws_key=...")
     * @param body request body for POST/PUT requests
     * @return HTTP response as string
     */
    private String makeRequest(String method, String endpoint, String body) throws Exception {
        String url = baseUrl + endpoint;
        logger.debug("Making {} request to: {}", method, url);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json");

        // Note: Prestashop API uses ws_key query parameter (included in endpoint)
        // rather than Authorization header. No header auth needed.

        // Set method and body based on HTTP verb
        switch (method.toUpperCase()) {
            case "POST":
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
                break;
            case "PUT":
                requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body));
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
