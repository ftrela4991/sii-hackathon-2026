# TC-069: Set Currency-Specific Prices for a Product

**ID:** TC-069
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @currency @pricing

---

## Feature

Currency-Specific Product Price Override

## User Story

As a shop admin, I want to set a specific price for a product in EUR that overrides the exchange rate conversion.

## Background

EUR and GBP are both active currencies.

## Scenario

**Given** a product has a base price of "£100.00 GBP"
**When** the admin sets a specific price of "€90.00" for the EUR currency on that product
**And** a customer switches the store to EUR
**Then** the product price displayed is "€90.00"
**And** it is not the exchange-rate-converted value from £100.00

---

## Acceptance Criteria

- [ ] Specific EUR price overrides the automatic conversion
- [ ] GBP price remains unchanged for GBP customers
- [ ] Price displayed in EUR matches the manually set value exactly
