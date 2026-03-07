# TC-010: Apply Percentage Discount on Entire Category

**ID:** TC-010
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @admin @promotion @category @discount

---

## Feature

Category-Wide Percentage Discount

## User Story

As a shop admin, I want to apply a 20% discount to all products in a category so that customers get a consistent sale price.

## Background

A product in category "Clothes" is priced at £100.00. A cart rule with 20% discount restricted to "Clothes" category is active.

## Scenario

**Given** the admin has created a cart rule: 20% off all products in "Clothes" category
**When** a customer adds a Clothes product priced £100.00 to the cart
**And** the cart rule is automatically applied
**Then** the cart shows a discount line of "-£20.00"
**And** the cart total is "£80.00"

---

## Acceptance Criteria

- [ ] Discount is applied automatically without a coupon code
- [ ] Discount amount is exactly 20% of the product price
- [ ] Cart total reflects the discounted amount
- [ ] Products outside the category are not discounted
- [ ] Cart rule is deleted via API after the test
