# TC-076: Percentage Coupon Code SUMMER10 Applies 10% Discount

**ID:** TC-076
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** high
**Tags:** @coupon @discount

---

## Feature

Percentage Coupon Code Application

## User Story

As a customer, I want to apply coupon code "SUMMER10" and receive 10% off my order total.

## Background

A cart rule with code "SUMMER10", 10% percentage discount, is active.

## Scenario

**Given** the customer has a product worth "£100.00" in the cart
**When** the customer enters coupon code "SUMMER10" in the discount code field
**And** the customer clicks "Apply"
**Then** the cart shows a discount of "-£10.00"
**And** the cart total is "£90.00"

---

## Acceptance Criteria

- [ ] Coupon code is accepted
- [ ] Discount is 10% of the cart total
- [ ] Cart total reflects the reduced amount
- [ ] Cart rule is deleted via API after the test
