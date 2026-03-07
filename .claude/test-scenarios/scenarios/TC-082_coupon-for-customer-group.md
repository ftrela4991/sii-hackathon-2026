# TC-082: VIP25 Coupon Usable Only by VIP Customer Group

**ID:** TC-082
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @coupon @groups

---

## Feature

Customer Group Restricted Coupon

## User Story

As a shop admin, I want the VIP25 coupon to be exclusive to VIP group members so only they benefit.

## Background

A cart rule "VIP25": 25% off, restricted to group "VIP", is active.

## Scenario

**Given** a VIP customer applies coupon "VIP25" with a "£100.00" cart
**Then** the 25% discount is applied: "-£25.00"
**And** the cart total is "£75.00"
**When** a Regular group customer tries to apply "VIP25"
**Then** an error is displayed indicating the coupon is not available for their account

---

## Acceptance Criteria

- [ ] VIP customer receives the 25% discount
- [ ] Regular customer receives an error when applying the code
- [ ] Error message is user-friendly
