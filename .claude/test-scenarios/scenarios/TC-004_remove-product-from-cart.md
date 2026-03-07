# TC-004: Remove Product from Cart

**ID:** TC-004
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @cart

---

## Feature

Remove Product from Cart

## User Story

As a customer, I want to remove a product from my cart so that I can adjust my order before checkout.

## Background

The cart contains exactly one product: "Hummingbird Printed T-Shirt".

## Scenario

**Given** the user has "Hummingbird Printed T-Shirt" in their cart
**And** the user is on the cart page `/cart`
**When** the user clicks the remove (delete) icon next to the product
**Then** the product is no longer listed in the cart
**And** the cart counter in the header shows 0
**And** the cart page displays an empty cart message

---

## Acceptance Criteria

- [ ] Product is removed from the cart immediately
- [ ] Cart counter resets to 0
- [ ] Empty cart message is visible
- [ ] No 5xx errors occur during removal
