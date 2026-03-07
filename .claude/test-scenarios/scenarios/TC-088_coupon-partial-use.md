# TC-088: Coupon with Partial Use Enabled Carries Remaining Value

**ID:** TC-088
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @coupon @partial-use

---

## Feature

Partial Use Coupon – Remaining Value Carried Forward

## User Story

As a customer, if my coupon value exceeds my cart total and partial use is enabled, I want the remaining balance saved for my next order.

## Background

A cart rule: £20 fixed amount, partial use = enabled, is active.

## Scenario

**Given** the customer's cart total is "£15.00"
**When** the customer applies the £20 coupon (partial use enabled)
**Then** the cart total becomes "£0.00" after the discount
**And** a new coupon for the remaining "£5.00" is generated for future use

---

## Acceptance Criteria

- [ ] Cart total is reduced to £0.00
- [ ] Remaining £5.00 credit is issued as a new coupon
- [ ] Original coupon is marked as used
- [ ] New coupon can be applied in a future cart
