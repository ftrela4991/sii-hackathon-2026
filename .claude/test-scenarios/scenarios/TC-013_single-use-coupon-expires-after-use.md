# TC-013: Single-Use Coupon Expires After First Use

**ID:** TC-013
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @admin @coupon @one-time

---

## Feature

Single-Use Coupon Invalidation

## User Story

As a shop admin, I want a coupon with a total usage limit of 1 to become invalid after the first redemption so it cannot be reused.

## Background

A cart rule with code "ONCE10", £10 discount, total usage limit = 1 is active.

## Scenario

**Given** customer A places an order using coupon code "ONCE10"
**Then** the order is confirmed with a "£10.00" discount applied
**When** customer B tries to apply coupon code "ONCE10" in a new cart
**Then** the system displays an error indicating the coupon is no longer valid or has reached its usage limit

---

## Acceptance Criteria

- [ ] First use of the coupon is accepted and discount applied
- [ ] Second attempt to use the same coupon is rejected
- [ ] Error message is clear and informative
- [ ] Coupon is deleted via API after the test
