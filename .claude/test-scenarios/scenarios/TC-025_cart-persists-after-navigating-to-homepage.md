# TC-025: Cart Persists After Navigating to Homepage

**ID:** TC-025
**Category:** ORDER
**Type:** positive
**Priority:** medium
**Tags:** @cart @session

---

## Feature

Cart Session Persistence Across Navigation

## User Story

As a customer, my cart contents should be preserved when I navigate away from the cart page and return.

## Background

The cart contains at least one product.

## Scenario

**Given** the customer has "Hummingbird Printed T-Shirt" (quantity 1) in the cart
**When** the customer clicks the store logo to navigate to the homepage
**Then** the cart icon counter in the header still shows 1
**When** the customer opens the cart page
**Then** the product "Hummingbird Printed T-Shirt" is still listed with the correct price

---

## Acceptance Criteria

- [ ] Cart counter visible on homepage after navigation
- [ ] Cart contents intact on returning to cart page
- [ ] No products are silently dropped during navigation
