# TC-087: Coupon Valid Only Within Defined Date Range

**ID:** TC-087
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @coupon @time

---

## Feature

Date-Ranged Coupon Validity

## User Story

As a shop admin, I want a coupon to automatically expire on its end date so I do not need to manually deactivate it.

## Scenario

**Given** the admin creates a coupon valid from [today] to [today + 1 day]
**When** a customer applies the coupon within the validity window
**Then** the discount is applied
**When** the end date has passed
**And** a customer tries to apply the same coupon
**Then** an error is shown: the coupon has expired

---

## Acceptance Criteria

- [ ] Coupon works during the validity window
- [ ] Coupon is automatically rejected after expiry
- [ ] Error message states the coupon is expired or no longer valid
