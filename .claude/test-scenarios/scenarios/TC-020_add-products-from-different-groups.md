# TC-020: Add Multiple Products from Different Category Groups

**ID:** TC-020
**Category:** ORDER
**Type:** positive
**Priority:** low
**Tags:** @cart @category

---

## Feature

Multi-Category Cart

## User Story

As a customer, I want to add products from different categories to the same cart and see them all listed correctly.

## Background

Products exist in "Clothes", "Accessories", and "Art" categories.

## Scenario

**Given** the customer's cart is empty
**When** the customer adds a product from the "Clothes" category
**And** the customer adds a product from the "Accessories" category
**And** the customer adds a product from the "Art" category
**Then** the cart contains 3 separate line items
**And** the cart total equals the sum of all three product prices

---

## Acceptance Criteria

- [ ] All 3 products are listed in the cart
- [ ] Each line item shows the correct product name and price
- [ ] Cart total is the arithmetic sum of all line items
