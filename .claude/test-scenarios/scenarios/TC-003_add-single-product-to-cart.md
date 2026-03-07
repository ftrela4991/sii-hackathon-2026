# TC-003: Add Single Product to Cart

**ID:** TC-003
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @cart @smoke

---

## Feature

Add Product to Cart

## User Story

As a customer, I want to add a product to my cart so that I can proceed to purchase it.

## Background

At least one product with quantity > 0 is available in the store.

## Scenario

**Given** the user is on the product detail page for "Hummingbird Printed T-Shirt"
**When** the user clicks the "Add to cart" button
**Then** the cart icon counter increments to 1
**And** the cart mini-preview shows the product name "Hummingbird Printed T-Shirt"
**And** the cart total reflects the correct product price

---

## Acceptance Criteria

- [ ] Cart counter updates immediately after adding the product
- [ ] Product name appears in the cart preview
- [ ] Cart total matches the product price
- [ ] No page reload required (AJAX update verified)
