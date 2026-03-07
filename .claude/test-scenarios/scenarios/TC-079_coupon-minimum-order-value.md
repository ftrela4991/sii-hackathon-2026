# TC-079: Coupon Requires Minimum Order Value of £100

**ID:** TC-079
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @coupon @discount @boundary

---

## Feature

Minimum Order Value Threshold for Coupon

## User Story

As a customer, a coupon with a minimum order value must only apply when my cart meets or exceeds that threshold.

## Background

A cart rule: 20% off, minimum cart value £100.00 is active.

## Scenario

**Given** the customer has products worth "£80.00" in the cart
**When** the customer tries to apply the coupon code
**Then** an error indicates the minimum order value of £100 is not met
**When** the customer adds more items so the cart total reaches "£100.01"
**And** the customer applies the coupon
**Then** the 20% discount is applied: "-£20.02"

---

## Acceptance Criteria

- [ ] Coupon is rejected when cart is below £100
- [ ] Error message references the minimum order value
- [ ] Coupon applies once threshold is met
- [ ] Discount amount is 20% of the qualifying total
