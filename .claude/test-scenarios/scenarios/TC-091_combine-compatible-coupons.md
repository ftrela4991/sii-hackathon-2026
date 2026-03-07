# TC-091: Multiple Compatible Coupons Applied Together

**ID:** TC-091
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @coupon @combination

---

## Feature

Stackable Coupon Codes

## User Story

As a customer, I want to apply two compatible coupons at the same time so I can maximise my savings.

## Background

Coupon A: £5 off (compatible with other coupons). Coupon B: 10% off (compatible with other coupons). Cart total: £100.00.

## Scenario

**Given** the customer's cart total is "£100.00"
**When** the customer applies coupon A (£5 off)
**And** the customer applies coupon B (10% off)
**Then** both discounts are listed in the cart summary
**And** the cart total reflects both discounts applied (e.g. £100 - £5 = £95 - 10% = £85.50, or as configured by priority)

---

## Acceptance Criteria

- [ ] Both coupons are accepted without error
- [ ] Both discount lines appear in the cart
- [ ] Final total is consistent with the combined discount logic
