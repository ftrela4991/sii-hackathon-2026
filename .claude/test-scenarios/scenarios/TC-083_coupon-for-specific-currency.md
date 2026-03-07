# TC-083: Coupon Valid Only in a Specific Currency

**ID:** TC-083
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @coupon @currency

---

## Feature

Currency-Restricted Coupon

## User Story

As a shop admin, I want a coupon to apply only when the customer is using EUR, so promotions can be currency-targeted.

## Background

A cart rule: 10% off, valid for EUR currency only, is active.

## Scenario

**Given** a customer is browsing in EUR and applies the EUR-only coupon
**Then** the 10% discount is applied to the cart
**When** the customer switches to GBP and tries the same coupon
**Then** the coupon is rejected with an error message

---

## Acceptance Criteria

- [ ] Coupon is accepted in EUR
- [ ] Coupon is rejected in GBP
- [ ] Error message is informative
