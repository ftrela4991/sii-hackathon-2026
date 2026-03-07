# TC-074: Time-Limited Promotion Active Only During Validity Period

**ID:** TC-074
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @promotion @time

---

## Feature

Time-Bounded Promotion Expiry

## User Story

As a shop admin, I want a promotion to stop applying automatically after its end date so I do not need to deactivate it manually.

## Scenario

**Given** the admin creates a cart rule valid from today to today (same-day expiry)
**When** a customer applies the cart rule code during the valid period
**Then** the discount is applied
**When** the cart rule's end date has passed
**And** a customer tries to apply the same code
**Then** an error is shown stating the coupon is expired or no longer valid

---

## Acceptance Criteria

- [ ] Coupon works during validity window
- [ ] Coupon is rejected after expiry date
- [ ] Error message clearly states the coupon has expired
