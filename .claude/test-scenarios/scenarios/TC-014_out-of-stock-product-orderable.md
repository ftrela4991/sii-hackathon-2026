# TC-014: Out-of-Stock Product Orderable When Allowed

**ID:** TC-014
**Category:** BASIC
**Type:** positive
**Priority:** medium
**Tags:** @admin @product @stock

---

## Feature

Out-of-Stock Product – Ordering Allowed

## User Story

As a shop admin, I want customers to be able to order a product even when its stock is 0, when this setting is enabled.

## Background

A product exists with quantity = 0 and "allow orders when out of stock" = enabled.

## Scenario

**Given** the admin has created a product with quantity "0" and ordering when out of stock "enabled"
**When** a customer opens the product detail page
**Then** the "Add to cart" button is visible and active
**And** the customer can add the product to the cart
**And** the customer can complete the checkout successfully

---

## Acceptance Criteria

- [ ] "Add to cart" button is active despite zero stock
- [ ] Product can be added to the cart
- [ ] Checkout can be completed
- [ ] Product is deleted via API after the test
