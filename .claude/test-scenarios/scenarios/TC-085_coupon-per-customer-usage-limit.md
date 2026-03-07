# TC-085: Coupon Limited to 1 Use Per Customer

**ID:** TC-085
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @coupon @limit

---

## Feature

Per-Customer Usage Limit on Coupon

## User Story

As a shop admin, I want each customer to be able to use a coupon only once so the promotion cannot be stacked per customer.

## Background

A cart rule: £5 off, usage limit per customer = 1, is active.

## Scenario

**Given** customer A uses the coupon and places an order
**Then** the order has the £5 discount applied
**When** customer A tries to apply the same coupon in a new cart
**Then** an error is shown: the customer has already used this coupon
**When** customer B (a different account) applies the coupon
**Then** the £5 discount is applied successfully for customer B

---

## Acceptance Criteria

- [ ] First use by customer A is accepted
- [ ] Second use by customer A is rejected
- [ ] Customer B can still use the coupon independently
