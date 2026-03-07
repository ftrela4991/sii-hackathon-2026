# TC-022: Decrease Product Quantity to Zero Removes the Item

**ID:** TC-022
**Category:** ORDER
**Type:** positive
**Priority:** medium
**Tags:** @cart

---

## Feature

Remove Product by Setting Quantity to Zero

## User Story

As a customer, decreasing a cart item's quantity to 0 should automatically remove it from the cart.

## Background

The cart contains exactly 1 product.

## Scenario

**Given** the cart contains 1 unit of "Hummingbird Printed T-Shirt"
**When** the customer decreases the quantity from 1 to 0 (or clicks the delete icon)
**Then** the product is removed from the cart
**And** the cart shows the empty cart message
**And** the cart counter in the header shows 0

---

## Acceptance Criteria

- [ ] Setting quantity to 0 removes the product
- [ ] Cart counter resets to 0
- [ ] Empty cart message is displayed
- [ ] No error messages appear
