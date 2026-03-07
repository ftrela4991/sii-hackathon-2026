package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import com.prestashop.tests.utils.ConfigLoader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminProductCreationPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AdminProductCreationPage.class);

    // Form field locators - using name attributes from Prestashop form
    private static final By PRODUCT_NAME_INPUT = By.name("product[header][name][1]");
    private static final By RETAIL_PRICE_INPUT = By.name("product[pricing][retail_price][price_tax_excluded]");
    private static final By QUANTITY_INPUT = By.name("product[stock][quantities][delta_quantity][quantity]");
    private static final By SAVE_BUTTON = By.xpath(
            "//button[contains(@class,'btn-primary') and (contains(text(),'Save') or contains(text(),'save') or contains(.,\"Save\"))]"
    );
    private static final By SUCCESS_ALERT = By.xpath(
            "//*[contains(@class,'alert-success') or contains(@class,'growl-success')]"
    );

    public AdminProductCreationPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to the new product creation form in the admin panel.
     * Tries the v1 URL first, then falls back to the v2 URL.
     */
    public void navigateToProductCreation() {
        String adminUrl = ConfigLoader.getProperty("admin.base.url");
        String primaryUrl = adminUrl + "/index.php/sell/catalog/products/new";
        String fallbackUrl = adminUrl + "/index.php/sell/catalog/products-v2/new";

        logger.info("Navigating to product creation page: {}", primaryUrl);
        navigateTo(primaryUrl);

        if (!isElementVisible(PRODUCT_NAME_INPUT)) {
            logger.warn("Product name input not found at primary URL, trying fallback: {}", fallbackUrl);
            navigateTo(fallbackUrl);
        }

        assertLoaded();
    }

    /**
     * Set the product name in the name field.
     *
     * @param name the product name
     */
    public void setProductName(String name) {
        logger.info("Setting product name: {}", name);
        sendKeys(PRODUCT_NAME_INPUT, name);
    }

    /**
     * Set the retail price (tax excluded) in the pricing field.
     *
     * @param price the price as a string (e.g. "19.99")
     */
    public void setRetailPrice(String price) {
        logger.info("Setting retail price: {}", price);
        sendKeys(RETAIL_PRICE_INPUT, price);
    }

    /**
     * Set the quantity in the stock delta quantity field.
     *
     * @param quantity the quantity as a string (e.g. "100")
     */
    public void setQuantity(String quantity) {
        logger.info("Setting quantity: {}", quantity);
        sendKeys(QUANTITY_INPUT, quantity);
    }

    /**
     * Click the Save button and wait for a success confirmation.
     */
    public void saveProduct() {
        logger.info("Clicking Save button");
        click(SAVE_BUTTON);
        waitForElement(SUCCESS_ALERT);
        logger.info("Product saved successfully (success alert appeared)");
    }

    /**
     * Convenience method to fill in all required product fields and save.
     *
     * @param name     the product name
     * @param price    the retail price (tax excluded)
     * @param quantity the stock quantity
     */
    public void createProduct(String name, String price, String quantity) {
        logger.info("Creating product: name={}, price={}, quantity={}", name, price, quantity);
        setProductName(name);
        setRetailPrice(price);
        setQuantity(quantity);
        saveProduct();
        logger.info("Product creation completed");
    }

    /**
     * Verify that the product creation page is loaded by checking that the
     * product name input is visible. This is the only assertion allowed here.
     */
    @Override
    public void assertLoaded() {
        logger.info("Verifying Product Creation page is loaded");
        waitForElement(PRODUCT_NAME_INPUT);
        logger.info("Product Creation page loaded successfully");
    }
}
