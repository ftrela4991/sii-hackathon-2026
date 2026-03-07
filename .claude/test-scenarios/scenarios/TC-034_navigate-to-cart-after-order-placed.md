# TC-034: Cart is Empty After Order is Successfully Placed

**ID:** TC-034
**Category:** ORDER
**Type:** positive
**Priority:** low
**Tags:** @cart @order @post-order

---

## Feature

Cart Cleared After Order Placement

## User Story

As a customer, after placing an order the cart should be empty so I am not accidentally charged twice.

## Background

The customer has just successfully placed an order.

## Scenario

**Given** the customer has successfully placed an order
**When** the customer navigates to the cart page `/cart`
**Then** the cart is empty
**And** the cart counter in the header shows 0

---

## Acceptance Criteria

- [ ] Cart is empty immediately after order placement
- [ ] Cart counter shows 0
- [ ] Empty cart message is displayed
- [ ] Previously ordered products are not still listed in the cart
