# TC-064: Product with quantity=0 and Ordering Blocked

**ID:** TC-064
**Category:** PRODUCTS_AND_API
**Type:** negative
**Priority:** high
**Tags:** @product @stock

---

## Feature

Out-of-Stock Product – Add to Cart Blocked

## User Story

As a shop admin, when a product has zero stock and ordering is disabled, customers must not be able to add it to the cart.

## Background

A product exists with quantity = 0 and "allow orders when out of stock" = No.

## Scenario

**Given** the admin has created a product with quantity "0" and ordering when out of stock disabled
**When** a customer opens the product detail page
**Then** the "Add to cart" button is disabled or absent
**And** an out-of-stock indicator is visible on the product page

---

## Acceptance Criteria

- [ ] "Add to cart" is not functional
- [ ] Out-of-stock status is communicated to the customer
- [ ] Product cannot be added via any UI interaction
- [ ] Product is deleted via API after the test
