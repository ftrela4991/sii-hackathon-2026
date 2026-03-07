# TC-078: Automatic Cart Rule Applied Without Coupon Code

**ID:** TC-078
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @coupon @automatic

---

## Feature

Automatic Discount Without Code Entry

## User Story

As a customer, I want qualifying discounts to be applied automatically without having to enter a coupon code.

## Background

A cart rule with no code, 5% off all orders, is active.

## Scenario

**Given** the customer adds any product to the cart
**When** the customer views the cart page
**Then** the 5% discount is automatically visible without the customer entering any code
**And** the cart total reflects the discounted amount

---

## Acceptance Criteria

- [ ] Discount appears without any user action (no code entry required)
- [ ] Discount is 5% of the cart subtotal
- [ ] Cart total is correct after automatic discount
