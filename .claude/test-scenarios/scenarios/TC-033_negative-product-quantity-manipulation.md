# TC-033: Cart Rejects Negative Product Quantity

**ID:** TC-033
**Category:** ORDER
**Type:** negative
**Priority:** medium
**Tags:** @cart @validation @security

---

## Feature

Negative Quantity Input Protection

## User Story

As a customer, attempting to set a negative quantity for a cart item must be rejected so the cart total is never manipulated downward.

## Background

The cart contains at least one product.

## Scenario

**Given** the customer has "Hummingbird Printed T-Shirt" (quantity 1) in the cart
**When** the customer manually sets the quantity input field to "-1"
**And** the customer confirms the update
**Then** the quantity is not saved as a negative value
**And** the cart either shows a validation error, resets to 1, or removes the item

---

## Acceptance Criteria

- [ ] Negative quantity is not accepted
- [ ] Cart total does not decrease below zero due to negative quantity
- [ ] An appropriate response (error, reset, or removal) is shown
- [ ] No server error (5xx) occurs
