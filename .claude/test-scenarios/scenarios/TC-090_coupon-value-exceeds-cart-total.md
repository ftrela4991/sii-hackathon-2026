# TC-090: Coupon Value Greater Than Cart Total Does Not Result in Negative Amount

**ID:** TC-090
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @coupon @boundary

---

## Feature

Coupon Over-Value Protection

## User Story

As a customer, if my coupon value exceeds my cart total and partial use is disabled, the final amount should be £0.00, not negative.

## Background

A cart rule: £30 fixed amount, partial use = disabled, is active.

## Scenario

**Given** the customer's cart total is "£20.00"
**When** the customer applies the £30 coupon (partial use disabled)
**Then** the cart total is shown as "£0.00"
**And** the customer is not owed £10.00 credit

---

## Acceptance Criteria

- [ ] Cart total floors at £0.00, never goes negative
- [ ] No credit or change is issued (partial use disabled)
- [ ] Order can be placed at £0.00 total
