# TC-128: Checkout Blocked When Cart Exceeds Maximum Order Value

**ID:** TC-128
**Category:** PRODUCTS_AND_API
**Type:** negative
**Priority:** low
**Tags:** @admin @checkout @boundary

---

## Feature

Maximum Order Value Enforcement

## User Story

As a shop admin, I want orders above a maximum value to be blocked so high-risk or unusual orders are flagged.

## Background

Maximum order value is set to £500.00.

## Scenario

**Given** the customer's cart total is "£600.00"
**When** the customer tries to proceed to checkout
**Then** an error or warning is shown indicating the maximum order value has been exceeded
**And** the customer cannot complete the order

---

## Acceptance Criteria

- [ ] Cart above £500 is blocked at checkout
- [ ] Error message references the maximum order value
- [ ] No order is created when the maximum is exceeded
