# TC-109: Carriers with Weight Limits

**ID:** TC-109
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @shipping @boundary

---

## Feature

Carrier Weight Limit Enforcement

## User Story

As a shop admin, I want Standard Delivery to be unavailable for orders exceeding 10kg so heavy orders use appropriate carriers.

## Background

Standard Delivery has a maximum weight of 10kg.

## Scenario

**Given** a customer adds a product with weight "5kg" to the cart
**Then** Standard Delivery is available at the shipping step
**When** the customer adds another product making the total cart weight "11kg"
**Then** Standard Delivery is no longer listed (over weight limit)

---

## Acceptance Criteria

- [ ] Standard Delivery available at 5kg
- [ ] Standard Delivery unavailable above 10kg
- [ ] Alternative carriers (if configured) are shown for heavy orders
