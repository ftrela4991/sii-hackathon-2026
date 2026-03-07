# TC-066: Product with quantity=1 (Last Item in Stock)

**ID:** TC-066
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @product @stock @boundary

---

## Feature

Last Item in Stock – Purchase

## User Story

As a customer, when only 1 unit remains in stock, I should be able to purchase it and see the stock reach zero.

## Background

A product exists with quantity = 1.

## Scenario

**Given** the admin has created a product with quantity "1"
**When** a customer opens the product detail page
**Then** the "Add to cart" button is active
**And** the customer adds the product to the cart
**And** the customer completes the order successfully
**Then** the product stock is reduced to 0

---

## Acceptance Criteria

- [ ] Product is purchasable with quantity 1
- [ ] Order completes successfully
- [ ] Stock in admin panel shows 0 after the order
- [ ] Product is deleted via API after the test
