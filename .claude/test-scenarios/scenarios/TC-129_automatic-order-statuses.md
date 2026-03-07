# TC-129: Order Status Automatically Set After Payment

**ID:** TC-129
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @orders

---

## Feature

Automatic Order Status Assignment

## User Story

As a shop admin, I want the order status to be set automatically based on the payment method used so no manual intervention is needed.

## Background

"Pay by Check" is configured to set status "Awaiting cheque payment" on order placement.

## Scenario

**Given** a customer places an order using "Pay by Check" payment method
**Then** the order status is automatically set to "Awaiting cheque payment"
**When** the admin manually updates the order status to "Payment accepted"
**Then** the order history reflects the status change with a timestamp

---

## Acceptance Criteria

- [ ] Order is created with "Awaiting cheque payment" status automatically
- [ ] Manual status update is saved and reflected in the order list
- [ ] Status history shows both the initial and updated statuses with timestamps
