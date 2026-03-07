# TC-081: Coupon Applies Discount Only to Specific Category

**ID:** TC-081
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @coupon @category

---

## Feature

Category-Scoped Coupon

## User Story

As a shop admin, I want a coupon that only discounts products from the "Art" category, leaving other categories at full price.

## Background

A cart rule: 15% off category "Art" only, is active.

## Scenario

**Given** the customer adds an "Art" product (£50.00) and a "Clothes" product (£30.00) to the cart
**When** the customer applies the category-specific coupon
**Then** the 15% discount is applied to the Art product: "-£7.50"
**And** the Clothes product price remains "£30.00"
**And** the cart total is "£72.50"

---

## Acceptance Criteria

- [ ] 15% discount applies only to Art category products
- [ ] Clothes product is unaffected
- [ ] Cart total is correct after partial discount
