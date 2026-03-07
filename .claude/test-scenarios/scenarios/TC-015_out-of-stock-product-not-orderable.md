# TC-015: Out-of-Stock Product Not Orderable When Blocked

**ID:** TC-015
**Category:** BASIC
**Type:** negative
**Priority:** medium
**Tags:** @admin @product @stock

---

## Feature

Out-of-Stock Product – Ordering Blocked

## User Story

As a shop admin, I want the "Add to cart" button to be disabled when stock = 0 and ordering is blocked, so customers cannot buy unavailable items.

## Background

A product exists with quantity = 0 and "allow orders when out of stock" = disabled.

## Scenario

**Given** the admin has created a product with quantity "0" and ordering when out of stock "disabled"
**When** a customer opens the product detail page
**Then** the "Add to cart" button is absent or disabled
**And** an out-of-stock message is displayed on the page

---

## Acceptance Criteria

- [ ] "Add to cart" button is disabled or hidden
- [ ] An out-of-stock indicator is shown to the customer
- [ ] The product cannot be added to the cart by any interaction
- [ ] Product is deleted via API after the test
