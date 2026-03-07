# TC-071: Fixed Amount Promotion on a Specific Brand

**ID:** TC-071
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @promotion @brand

---

## Feature

Brand-Scoped Fixed Discount

## User Story

As a shop admin, I want to apply a £10 discount on all products from a specific brand so those customers get a better deal.

## Background

Brand "BrandX" exists with at least one product priced above £10.

## Scenario

**Given** the admin creates a cart rule: "£10 off all products from BrandX"
**When** a customer adds a BrandX product worth "£50.00" to the cart
**And** the cart rule is automatically applied
**Then** the cart shows a discount line "-£10.00"
**And** the cart total is "£40.00"

---

## Acceptance Criteria

- [ ] Discount applies automatically for BrandX products
- [ ] Non-BrandX products are not discounted
- [ ] Discount amount is exactly £10.00
- [ ] Cart rule is deleted via API after the test
