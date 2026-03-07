# TC-092: Mutually Exclusive Coupons Cannot Be Combined

**ID:** TC-092
**Category:** PRODUCTS_AND_API
**Type:** negative
**Priority:** medium
**Tags:** @coupon @combination

---

## Feature

Coupon Exclusivity Enforcement

## User Story

As a shop admin, I want certain coupons to be exclusive so customers cannot stack incompatible promotions.

## Background

Coupon A has "cannot be used with other coupons" = Yes.

## Scenario

**Given** the customer's cart already has coupon A applied
**When** the customer tries to apply coupon B
**Then** an error is displayed indicating the coupons cannot be combined
**And** only coupon A remains applied in the cart

---

## Acceptance Criteria

- [ ] Coupon B is rejected when coupon A is exclusive
- [ ] Error message is clear and informative
- [ ] Coupon A remains applied after the rejection
- [ ] Cart total reflects only coupon A's discount
