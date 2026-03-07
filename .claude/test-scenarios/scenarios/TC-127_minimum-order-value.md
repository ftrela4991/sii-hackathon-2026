# TC-127: Checkout Blocked When Cart is Below Minimum Order Value

**ID:** TC-127
**Category:** PRODUCTS_AND_API
**Type:** negative
**Priority:** medium
**Tags:** @admin @checkout @boundary

---

## Feature

Minimum Order Value Enforcement

## User Story

As a shop admin, I want to prevent orders below a minimum value threshold so unprofitable small orders are avoided.

## Background

Minimum order value is set to £10.00 in shop preferences.

## Scenario

**Given** the customer's cart total is "£8.00"
**When** the customer tries to proceed to checkout
**Then** an error is shown: the minimum order amount is £10.00
**And** the customer cannot proceed past the cart step
**When** the customer adds more items to reach "£10.01"
**Then** the customer can proceed to checkout without error

---

## Acceptance Criteria

- [ ] Cart below £10 is blocked at checkout
- [ ] Error message states the minimum amount
- [ ] Cart at £10.01 allows checkout to proceed
