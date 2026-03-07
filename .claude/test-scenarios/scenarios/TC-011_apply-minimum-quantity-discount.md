# TC-011: Apply Minimum Quantity Discount

**ID:** TC-011
**Category:** BASIC
**Type:** positive
**Priority:** medium
**Tags:** @admin @promotion @quantity @discount

---

## Feature

Quantity-Based Discount Rule

## User Story

As a shop admin, I want customers to receive a 10% discount when they buy at least 2 units of the same product.

## Background

A specific price rule exists: quantity >= 2 triggers 10% discount. A product is priced at £50.00.

## Scenario

**Given** a product priced at "£50.00" is in the store
**And** a quantity discount rule is active: 10% off when quantity >= 2
**When** the customer adds 1 unit to the cart
**Then** no discount is applied and the cart total is "£50.00"
**When** the customer increases the quantity to 2
**Then** a 10% discount is applied: "-£10.00"
**And** the cart total is "£90.00"

---

## Acceptance Criteria

- [ ] No discount at quantity 1
- [ ] 10% discount applies at quantity 2
- [ ] Discount amount is calculated correctly
- [ ] Cart total reflects the discounted price
