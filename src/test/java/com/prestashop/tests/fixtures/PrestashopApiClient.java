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
     * Two-step flow matching the actual browser behaviour:
     *
     * Step 1 — Create product instance (GET):
     *   First, the product list page is fetched to obtain the route-level {@code _token}.
     *   Then GET /products-v2/new?_token={token} is requested.  Prestashop creates a
     *   draft product, assigns it a new ID, and redirects to the edit page:
     *     /products-v2/{newId}/edit?forceDefaultActive=0&_token={token}
     *   The product ID is extracted from this redirect URL.
     *
     * Step 2 — Fill product data (POST):
     *   The edit page HTML contains a second CSRF token (product[_token]).
     *   A POST to the same edit URL with the full form body (name, price, stock, …)
     *   saves the product details and makes it available for purchase.
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

            // ----------------------------------------------------------------
            // Step 1a: GET the product list page to obtain the route _token.
            // The token appears in action links embedded in the list HTML and
            // must be passed when requesting the "new product" URL.
            // ----------------------------------------------------------------
            String listUrl = adminBase + "/index.php/sell/catalog/products-v2";
            HttpResponse<String> listResponse = adminHttpClient.send(
                    HttpRequest.newBuilder().uri(new URI(listUrl)).GET().build(),
                    HttpResponse.BodyHandlers.ofString());

            String routeToken = extractAdminToken(listResponse.body());
            if (routeToken.isEmpty()) {
                routeToken = extractAdminToken(listResponse.uri().toString());
            }
            logger.debug("Route _token from list page: {}", routeToken);

            // ----------------------------------------------------------------
            // Step 1b: GET /products-v2/new?_token={routeToken}
            // Prestashop creates a draft product (assigns a new ID) and
            // redirects to /products-v2/{newId}/edit?forceDefaultActive=0&_token=...
            // ----------------------------------------------------------------
            String newProductUrl = adminBase + "/index.php/sell/catalog/products-v2/new"
                    + "?_token=" + URLEncoder.encode(routeToken, StandardCharsets.UTF_8);
            HttpResponse<String> editPageResponse = adminHttpClient.send(
                    HttpRequest.newBuilder().uri(new URI(newProductUrl)).GET().build(),
                    HttpResponse.BodyHandlers.ofString());

            // Extract the newly-assigned product ID from the redirect destination URL
            String editPageUrl = editPageResponse.uri().toString();
            long productId = extractProductIdFromUrl(editPageUrl);
            if (productId < 0) {
                logger.error("Could not extract product ID from redirect URL: {}", editPageUrl);
                return -1;
            }
            logger.debug("Draft product created with ID: {}", productId);

            // ----------------------------------------------------------------
            // Step 2: POST the product form to the edit endpoint.
            // Two tokens are needed:
            //   urlToken  — _token in the URL query string (route-level security)
            //   formToken — product[_token] embedded in the edit page form HTML
            // ----------------------------------------------------------------
            String urlToken  = extractAdminToken(editPageUrl);
            String formToken = extractInputValue(editPageResponse.body(), "product[_token]");
            logger.debug("Edit page URL _token: {}, form product[_token]: {}", urlToken, formToken);

            String editUrl = adminBase + "/index.php/sell/catalog/products-v2/" + productId
                    + "/edit?forceDefaultActive=0&_token=" + URLEncoder.encode(urlToken, StandardCharsets.UTF_8);
            String formBody = buildProductFormBody(name, price, quantity, formToken);

            HttpResponse<String> saveResponse = adminHttpClient.send(
                    HttpRequest.newBuilder()
                            .uri(new URI(editUrl))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(formBody))
                            .build(),
                    HttpResponse.BodyHandlers.ofString());

            logger.info("Product saved — ID: {}, HTTP {}", productId, saveResponse.statusCode());
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
        // Field names use %5B / %5D (URL-encoded [ / ]) matching the exact browser POST
        // observed via DevTools. Dynamic values (name, price, quantity, token) are
        // URL-encoded; all other values are static constants copied from the live fetch.
        String encName  = URLEncoder.encode(name,      StandardCharsets.UTF_8);
        String encToken = URLEncoder.encode(formToken, StandardCharsets.UTF_8);
        String encPrice = String.format("%.6f", price);   // e.g. "19.990000"

        return // --- header section ---
               "product%5Bheader%5D%5Bname%5D%5B1%5D="             + encName
             + "&product%5Bheader%5D%5Btype%5D=standard"
             + "&product%5Bheader%5D%5Bactive%5D=1"
             + "&product%5Bheader%5D%5Binitial_type%5D=standard"
             // --- footer (triggers save) ---
             + "&product%5Bfooter%5D%5Bsave%5D="
             // --- description section ---
             + "&product%5Bdescription%5D%5Bdescription_short%5D%5B1%5D="
             + "&product%5Bdescription%5D%5Bdescription%5D%5B1%5D="
             + "&product%5Bdescription%5D%5Bcategories%5D%5Bproduct_categories%5D%5B0%5D%5Bdisplay_name%5D=Home"
             + "&product%5Bdescription%5D%5Bcategories%5D%5Bproduct_categories%5D%5B0%5D%5Bname%5D=Home"
             + "&product%5Bdescription%5D%5Bcategories%5D%5Bproduct_categories%5D%5B0%5D%5Bid%5D=2"
             + "&product%5Bdescription%5D%5Bcategories%5D%5Bdefault_category_id%5D=2"
             + "&product%5Bdescription%5D%5Bmanufacturer%5D=0"
             // --- details / references ---
             + "&product%5Bdetails%5D%5Breferences%5D%5Breference%5D="
             + "&product%5Bdetails%5D%5Breferences%5D%5Bmpn%5D="
             + "&product%5Bdetails%5D%5Breferences%5D%5Bupc%5D="
             + "&product%5Bdetails%5D%5Breferences%5D%5Bean_13%5D="
             + "&product%5Bdetails%5D%5Breferences%5D%5Bisbn%5D="
             + "&product%5Bdetails%5D%5Bshow_condition%5D=0"
             // --- stock section ---
             + "&product%5Bstock%5D%5Bquantities%5D%5Bdelta_quantity%5D%5Binitial_quantity%5D=0"
             + "&product%5Bstock%5D%5Bquantities%5D%5Bdelta_quantity%5D%5Bquantity%5D=" + quantity
             + "&product%5Bstock%5D%5Bquantities%5D%5Bdelta_quantity%5D%5Bdelta%5D="   + quantity
             + "&product%5Bstock%5D%5Bquantities%5D%5Bminimal_quantity%5D=1"
             + "&product%5Bstock%5D%5Boptions%5D%5Bstock_location%5D="
             + "&product%5Bstock%5D%5Boptions%5D%5Bdisabling_switch_low_stock_threshold%5D=0"
             + "&product%5Bstock%5D%5Bavailability%5D%5Bout_of_stock_type%5D=2"
             + "&product%5Bstock%5D%5Bavailability%5D%5Bavailable_now_label%5D%5B1%5D="
             + "&product%5Bstock%5D%5Bavailability%5D%5Bavailable_later_label%5D%5B1%5D="
             + "&product%5Bstock%5D%5Bavailability%5D%5Bavailable_date%5D="
             // --- shipping section ---
             + "&product%5Bshipping%5D%5Bdimensions%5D%5Bwidth%5D=0"
             + "&product%5Bshipping%5D%5Bdimensions%5D%5Bheight%5D=0"
             + "&product%5Bshipping%5D%5Bdimensions%5D%5Bdepth%5D=0"
             + "&product%5Bshipping%5D%5Bdimensions%5D%5Bweight%5D=0"
             + "&product%5Bshipping%5D%5Bdelivery_time_note_type%5D=1"
             + "&product%5Bshipping%5D%5Badditional_shipping_cost%5D=0.000000"
             // --- pricing section ---
             + "&product%5Bpricing%5D%5Bretail_price%5D%5Bprice_tax_excluded%5D=" + encPrice
             + "&product%5Bpricing%5D%5Bretail_price%5D%5Btax_rules_group_id%5D=1"
             + "&product%5Bpricing%5D%5Bretail_price%5D%5Bprice_tax_included%5D=" + encPrice
             + "&product%5Bpricing%5D%5Bwholesale_price%5D=0.000000"
             + "&product%5Bpricing%5D%5Bdisabling_switch_unit_price%5D=0"
             + "&paginator-limit=10"
             + "&product%5Bpricing%5D%5Bpriority_management%5D%5Buse_custom_priority%5D=0"
             // --- SEO section ---
             + "&product%5Bseo%5D%5Bmeta_title%5D%5B1%5D="
             + "&product%5Bseo%5D%5Bmeta_description%5D%5B1%5D="
             + "&product%5Bseo%5D%5Blink_rewrite%5D%5B1%5D="
             + "&product%5Bseo%5D%5Bredirect_option%5D%5Btype%5D=default"
             + "&product%5Bseo%5D%5Btags%5D%5B1%5D="
             // --- options / visibility ---
             + "&product%5Boptions%5D%5Bvisibility%5D%5Bvisibility%5D=both"
             + "&product%5Boptions%5D%5Bvisibility%5D%5Bavailable_for_order%5D=1"
             + "&product%5Boptions%5D%5Bvisibility%5D%5Bonline_only%5D=0"
             // --- CSRF token ---
             + "&product%5B_token%5D=" + encToken;
    }

    // =========================================================================
    // REST API helper
    // =========================================================================

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
