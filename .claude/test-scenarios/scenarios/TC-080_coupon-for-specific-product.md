# TC-080: Coupon Applies Discount Only to Specific Product

**ID:** TC-080
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @coupon @product

---

## Feature

Product-Scoped Coupon

## User Story

As a shop admin, I want a coupon that discounts only one specific product, leaving other cart items at full price.

## Background

A cart rule: £10 off product "Hummingbird Printed T-Shirt" only, is active.

## Scenario

**Given** the customer adds "Hummingbird Printed T-Shirt" (£22.94) and another product (£30.00) to the cart
**When** the customer applies the product-specific coupon
**Then** the £10 discount is applied only to "Hummingbird Printed T-Shirt"
**And** the other product price is unchanged at £30.00

---

## Acceptance Criteria

- [ ] Discount applies only to the targeted product
- [ ] Other products in the cart are not discounted
- [ ] Cart total reflects partial discount correctly
