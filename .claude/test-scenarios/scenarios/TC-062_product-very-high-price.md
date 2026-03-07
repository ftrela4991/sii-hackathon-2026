# TC-062: Product with Very High Price (999999 EUR)

**ID:** TC-062
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @product @pricing @boundary

---

## Feature

High-Value Product Display

## User Story

As a shop admin, I want a product priced at 999999 EUR to display correctly without truncation or formatting errors.

## Scenario

**Given** the admin creates a product "Luxury Item <uuid>" with price "999999.00" EUR
**When** a customer views the product detail page
**Then** the price is displayed correctly as "€999,999.00"
**And** the product can be added to the cart
**And** the cart total shows the correct high amount

---

## Acceptance Criteria

- [ ] Price is not truncated or rounded incorrectly
- [ ] Thousands separator is applied correctly
- [ ] Cart total matches the product price
- [ ] Product is deleted via API after the test
