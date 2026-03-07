# TC-106: Different Shipping Prices per Carrier

**ID:** TC-106
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @shipping @pricing

---

## Feature

Carrier-Specific Shipping Prices

## User Story

As a shop admin, I want Standard delivery to cost £5 and Express to cost £15 so customers can make an informed choice.

## Background

Standard Delivery: £5.00. Express Delivery: £15.00.

## Scenario

**Given** Standard Delivery is priced at "£5.00" and Express Delivery at "£15.00"
**When** a customer reaches the shipping step at checkout
**Then** Standard Delivery shows "£5.00"
**And** Express Delivery shows "£15.00"
**And** selecting each carrier updates the order total accordingly

---

## Acceptance Criteria

- [ ] Correct price shown for each carrier
- [ ] Order total updates when carrier is switched
- [ ] Price difference between carriers is correctly reflected in the total
