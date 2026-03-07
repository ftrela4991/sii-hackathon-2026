# TC-018: Add Discounted Product to Cart

**ID:** TC-018
**Category:** ORDER
**Type:** positive
**Priority:** medium
**Tags:** @cart @discount

---

## Feature

Discounted Product Cart Price

## User Story

As a customer, I want to add a product that is on sale and see the discounted price in the cart, not the original price.

## Background

A product has a specific price rule of -20% applied in the admin panel.

## Scenario

**Given** the product "Hummingbird Printed T-Shirt" has a 20% discount applied
**When** the customer adds the product to the cart
**Then** the cart displays both the original price and the discounted price
**And** the cart total reflects the discounted price, not the original price

---

## Acceptance Criteria

- [ ] Original (crossed-out) price is shown alongside the discounted price
- [ ] Cart total uses the discounted price
- [ ] Discount percentage or amount is indicated in the cart
