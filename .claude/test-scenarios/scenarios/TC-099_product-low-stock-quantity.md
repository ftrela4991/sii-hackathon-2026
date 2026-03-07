# TC-099: Product with Low Stock Quantity (1–5 Units)

**ID:** TC-099
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @product @stock @boundary

---

## Feature

Low Stock Product Warning

## User Story

As a shop admin, products with very low stock should display a warning so customers know availability is limited.

## Scenario

**Given** the admin creates a product with quantity "3"
**When** a customer views the product detail page
**Then** a low-stock message or badge is shown (if the low-stock threshold feature is enabled)
**And** the product can still be added to the cart

---

## Acceptance Criteria

- [ ] Low-stock indicator is visible on the product page (when feature enabled)
- [ ] "Add to cart" is still functional
- [ ] Customer can purchase the product successfully
- [ ] Product is deleted via API after the test
