# TC-065: Product with quantity=0 and Ordering Allowed

**ID:** TC-065
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @product @stock

---

## Feature

Out-of-Stock Product – Ordering Permitted

## User Story

As a shop admin, I want customers to be able to order a product with zero stock when the allow-ordering setting is enabled.

## Background

A product exists with quantity = 0 and "allow orders when out of stock" = Yes.

## Scenario

**Given** the admin has created a product with quantity "0" and ordering when out of stock enabled
**When** a customer opens the product detail page
**Then** the "Add to cart" button is visible and active
**And** the product can be successfully added to the cart

---

## Acceptance Criteria

- [ ] "Add to cart" is active despite zero stock
- [ ] Product is added to the cart without error
- [ ] Checkout can proceed with this product
- [ ] Product is deleted via API after the test
