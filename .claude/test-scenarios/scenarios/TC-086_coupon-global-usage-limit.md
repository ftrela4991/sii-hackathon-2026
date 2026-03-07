# TC-086: Coupon with Global Usage Limit (First 3 Uses)

**ID:** TC-086
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @coupon @limit

---

## Feature

Global Coupon Usage Limit

## User Story

As a shop admin, I want a coupon to become invalid once it has been redeemed 3 times in total across all customers.

## Background

A cart rule: £5 off, global usage limit = 3, is active.

## Scenario

**Given** 3 different customers each use the coupon and place orders
**Then** all 3 orders have the £5 discount applied
**When** a 4th customer tries to apply the same coupon
**Then** an error is shown indicating the coupon has reached its usage limit

---

## Acceptance Criteria

- [ ] First 3 uses are accepted
- [ ] 4th use is rejected
- [ ] Error message references the usage limit
