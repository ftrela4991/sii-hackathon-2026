# TC-006: Create Standard Product (No Variants) Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement a test that creates a standard product via admin panel form, verifies it on storefront via search, and deletes it via form-based UI interaction.

**Architecture:**
- Create three admin page objects for product workflow (creation, list verification, deletion)
- Reuse existing LoginPage for admin authentication (same login page)
- Reuse ProductDetailPage to verify product on storefront (search + view)
- Test uses timestamp-based product naming for isolation
- Form-based deletion documents the POST structure for future API implementation

**Tech Stack:**
- Selenium WebDriver with PageObjectModel
- JUnit 5 for test execution
- Prestashop form-based admin API (documented in PRODUCT_REQUEST_STRUCTURE.md)
- CSRF token extraction from admin pages

---

## Task 1: Create AdminProductCreationPage

**Files:**
- Create: `src/test/java/com/prestashop/tests/pages/AdminProductCreationPage.java`

**Overview:** Page object for the product creation form in admin panel. Handles navigation to creation page, filling form fields (name, price, stock), and saving.

**Step 1: Write the test that exercises the page object**

Create a minimal test file to guide development:

```bash
# We'll write this after creating the page object in Step 3
# For now, focus on creating the page object itself
```

**Step 2: Create AdminProductCreationPage**

```java
package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import com.prestashop.tests.utils.ConfigLoader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the Prestashop Admin Product Creation page.
 *
 * Handles:
 * - Navigation to product creation page
 * - Filling product form (name, price, stock)
 * - Saving product
 * - Extracting CSRF token for form submission
 */
public class AdminProductCreationPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AdminProductCreationPage.class);

    // Form field locators
    private static final By PRODUCT_NAME_INPUT = By.name("product[header][name][1]");
    private static final By RETAIL_PRICE_INPUT = By.name("product[pricing][retail_price][price_tax_excluded]");
    private static final By QUANTITY_INPUT = By.name("product[stock][quantities][delta_quantity][quantity]");
    private static final By CSRF_TOKEN = By.name("product[_token]");
    private static final By SAVE_BUTTON = By.xpath("//button[contains(text(), 'Save')]");
    private static final By PRODUCT_TYPE_SELECT = By.name("product[header][type]");

    // Page verification
    private static final By PRODUCT_HEADER = By.xpath("//h1[contains(text(), 'Product')]");

    public AdminProductCreationPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to admin product creation page
     */
    public void navigateToProductCreation() {
        String adminUrl = ConfigLoader.getProperty("admin.base.url");
        // Navigate to new product page (ID 0 is convention for creating new product)
        navigateTo(adminUrl + "/index.php/sell/catalog/products-v2/0/edit");
        logger.info("Navigated to product creation page");
        assertLoaded();
    }

    /**
     * Fill in the product name field
     */
    public void setProductName(String name) {
        logger.info("Setting product name: {}", name);
        sendKeys(PRODUCT_NAME_INPUT, name);
    }

    /**
     * Fill in the retail price field
     */
    public void setRetailPrice(String price) {
        logger.info("Setting retail price: {}", price);
        sendKeys(RETAIL_PRICE_INPUT, price);
    }

    /**
     * Fill in the quantity/stock field
     */
    public void setQuantity(String quantity) {
        logger.info("Setting quantity: {}", quantity);
        sendKeys(QUANTITY_INPUT, quantity);
    }

    /**
     * Set product type (standard, virtual, pack)
     */
    public void setProductType(String type) {
        logger.info("Setting product type: {}", type);
        sendKeys(PRODUCT_TYPE_SELECT, type);
    }

    /**
     * Get the CSRF token from the form (needed for API deletion later)
     */
    public String getCsrfToken() {
        String token = driver.findElement(CSRF_TOKEN).getAttribute("value");
        logger.info("Extracted CSRF token: {}", token.substring(0, 10) + "...");
        return token;
    }

    /**
     * Save the product by clicking Save button
     */
    public void saveProduct() {
        logger.info("Clicking Save button");
        click(SAVE_BUTTON);
        // Wait for save to complete - page will redirect
        waitForElement(By.xpath("//div[contains(@class, 'success')]"));
        logger.info("Product saved successfully");
    }

    /**
     * Fill and save a complete product in one method
     */
    public void createProduct(String name, String price, String quantity) {
        logger.info("Creating product: name={}, price={}, quantity={}", name, price, quantity);
        setProductType("standard");
        setProductName(name);
        setRetailPrice(price);
        setQuantity(quantity);
        saveProduct();
        logger.info("Product creation completed");
    }

    @Override
    public void assertLoaded() {
        logger.info("Verifying Product Creation page is loaded");
        waitForElement(PRODUCT_NAME_INPUT);
        logger.info("Product Creation page loaded successfully");
    }
}
```

**Step 3: Commit**

```bash
git add src/test/java/com/prestashop/tests/pages/AdminProductCreationPage.java
git commit -m "feat: add AdminProductCreationPage for product creation form"
```

---

## Task 2: Create AdminProductListPage

**Files:**
- Create: `src/test/java/com/prestashop/tests/pages/AdminProductListPage.java`

**Overview:** Page object for the admin product list view. Verifies that created products appear in the list.

**Step 1: Create AdminProductListPage**

```java
package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import com.prestashop.tests.utils.ConfigLoader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Page Object for the Prestashop Admin Product List page.
 *
 * Handles:
 * - Navigation to product list
 * - Searching for products by name
 * - Verifying product exists in list
 * - Clicking on product to edit
 */
public class AdminProductListPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AdminProductListPage.class);

    // Locators
    private static final By SEARCH_INPUT = By.xpath("//input[@placeholder='Search...' or @placeholder='Search']");
    private static final By PRODUCT_TABLE = By.xpath("//table[@class='table']");
    private static final By PRODUCT_ROWS = By.xpath("//table//tbody//tr");
    private static final By PRODUCT_NAME_CELL = By.xpath(".//td[2]"); // Usually 2nd column
    private static final By PAGE_HEADER = By.xpath("//h1[contains(text(), 'Products')]");

    public AdminProductListPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to admin product list page
     */
    public void navigateToProductList() {
        String adminUrl = ConfigLoader.getProperty("admin.base.url");
        navigateTo(adminUrl + "/index.php/sell/catalog/products");
        logger.info("Navigated to product list page");
        assertLoaded();
    }

    /**
     * Search for a product by name
     */
    public void searchProduct(String productName) {
        logger.info("Searching for product: {}", productName);
        sendKeys(SEARCH_INPUT, productName);
        // Wait for results to appear
        Thread.sleep(1000); // Brief wait for search to complete
        logger.info("Search executed");
    }

    /**
     * Verify that a product with the given name exists in the list
     */
    public boolean verifyProductExists(String productName) {
        logger.info("Verifying product exists: {}", productName);
        try {
            searchProduct(productName);
            List<WebElement> rows = driver.findElements(PRODUCT_ROWS);

            for (WebElement row : rows) {
                String cellText = row.findElement(PRODUCT_NAME_CELL).getText();
                if (cellText.contains(productName)) {
                    logger.info("✓ Product found in list: {}", productName);
                    return true;
                }
            }
            logger.warn("✗ Product not found in list: {}", productName);
            return false;
        } catch (Exception e) {
            logger.error("Error verifying product: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Click on a product in the list to edit it
     */
    public void clickProductToEdit(String productName) {
        logger.info("Clicking product to edit: {}", productName);
        searchProduct(productName);
        List<WebElement> rows = driver.findElements(PRODUCT_ROWS);

        for (WebElement row : rows) {
            String cellText = row.findElement(PRODUCT_NAME_CELL).getText();
            if (cellText.contains(productName)) {
                row.click();
                logger.info("Clicked product: {}", productName);
                return;
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    @Override
    public void assertLoaded() {
        logger.info("Verifying Product List page is loaded");
        waitForElement(PAGE_HEADER);
        logger.info("Product List page loaded successfully");
    }
}
```

**Step 2: Commit**

```bash
git add src/test/java/com/prestashop/tests/pages/AdminProductListPage.java
git commit -m "feat: add AdminProductListPage for product list verification"
```

---

## Task 3: Create AdminProductDeletePage

**Files:**
- Create: `src/test/java/com/prestashop/tests/pages/AdminProductDeletePage.java`

**Overview:** Page object for deleting products via the admin form. This tests the UI deletion flow and documents the form structure for future API implementation.

**Step 1: Create AdminProductDeletePage**

```java
package com.prestashop.tests.pages;

import com.prestashop.tests.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for the Prestashop Admin Product Deletion.
 *
 * Handles:
 * - Clicking delete button on product edit page
 * - Confirming deletion
 * - Documenting form structure for future API implementation
 */
public class AdminProductDeletePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AdminProductDeletePage.class);

    // Locators for delete functionality
    private static final By DELETE_BUTTON = By.xpath("//button[contains(text(), 'Delete')]");
    private static final By DELETE_BUTTON_ALT = By.xpath("//a[contains(text(), 'Delete')]");
    private static final By CONFIRM_DELETE_YES = By.xpath("//button[contains(text(), 'Yes')]");
    private static final By CONFIRM_DELETE_YES_ALT = By.xpath("//button[@type='submit' and contains(text(), 'Yes')]");
    private static final By MODAL_DIALOG = By.xpath("//div[contains(@class, 'modal')]");
    private static final By PRODUCT_FORM = By.xpath("//form[@name='product_form']");

    public AdminProductDeletePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Click the Delete button on product edit page
     */
    public void clickDeleteButton() {
        logger.info("Clicking Delete button");
        try {
            click(DELETE_BUTTON);
        } catch (Exception e) {
            logger.debug("DELETE_BUTTON not found, trying alternative");
            click(DELETE_BUTTON_ALT);
        }
        logger.info("Delete button clicked, waiting for confirmation dialog");
        // Wait for confirmation dialog to appear
        waitForElement(MODAL_DIALOG);
    }

    /**
     * Confirm the deletion by clicking "Yes" in the confirmation dialog
     */
    public void confirmDeletion() {
        logger.info("Confirming deletion by clicking Yes");
        try {
            click(CONFIRM_DELETE_YES);
        } catch (Exception e) {
            logger.debug("CONFIRM_DELETE_YES not found, trying alternative");
            click(CONFIRM_DELETE_YES_ALT);
        }
        logger.info("Deletion confirmed, waiting for redirect to product list");
        // Wait for redirect - should go back to product list
        Thread.sleep(2000);
    }

    /**
     * Delete a product by clicking delete and confirming
     */
    public void deleteProduct() {
        logger.info("Starting product deletion process");
        clickDeleteButton();
        confirmDeletion();
        logger.info("Product deletion completed");
    }

    @Override
    public void assertLoaded() {
        logger.info("Verifying Product Edit page is loaded (with delete option)");
        waitForElement(PRODUCT_FORM);
        logger.info("Product Edit page loaded successfully");
    }
}
```

**Step 2: Commit**

```bash
git add src/test/java/com/prestashop/tests/pages/AdminProductDeletePage.java
git commit -m "feat: add AdminProductDeletePage for form-based product deletion"
```

---

## Task 4: Create HomePage with Search Functionality

**Files:**
- Modify: `src/test/java/com/prestashop/tests/pages/HomePage.java` (add search method if not present)

**Overview:** Ensure HomePage has search capability to find products by name.

**Step 1: Read existing HomePage**

```bash
# Check if HomePage exists and has search
cat src/test/java/com/prestashop/tests/pages/HomePage.java
```

If search doesn't exist, add this method:

```java
private static final By SEARCH_INPUT = By.name("s");

/**
 * Search for a product by name
 */
public void searchProduct(String productName) {
    logger.info("Searching for product: {}", productName);
    sendKeys(SEARCH_INPUT, productName);
    // Press Enter or click search button
    driver.findElement(SEARCH_INPUT).submit();
    logger.info("Search submitted for: {}", productName);
}

/**
 * Navigate to home page
 */
public void navigateToHome() {
    String baseUrl = ConfigLoader.getProperty("base.url");
    navigateTo(baseUrl + "/");
    assertLoaded();
}
```

**Step 2: Commit if modified**

```bash
git add src/test/java/com/prestashop/tests/pages/HomePage.java
git commit -m "feat: add search functionality to HomePage"
```

---

## Task 5: Create ProductCreationTest (TC-006)

**Files:**
- Create: `src/test/java/com/prestashop/tests/tests/ProductCreationTest.java`

**Overview:** Main test class for TC-006 that orchestrates the complete workflow.

**Step 1: Create the test class**

```java
package com.prestashop.tests.tests;

import com.prestashop.tests.base.BaseTest;
import com.prestashop.tests.pages.*;
import com.prestashop.tests.utils.ConfigLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for product creation scenarios (TC-006).
 * Tests creating a standard product via admin form and verifying it on storefront.
 *
 * Workflow:
 * 1. Login to admin panel
 * 2. Create product with form (name: timestamp, price: 29.99, stock: 50)
 * 3. Verify in admin product list
 * 4. Verify on storefront (search + view detail)
 * 5. Delete via form
 */
@DisplayName("Product Creation Tests (TC-006)")
public class ProductCreationTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(ProductCreationTest.class);

    // Test data
    private String testProductName;
    private static final String PRODUCT_PRICE = "29.99";
    private static final String PRODUCT_QUANTITY = "50";
    private static final String EXPECTED_PRICE_DISPLAY = "£29.99";

    /**
     * Setup: Generate unique product name with timestamp
     */
    public void setupTestData() {
        testProductName = "Test Simple Product " + System.currentTimeMillis();
        logger.info("Test product name: {}", testProductName);
    }

    /**
     * TC-006: Create Standard Product (No Variants)
     *
     * Scenario:
     * - Admin creates product with name (timestamp), price 29.99, stock 50
     * - Product appears in admin list
     * - Product visible on storefront with correct price
     * - Product can be deleted
     *
     * Acceptance Criteria:
     * ✓ Product is saved without errors
     * ✓ Product appears in storefront with correct price
     * ✓ No variants or combination selectors shown
     * ✓ Product is deleted via UI (learning for API)
     */
    @Test
    @DisplayName("TC-006: Create standard product and verify on storefront")
    public void testCreateStandardProduct() {
        setupTestData();
        logger.info("Starting TC-006: Create Standard Product");

        // Step 1: Login to admin panel
        logger.info("Step 1: Logging in to admin panel");
        String adminUrl = ConfigLoader.getProperty("admin.base.url");
        navigateTo(adminUrl + "/");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.assertLoaded();

        String adminEmail = ConfigLoader.getProperty("admin.email");
        String adminPassword = ConfigLoader.getProperty("admin.password");
        loginPage.login(adminEmail, adminPassword);

        // Wait for admin dashboard to load
        Thread.sleep(2000);
        logger.info("✓ Admin login completed");

        // Step 2: Navigate to product creation page
        logger.info("Step 2: Navigating to product creation page");
        AdminProductCreationPage productCreation = new AdminProductCreationPage(getDriver());
        productCreation.navigateToProductCreation();
        logger.info("✓ On product creation page");

        // Step 3: Fill and save product form
        logger.info("Step 3: Creating product via form");
        productCreation.createProduct(testProductName, PRODUCT_PRICE, PRODUCT_QUANTITY);
        logger.info("✓ Product created and saved");

        // Step 4: Verify product appears in admin list
        logger.info("Step 4: Verifying product in admin list");
        AdminProductListPage productList = new AdminProductListPage(getDriver());
        productList.navigateToProductList();
        boolean existsInAdminList = productList.verifyProductExists(testProductName);
        assertTrue(existsInAdminList, "Product should appear in admin product list");
        logger.info("✓ Product verified in admin list");

        // Step 5: Verify product on storefront via search
        logger.info("Step 5: Verifying product on storefront");
        navigateTo(ConfigLoader.getProperty("base.url") + "/");
        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToHome();
        homePage.searchProduct(testProductName);

        // Wait for search results and click product
        Thread.sleep(1000);
        ProductDetailPage productDetail = new ProductDetailPage(getDriver());
        productDetail.assertLoaded();

        String displayedName = productDetail.getProductName();
        String displayedPrice = productDetail.getProductPrice();

        assertTrue(displayedName.contains(testProductName),
            "Product name should be visible: " + displayedName);
        assertTrue(displayedPrice.contains("29.99") || displayedPrice.contains("£29.99"),
            "Price should display as £29.99 or 29.99, but found: " + displayedPrice);
        logger.info("✓ Product verified on storefront");
        logger.info("  - Name: {}", displayedName);
        logger.info("  - Price: {}", displayedPrice);

        // Step 6: Delete product via admin form
        logger.info("Step 6: Deleting product via form");
        navigateTo(ConfigLoader.getProperty("admin.base.url") + "/");
        AdminProductListPage productListForDelete = new AdminProductListPage(getDriver());
        productListForDelete.navigateToProductList();
        productListForDelete.clickProductToEdit(testProductName);

        AdminProductDeletePage deleteProduct = new AdminProductDeletePage(getDriver());
        deleteProduct.assertLoaded();
        deleteProduct.deleteProduct();
        logger.info("✓ Product deleted via form");

        // Step 7: Verify product is gone from admin list
        logger.info("Step 7: Verifying product is deleted");
        productListForDelete.navigateToProductList();
        Thread.sleep(1000);
        boolean stillExists = productList.verifyProductExists(testProductName);
        assertTrue(!stillExists, "Product should be deleted from admin list");
        logger.info("✓ Product deletion verified");

        logger.info("TC-006: Test completed successfully");
    }
}
```

**Step 2: Run test to identify missing locators**

```bash
mvn clean test -Dtest=ProductCreationTest#testCreateStandardProduct
```

Expected: Test runs but may fail on specific locators. Document any failures for fixing.

**Step 3: Fix locators based on test output**

Update locators in the page objects based on actual admin panel HTML. Check:
- Form field names (may differ from PRODUCT_REQUEST_STRUCTURE.md)
- Button text (may be "Save Product" not "Save")
- Modal dialog selectors for deletion

**Step 4: Commit**

```bash
git add src/test/java/com/prestashop/tests/tests/ProductCreationTest.java
git commit -m "test: add ProductCreationTest (TC-006) for product creation workflow"
```

---

## Task 6: Handle Failures and Fix Locators

**Files:**
- Modify: `src/test/java/com/prestashop/tests/pages/AdminProductCreationPage.java`
- Modify: `src/test/java/com/prestashop/tests/pages/AdminProductDeletePage.java`
- As needed based on test output

**Step 1: Analyze test failures**

When test fails, look for:
1. Element not found → Wrong locator
2. Timeout waiting for element → Locator not visible
3. Form not submitted → Wrong button selector
4. Price not displaying correctly → Need alternative locator

**Step 2: Use browser DevTools**

Login to admin panel manually and inspect:
- Input field `name` attributes for form fields
- Delete button element (class, id, xpath)
- Confirmation dialog structure

**Step 3: Update locators with alternatives**

Example pattern (try multiple locators):

```java
public void setRetailPrice(String price) {
    try {
        sendKeys(RETAIL_PRICE_INPUT, price);
    } catch (Exception e) {
        logger.debug("Primary selector failed, trying alternatives");
        By ALT_PRICE = By.xpath("//input[contains(@class, 'price')]");
        sendKeys(ALT_PRICE, price);
    }
}
```

**Step 4: Commit fixes**

```bash
git add src/test/java/com/prestashop/tests/pages/
git commit -m "fix: update admin page locators based on test execution"
```

---

## Task 7: Test Full Workflow and Validate

**Files:**
- Test: `src/test/java/com/prestashop/tests/tests/ProductCreationTest.java`

**Step 1: Run test multiple times**

```bash
# Run single test
mvn clean test -Dtest=ProductCreationTest#testCreateStandardProduct

# Run with single thread to debug
mvn test -Dtest=ProductCreationTest -DthreadCount=1

# Check for cleanup: verify product is actually deleted
```

**Step 2: Verify test results**

Expected output:
- ✓ Product created in admin
- ✓ Product appears in admin list
- ✓ Product appears on storefront
- ✓ Price displays correctly (£29.99)
- ✓ No variant selectors shown
- ✓ Product deleted successfully
- ✓ Product gone from admin list

**Step 3: Check for side effects**

- [ ] Previous test products cleaned up properly
- [ ] No leftover products in database (manually verify in admin)
- [ ] Admin session properly closed after test
- [ ] Storefront responsive after product deletion

**Step 4: Commit test validation**

```bash
git add src/test/java/com/prestashop/tests/
git commit -m "test: validate TC-006 full workflow execution"
```

---

## Task 8: Document Deletion Form Structure for API

**Files:**
- Create/Modify: `docs/PRODUCT_DELETION_STRUCTURE.md`

**Overview:** Document the form structure used for product deletion so we can implement API version later.

**Step 1: Create documentation**

During test execution, document:

```markdown
# Product Deletion Form Structure

## Overview
Form-based deletion from admin product edit page.

## HTTP Details
- **Method**: POST
- **Endpoint**: `/admin_hackathon/index.php/sell/catalog/products-v2/{id}/edit`
- **Content-Type**: `application/x-www-form-urlencoded`

## Form Parameters
```
product[header][name][1]={existing_name}
product[...other required fields...]
product[footer][delete]=1  # or similar delete action
product[_token]={csrf_token}
```

## Implementation Notes
- CSRF token must be extracted from product edit page
- All existing product fields may need to be re-submitted
- Modal confirmation in UI (check if required in API)
```

**Step 2: Commit documentation**

```bash
git add docs/PRODUCT_DELETION_STRUCTURE.md
git commit -m "docs: document product deletion form structure"
```

---

## Task 9: Final Verification and Code Review

**Files:**
- Review: All modified/created files

**Step 1: Run full test suite**

```bash
# Run all tests to ensure no regressions
mvn clean test

# Check for any failures in other tests
```

**Step 2: Code review checklist**

- [ ] All page objects extend BasePage
- [ ] No assertions in page objects (except assertLoaded)
- [ ] Test class has clear step comments
- [ ] Assertions in test class, not page objects
- [ ] CSRF token extraction documented
- [ ] Thread-safe (no shared state)
- [ ] Proper cleanup (product deleted after test)
- [ ] Logging at appropriate levels
- [ ] Unique test data (timestamp-based)

**Step 3: Final commit**

```bash
git add -A
git commit -m "feat: implement TC-006 complete workflow with admin page objects"
```

---

## Execution Instructions

### Before Starting
1. Admin credentials configured in `config.properties` ✓
2. Admin URL set to `http://145.239.29.235/admin_hackathon` ✓
3. Ensure admin can access product creation page

### During Implementation
- Run test after each page object is created
- Fix locators based on actual admin panel HTML
- Document any variations from PRODUCT_REQUEST_STRUCTURE.md

### After Completion
- Verify TC-006 runs successfully (creates, verifies, deletes)
- Check no leftover products in database
- All other tests still pass
- Locators documented for future API implementation

---

## Key Implementation Notes

1. **Admin Login Reuse**: LoginPage works for admin (same form, same locators)
2. **CSRF Token**: Extract from form before submission (needed for deletion API later)
3. **Form Structure**: Follow PRODUCT_REQUEST_STRUCTURE.md for field names
4. **Timestamp Naming**: Ensures product uniqueness across test runs
5. **Deletion Learning**: Document form submission to build API method later
6. **Storefront Verification**: Use existing HomePage + ProductDetailPage + search

