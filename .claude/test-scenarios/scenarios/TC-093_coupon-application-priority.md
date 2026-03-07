# TC-093: Coupons Applied in Correct Priority Order

**ID:** TC-093
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @coupon @priority

---

## Feature

Coupon Priority Order

## User Story

As a shop admin, I want coupons with higher priority to be applied before lower-priority ones so the discount calculation is predictable.

## Background

Coupon A: priority 1, 10% off. Coupon B: priority 2, £5 off. Both are compatible.

## Scenario

**Given** the customer's cart total is "£100.00"
**When** both coupons are applied (or automatically applied)
**Then** coupon A (priority 1) is applied first: £100 - 10% = £90.00
**And** coupon B (priority 2) is applied to the result: £90 - £5 = £85.00
**And** the cart total is "£85.00"

---

## Acceptance Criteria

- [ ] Higher priority coupon is applied first
- [ ] Lower priority coupon is applied to the already-discounted total
- [ ] Final cart total is £85.00
