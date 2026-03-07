# TC-068: Set and Verify Exchange Rates

**ID:** TC-068
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @currency

---

## Feature

Exchange Rate Configuration

## User Story

As a shop admin, I want to set a specific exchange rate and verify prices are converted correctly on the storefront.

## Background

EUR and GBP are both active currencies.

## Scenario

**Given** the admin sets the GBP-to-EUR exchange rate to "1.17"
**And** a product is priced at "£100.00 GBP"
**When** a customer switches the store currency to EUR
**Then** the product price is displayed as "€117.00"

---

## Acceptance Criteria

- [ ] Exchange rate is saved in the admin panel
- [ ] Storefront price conversion matches the configured rate
- [ ] Rounding follows the store's configured decimal precision
