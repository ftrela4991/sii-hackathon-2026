# TC-012: Apply Fixed Amount Coupon (SAVE15)

**ID:** TC-012
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @admin @coupon @discount

---

## Feature

Fixed Amount Coupon Code

## User Story

As a customer, I want to apply a £15 coupon code at checkout so that my order total is reduced by exactly £15.

## Background

A cart rule with code "SAVE15", £15 fixed discount, no minimum order value, is active in the store.

## Scenario

**Given** the customer has a product worth "£50.00" in the cart
**When** the customer enters coupon code "SAVE15" in the discount code field at checkout
**And** the customer clicks "Apply"
**Then** the cart shows a discount line "-£15.00"
**And** the cart total updates to "£35.00"

---

## Acceptance Criteria

- [ ] Coupon is accepted without error
- [ ] Discount line shows exactly "-£15.00"
- [ ] Cart total is reduced to the correct amount
- [ ] Coupon code is case-insensitive (SAVE15 = save15) – if applicable
- [ ] Cart rule is deleted via API after the test
