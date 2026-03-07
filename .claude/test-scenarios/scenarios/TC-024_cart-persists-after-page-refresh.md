# TC-024: Cart Persists After Page Refresh

**ID:** TC-024
**Category:** ORDER
**Type:** positive
**Priority:** medium
**Tags:** @cart @session

---

## Feature

Cart Session Persistence on Refresh

## User Story

As a customer, my cart contents should be preserved when I refresh the page so I do not lose my selection.

## Background

The cart contains at least one product.

## Scenario

**Given** the customer has "Hummingbird Printed T-Shirt" (quantity 1) in the cart
**When** the customer refreshes the current page
**Then** the cart still contains "Hummingbird Printed T-Shirt" with quantity 1
**And** the cart total is unchanged

---

## Acceptance Criteria

- [ ] Cart contents survive a full page reload (F5)
- [ ] Product names, quantities, and prices are unchanged after refresh
- [ ] Cart counter in the header reflects the correct count
