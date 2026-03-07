# TC-017: Add Product in Alternative Currency to Cart

**ID:** TC-017
**Category:** ORDER
**Type:** positive
**Priority:** medium
**Tags:** @cart @currency

---

## Feature

Cart Total in Alternative Currency

## User Story

As a customer, I want to switch the store currency and see my cart total recalculated in the selected currency.

## Background

EUR and GBP are both active. Exchange rate EUR/GBP is configured in the admin panel.

## Scenario

**Given** the store default currency is GBP
**And** the customer switches the currency to EUR
**When** the customer adds a product priced "£50.00 GBP" to the cart
**Then** the cart total is displayed in EUR
**And** the EUR total matches the GBP price converted at the configured exchange rate

---

## Acceptance Criteria

- [ ] Currency switch is reflected in the cart immediately
- [ ] Converted amount matches the configured exchange rate
- [ ] Currency symbol updates throughout the page
