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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API client for Prestashop customer management via XML-based Webservice API.
 * Handles customer creation, deletion, and retrieval.
 */
public class PrestashopCustomerApiClient {
    private static final Logger logger = LoggerFactory.getLogger(PrestashopCustomerApiClient.class);

    private final String baseUrl;
    private final String apiKey;
    private final HttpClient httpClient;

    public PrestashopCustomerApiClient() {
        this.baseUrl = ConfigLoader.getProperty("api.base.url", "http://145.239.29.235/api");
        this.apiKey = ConfigLoader.getProperty("api.key", "");
        this.httpClient = HttpClient.newHttpClient();

        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.warn("API key not configured in config.properties");
        }

        logger.info("Prestashop Customer API client initialized with base URL: {}", baseUrl);
    }

    /**
     * Create a new customer via XML Webservice API.
     *
     * @param email customer email address
     * @param password customer password
     * @return customer ID if successful, -1 if failed
     */
    public long createCustomer(String email, String password) {
        logger.info("Creating customer with email: {}", email);
        try {
            String firstName = "Test";
            String lastName = "Customer";
            String idGender = "1";

            String xmlBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<prestashop>" +
                    "<customer>" +
                    "<id_gender>" + idGender + "</id_gender>" +
                    "<firstname>" + firstName + "</firstname>" +
                    "<lastname>" + lastName + "</lastname>" +
                    "<email>" + email + "</email>" +
                    "<passwd>" + password + "</passwd>" +
                    "<id_default_group>3</id_default_group>" +
                    "<active>1</active>" +
                    "</customer>" +
                    "</prestashop>";

            String endpoint = "/customers?ws_key=" + apiKey + "&output_format=JSON";
            String response = makeRequest("POST", endpoint, xmlBody, "application/xml");

            if (response == null || response.isEmpty()) {
                logger.warn("No response from API when creating customer with email: {}", email);
                return -1;
            }

            logger.debug("Customer creation API response: {}", response);

            Pattern idPattern = Pattern.compile("\"id\"\\s*:\\s*[\"]?(\\d+)[\"]?");
            Matcher matcher = idPattern.matcher(response);

            if (matcher.find()) {
                long customerId = Long.parseLong(matcher.group(1));
                logger.info("Customer created successfully with ID {} for email: {}", customerId, email);
                return customerId;
            } else {
                logger.warn("Could not extract customer ID from API response: {}", response);
                return -1;
            }
        } catch (Exception e) {
            logger.error("Failed to create customer", e);
            return -1;
        }
    }

    /**
     * Find a customer ID by email address.
     *
     * @param email customer email address
     * @return Optional containing customer ID if found
     */
    public Optional<Long> findCustomerIdByEmail(String email) {
        logger.info("Finding customer ID for email: {}", email);
        try {
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            String endpoint = "/customers?search=" + encodedEmail + "&ws_key=" + apiKey + "&output_format=JSON";
            String response = makeRequest("GET", endpoint, null, "application/xml");

            if (response == null || response.isEmpty()) {
                logger.warn("No response from API when searching for customer with email: {}", email);
                return Optional.empty();
            }

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
     * Delete a customer via API.
     *
     * @param customerId customer ID to delete
     * @return true if successful
     */
    public boolean deleteCustomer(long customerId) {
        logger.info("Deleting customer: {}", customerId);
        try {
            String endpoint = "/customers/" + customerId + "?ws_key=" + apiKey;
            makeRequest("DELETE", endpoint, null, "application/xml");
            logger.info("Customer {} deleted successfully", customerId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to delete customer: {}", customerId, e);
            return false;
        }
    }

    /**
     * Delete customer by email address.
     *
     * @param email customer email address
     * @return true if successful
     */
    public boolean deleteCustomerByEmail(String email) {
        logger.info("Deleting customer with email: {}", email);
        try {
            Optional<Long> customerId = findCustomerIdByEmail(email);
            if (customerId.isPresent()) {
                return deleteCustomer(customerId.get());
            } else {
                logger.warn("Customer with email {} not found", email);
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to delete customer by email: {}", email, e);
            return false;
        }
    }

    /**
     * Make HTTP request with custom content type.
     */
    private String makeRequest(String method, String endpoint, String body, String contentType) throws Exception {
        String url = baseUrl + endpoint;
        logger.debug("Making {} request to: {}", method, url);

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
}
