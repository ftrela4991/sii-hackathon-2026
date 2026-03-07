# TC-075: Combined Promotion: Category + Currency + Group + Minimum Quantity

**ID:** TC-075
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @promotion @complex

---

## Feature

Multi-Condition Combined Promotion

## User Story

As a shop admin, I want a single promotion with multiple conditions (category, currency, group, quantity) so it targets a very specific customer segment.

## Background

Conditions: product in "Clothes" category AND currency GBP AND group VIP AND quantity >= 2.

## Scenario

**Given** the admin creates a cart rule: 30% off when all conditions are met
**And** a VIP customer is browsing in GBP
**When** the customer adds 2 Clothes products to the cart
**Then** the 30% discount is applied to qualifying items
**When** any one condition is not met (e.g. only 1 item added)
**Then** the discount is not applied

---

## Acceptance Criteria

- [ ] All 4 conditions must be true for the discount to apply
- [ ] Removing any one condition prevents the discount
- [ ] Discount amount is 30% of qualifying item total
