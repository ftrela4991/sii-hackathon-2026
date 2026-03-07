# TC-077: FREESHIP Coupon Removes Shipping Costs

**ID:** TC-077
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** high
**Tags:** @coupon @shipping

---

## Feature

Free Shipping Coupon Code

## User Story

As a customer, I want to apply the FREESHIP coupon so that my shipping cost is waived.

## Background

A cart rule with code "FREESHIP" offering free shipping is active. Shipping normally costs £5.00.

## Scenario

**Given** the customer has items in the cart with a shipping cost of "£5.00"
**When** the customer enters coupon code "FREESHIP" at checkout
**And** the customer clicks "Apply"
**Then** the shipping line shows "£0.00"
**And** the cart total is reduced by the original £5.00 shipping amount

---

## Acceptance Criteria

- [ ] Shipping cost becomes £0.00 after applying the coupon
- [ ] Cart total is reduced by the shipping amount
- [ ] Coupon is listed in the discount summary
- [ ] Cart rule is deleted via API after the test
