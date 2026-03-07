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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API client for Prestashop product management via HTTP form-based approach.
 * Uses admin login to create products via the admin panel's form endpoints.
 * Deletes products via Webservice API (simpler, already works).
 */
public class PrestashopProductApiClient {
    private static final Logger logger = LoggerFactory.getLogger(PrestashopProductApiClient.class);

    private final String adminBaseUrl;
    private final String adminEmail;
    private final String adminPassword;
    private final String wsApiBaseUrl;
    private final String apiKey;
    private final HttpClient httpClient;

    public PrestashopProductApiClient() {
        this.adminBaseUrl = ConfigLoader.getProperty("admin.base.url", "http://145.239.29.235/admin_hackathon");
        this.adminEmail = ConfigLoader.getProperty("admin.email", "admin@local.dev");
        this.adminPassword = ConfigLoader.getProperty("admin.password", "hackathon-sii-2026");
        this.wsApiBaseUrl = ConfigLoader.getProperty("api.base.url", "http://145.239.29.235/api");
        this.apiKey = ConfigLoader.getProperty("api.key", "");

        // Create HTTP client with cookie manager for session persistence
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.httpClient = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        logger.info("Prestashop Product API client initialized with admin URL: {}", adminBaseUrl);
    }

    /**
     * Create a new product via HTTP form-based admin interface.
     *
     * @param name product name
     * @param price product price
     * @return product ID if successful, -1 if failed
     */
    public long createProduct(String name, double price) {
        logger.info("Creating product: {} with price: {}", name, price);
        try {
            // Step 1: Admin login (get session cookies)
            if (!adminLogin()) {
                logger.error("Failed to authenticate as admin");
                return -1;
            }

            // Step 2: Get new product ID by accessing create page (redirects to edit page with new ID)
            long productId = createEmptyProduct();
            if (productId == -1) {
                logger.error("Failed to create empty product");
                return -1;
            }

            // Step 3: Extract CSRF token from product edit page
            String csrfToken = extractCsrfToken(productId);
            if (csrfToken == null || csrfToken.isEmpty()) {
                logger.error("Failed to extract CSRF token for product {}", productId);
                return -1;
            }

            // Step 4: POST form data to update product with details
            if (!updateProductForm(productId, name, price, csrfToken)) {
                logger.error("Failed to update product form for ID {}", productId);
                return -1;
            }

            logger.info("Product created successfully with ID {} for name: {}", productId, name);
            return productId;
        } catch (Exception e) {
            logger.error("Failed to create product", e);
            return -1;
        }
    }

    /**
     * Delete a product via Webservice API.
     *
     * @param productId product ID to delete
     * @return true if successful
     */
    public boolean deleteProduct(long productId) {
        logger.info("Deleting product: {}", productId);
        try {
            String authHeader = "Basic " + Base64.getEncoder()
                    .encodeToString((apiKey + ":").getBytes(StandardCharsets.UTF_8));
            String url = wsApiBaseUrl + "/products/" + productId;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Authorization", authHeader)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                logger.info("Product {} deleted successfully", productId);
                return true;
            } else {
                logger.error("Failed to delete product {} (HTTP {}): {}", productId, response.statusCode(), response.body());
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to delete product: {}", productId, e);
            return false;
        }
    }

    /**
     * Admin login via HTTP POST to get session cookies.
     */
    private boolean adminLogin() throws Exception {
        logger.debug("Authenticating as admin: {}", adminEmail);
        String loginUrl = adminBaseUrl + "/index.php";
        String loginBody = "email=" + URLEncoder.encode(adminEmail, StandardCharsets.UTF_8)
                + "&passwd=" + URLEncoder.encode(adminPassword, StandardCharsets.UTF_8)
                + "&submitLogin=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(loginUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(loginBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            logger.debug("Admin login successful (HTTP {})", response.statusCode());
            return true;
        } else {
            logger.error("Admin login failed (HTTP {})", response.statusCode());
            return false;
        }
    }

    /**
     * Create an empty product via GET to /products-v2/create.
     * The admin panel redirects to the edit page with the new product ID.
     */
    private long createEmptyProduct() throws Exception {
        logger.debug("Creating empty product via /products-v2/create");
        String createUrl = adminBaseUrl + "/index.php/sell/catalog/products-v2/create";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(createUrl))
                .GET()
                .build();

        // Use a custom handler to capture the final URL after redirects
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Extract product ID from the final URL: /products-v2/{ID}/edit
        Pattern idPattern = Pattern.compile("/products-v2/(\\d+)/edit");
        Matcher matcher = idPattern.matcher(response.uri().toString());

        if (matcher.find()) {
            long productId = Long.parseLong(matcher.group(1));
            logger.debug("Empty product created with ID: {}", productId);
            return productId;
        } else {
            logger.error("Could not extract product ID from redirect URL: {}", response.uri());
            return -1;
        }
    }

    /**
     * Extract CSRF token from product edit page HTML.
     */
    private String extractCsrfToken(long productId) throws Exception {
        logger.debug("Extracting CSRF token for product {}", productId);
        String editUrl = adminBaseUrl + "/index.php/sell/catalog/products-v2/" + productId + "/edit";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(editUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String html = response.body();

        // Look for CSRF token in form: name="product[_token]" value="..."
        Pattern tokenPattern = Pattern.compile("name=\"product\\[_token\\]\"\\s+value=\"([^\"]+)\"");
        Matcher matcher = tokenPattern.matcher(html);

        if (matcher.find()) {
            String token = matcher.group(1);
            logger.debug("CSRF token extracted: {}", token.substring(0, Math.min(20, token.length())) + "...");
            return token;
        } else {
            logger.warn("Could not find CSRF token in product edit page");
            return null;
        }
    }

    /**
     * Update product with form data via POST.
     */
    private boolean updateProductForm(long productId, String name, double price, String csrfToken) throws Exception {
        logger.debug("Updating product form for ID {} with name: {}", productId, name);
        String editUrl = adminBaseUrl + "/index.php/sell/catalog/products-v2/" + productId + "/edit";

        // Build form data with required fields
        String formData = buildProductFormData(name, price, csrfToken);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(editUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            logger.debug("Product form update successful (HTTP {})", response.statusCode());
            return true;
        } else {
            logger.error("Product form update failed (HTTP {}): {}", response.statusCode(), response.body());
            return false;
        }
    }

    /**
     * Build form data for product creation.
     * Follows Prestashop admin panel form structure with nested arrays.
     */
    private String buildProductFormData(String name, double price, String csrfToken) throws Exception {
        StringBuilder data = new StringBuilder();

        // Product header: name, type, active status
        addFormField(data, "product[header][name][1]", name);
        addFormField(data, "product[header][type]", "standard");
        addFormField(data, "product[header][active]", "1");

        // Product description
        addFormField(data, "product[description][description_short][1]", "<p>" + name + "</p>");
        addFormField(data, "product[description][categories][product_categories][0][id]", "2");
        addFormField(data, "product[description][categories][product_categories][0][name]", "Home");
        addFormField(data, "product[description][categories][product_categories][0][display_name]", "Home");
        addFormField(data, "product[description][categories][default_category_id]", "2");

        // Stock management
        addFormField(data, "product[stock][quantities][delta_quantity][initial_quantity]", "100");
        addFormField(data, "product[stock][quantities][delta_quantity][quantity]", "100");
        addFormField(data, "product[stock][quantities][delta_quantity][delta]", "100");
        addFormField(data, "product[stock][quantities][minimal_quantity]", "1");

        // Pricing
        addFormField(data, "product[pricing][retail_price][price_tax_excluded]", String.valueOf(price));
        addFormField(data, "product[pricing][retail_price][tax_rules_group_id]", "1");

        // Product options
        addFormField(data, "product[options][visibility][visibility]", "both");
        addFormField(data, "product[options][visibility][available_for_order]", "1");

        // Footer: save button and CSRF token
        addFormField(data, "product[footer][save]", "");
        addFormField(data, "product[_token]", csrfToken);

        return data.toString();
    }

    /**
     * Helper to add URL-encoded form field.
     */
    private void addFormField(StringBuilder data, String name, String value) throws Exception {
        if (data.length() > 0) {
            data.append("&");
        }
        data.append(URLEncoder.encode(name, StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
    }
}
