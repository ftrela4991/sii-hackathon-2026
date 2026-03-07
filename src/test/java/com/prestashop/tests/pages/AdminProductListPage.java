package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import com.prestashop.tests.utils.ConfigLoader;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Page Object for the Prestashop admin product list view.
 *
 * Provides UI operations for managing products in the admin catalog:
 * - Navigating to the product list
 * - Searching for a product by name
 * - Verifying a product exists in the results
 * - Clicking a product row to open it for editing
 */
public class AdminProductListPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AdminProductListPage.class);

    private static final By PAGE_HEADER = By.xpath(
            "//h1[contains(text(),'Products') or contains(text(),'Catalog') or contains(text(),'produits') or contains(text(),'Produits')]");
    private static final By PRODUCT_ROWS = By.xpath(
            "//table[contains(@class,'table')]//tbody//tr");
    private static final By SEARCH_INPUT = By.xpath(
            "//div[contains(@class,'table-responsive') or contains(@class,'product-catalog')]//input[@type='text']"
            + " | //input[@name='product[name]']"
            + " | //div[@id='main-div']//input[@type='text']");

    public AdminProductListPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to the admin product catalog list page.
     * Calls assertLoaded() after navigation to confirm the page is ready.
     */
    public void navigateToProductList() {
        String adminUrl = ConfigLoader.getProperty("admin.base.url", "http://145.239.29.235/admin_hackathon");
        String targetUrl = adminUrl + "/index.php/sell/catalog/products";
        logger.info("Navigating to admin product list: {}", targetUrl);
        navigateTo(targetUrl);
        assertLoaded();
    }

    /**
     * Search for a product by name and return whether it appears in the results table.
     *
     * @param productName the product name to search for
     * @return true if the product name is found in any result row, false otherwise
     */
    public boolean verifyProductExists(String productName) {
        try {
            searchProduct(productName);
            List<WebElement> rows = driver.findElements(PRODUCT_ROWS);
            for (WebElement row : rows) {
                if (row.getText().contains(productName)) {
                    logger.info("Product found in list: {}", productName);
                    return true;
                }
            }
            logger.warn("Product not found in list: {}", productName);
            return false;
        } catch (Exception e) {
            logger.error("Error while verifying product '{}': {}", productName, e.getMessage());
            return false;
        }
    }

    /**
     * Search for a product by name and click the edit link for the first matching row.
     *
     * @param productName the product name to find
     * @throws RuntimeException if no matching product row is found after searching
     */
    public void clickProductToEdit(String productName) {
        searchProduct(productName);
        List<WebElement> rows = driver.findElements(PRODUCT_ROWS);
        for (WebElement row : rows) {
            if (row.getText().contains(productName)) {
                logger.info("Found product row for '{}', clicking edit", productName);
                try {
                    row.findElement(By.xpath(".//a[contains(@href,'edit')]")).click();
                } catch (Exception e) {
                    logger.debug("No edit link found in row, clicking row directly for: {}", productName);
                    row.click();
                }
                return;
            }
        }
        throw new RuntimeException("Product not found in list, cannot edit: " + productName);
    }

    /**
     * Verify that the admin product list page has loaded by waiting for the page header.
     */
    @Override
    public void assertLoaded() {
        logger.info("Verifying admin product list page is loaded");
        waitForElement(PAGE_HEADER);
        logger.info("Admin product list page loaded successfully");
    }

    /**
     * Type the product name into the search/filter input and submit the search.
     * Waits for the results table to be visible after the search.
     *
     * @param productName the product name to search for
     */
    private void searchProduct(String productName) {
        logger.info("Searching for product: {}", productName);
        WebElement searchInput = waitForElement(SEARCH_INPUT);
        searchInput.clear();
        searchInput.sendKeys(productName);
        searchInput.sendKeys(Keys.RETURN);

        // Wait for any stale state from the previous results to clear, then wait for rows
        try {
            wait.until(ExpectedConditions.stalenessOf(searchInput));
        } catch (Exception e) {
            // Staleness may not occur on all pages; continue to wait for results
            logger.debug("Search input did not go stale after submit, continuing: {}", e.getMessage());
        }

        waitForElement(PRODUCT_ROWS);
        logger.info("Search results loaded for: {}", productName);
    }
}
